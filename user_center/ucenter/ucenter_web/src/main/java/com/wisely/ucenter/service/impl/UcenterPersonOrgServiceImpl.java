package com.wisely.ucenter.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.client.handler.UcDictHelper;
import com.wisely.ucenter.client.vo.UcenterOrgVo;
import com.wisely.ucenter.client.vo.UcenterPersonVo;
import com.wisely.ucenter.entity.UcenterOrg;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterPersonOrg;
import com.wisely.ucenter.mapper.UcenterOrgMapper;
import com.wisely.ucenter.mapper.UcenterPersonMapper;
import com.wisely.ucenter.mapper.UcenterPersonOrgMapper;
import com.wisely.ucenter.service.UcenterPersonOrgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 部门/机构人员中间表(UcenterPersonOrg)表服务实现类
 *
 * @author xintao.li
 * @since 2021-05-31 10:44:09
 */
@Service
public class UcenterPersonOrgServiceImpl implements UcenterPersonOrgService {

    @Resource
    UcenterPersonOrgMapper ucenterPersonOrgMapper;
    @Resource
    UcenterPersonMapper ucenterPersonMapper;
    @Resource
    UcenterOrgMapper ucenterOrgMapper;


    @Override
    public List<UcenterPersonOrg> list(UcenterPersonOrg query) {
        query.setIsDeleted(0);
        return ucenterPersonOrgMapper.selectListBySelective(query);
    }

    @Override
    public PageInfo findByPage(UcenterPersonOrg query, PageVo pageVo) {
        query.setIsDeleted(0);

        UcenterOrg ucenterOrg = new UcenterOrg();
        List<UcenterOrg> ucenterOrgs = new ArrayList<>();
        Model model = RequestHelper.getInput();
        if (model.isNotBlank("pathIds")) {
            ucenterOrg.setPathIdsQueryLike(model.getString("pathIds"));
            ucenterOrg.setIsDeleted(0);
            ucenterOrgs = ucenterOrgMapper.selectListBySelective(ucenterOrg);
            String ucenterIds = "";
            if (ValidHelper.isNotEmpty(ucenterOrgs)) {
                ucenterIds = ucenterOrgs.stream().map(org -> DataHelper.getString(org.getId())).collect(Collectors.joining(","));
                query.setDeptIdQueryIn(ucenterIds);
            }
        }
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        List<UcenterPersonOrg> list = ucenterPersonOrgMapper.selectQueryListBySelective(query);
        return new PageInfo(list);
    }


    /**
     * 按部门调整人员关系
     *
     * @param input
     * @return
     */
    @Transactional
    @Override
    public int add(Model input) {

        Integer deptId = input.getInt("deptId");
        Model personModel = input.getModel("personModel");
        String personIds = StringHelper.join(personModel.keySet(), ",");

        SsoUser ssoUser = UserHelper.getUser();
        String now = DateHelper.formatNow();

        // 当前部门移出人员
        List<Integer> toRootPersonList = null;
        if (ValidHelper.isNotEmpty(personModel)) {
            // 获取从当前部门移除的人员
            UcenterPersonOrg personOrgQuery = new UcenterPersonOrg();
            personOrgQuery.setDeptId(deptId);
            personOrgQuery.setPersonIdQueryNotIn(personIds);
            personOrgQuery.setIsDeleted(0);
            List<UcenterPersonOrg> personOrgs =
                    ucenterPersonOrgMapper.selectListBySelective(personOrgQuery);
            if (ValidHelper.isNotEmpty(personOrgs)) {
                toRootPersonList =
                        personOrgs.stream()
                                .map(item -> item.getPersonId()).collect(Collectors.toList());
            }
        }

        // 按部门删除原关系
        ucenterPersonOrgMapper.deleteBySelective(Model.builder().set("personIdQueryIn", personIds));

        UcenterOrgVo deptVo = UcDictHelper.loadOrgVo(deptId);
        AssertHelper.EX_BUSINESS.isNotEmpty(deptVo, "common.entity_not_found.UcenterOrgCache_{0}", deptId);
        Integer orgId = ValidHelper.isNotEmpty(deptVo.getOrgId()) ? deptVo.getOrgId() : deptId;

        // 插入新的部门人员关系
        personModel.forEach((k, v) -> {

            Integer personId = DataHelper.getInt(k);

            UcenterPersonVo personVo = UcDictHelper.loadPersonVo(personId);

            UcenterPersonOrg personOrgRecord = new UcenterPersonOrg();
            personOrgRecord.setRootOrgId(orgId);
            personOrgRecord.setDeptId(deptId);
            personOrgRecord.setPersonId(personId);
            personOrgRecord.setOrderNo(DataHelper.getInt(v, 9999));
            personOrgRecord.setIsMain(1); // 设为主要部门
            personOrgRecord.setIsDeleted(0);
            personOrgRecord.setCreateBy(ssoUser.getId());
            personOrgRecord.setCreateTime(now);
            ucenterPersonOrgMapper.insertSelective(personOrgRecord);
        });

        // 处理移出人员
        if (ValidHelper.isNotEmpty(toRootPersonList)) {
            toRootPersonList.forEach(personId -> {

                UcenterPersonVo personVo = UcDictHelper.loadPersonVo(personId);

                UcenterPersonOrg rootOrgPersonRecord = new UcenterPersonOrg();
                rootOrgPersonRecord.setRootOrgId(orgId);
                rootOrgPersonRecord.setDeptId(orgId);
                rootOrgPersonRecord.setPersonId(personId);
                rootOrgPersonRecord.setOrderNo(9999);
                rootOrgPersonRecord.setIsMain(1); // 设为主要部门
                rootOrgPersonRecord.setIsDeleted(0);
                rootOrgPersonRecord.setCreateBy(ssoUser.getId());
                rootOrgPersonRecord.setCreateTime(now);
                ucenterPersonOrgMapper.insertSelective(rootOrgPersonRecord);
            });
        }
        return 0;
    }

    /**
     * 人员部门及机构关系维护
     *
     * @param deptInfoList  部门信息
     * @param ucenterPerson
     */
    @Transactional
    @Override
    public void updatePersonOrgByList(List<Model> deptInfoList, UcenterPerson ucenterPerson) {

        // 首先删除人员原有的部门/机构关系
        ucenterPersonOrgMapper.deleteBySelective(Model.builder().set("personIdQueryIn", DataHelper.getString(ucenterPerson.getId())));

        // 将ucenterPersonOrgs [{"deptId":部门id,"isMain":是否主组织 1-是 0-否,"isMainPerson": 1-是  0-否}] 保存到T_UCENTER_PERSON中
        // 如果是主组织，则需要将这个部门的最顶级的父部门id赋值给T_UCENTER_PERSON的ORG_ID字段
        if (ValidHelper.isEmpty(deptInfoList)) {
            return;
        }

        String now = DateHelper.formatNow();
        Integer userId = UserHelper.getUserId();

        deptInfoList.forEach(item -> {

            UcenterPersonOrg record = (UcenterPersonOrg) item.convertTo(UcenterPersonOrg.class);

            UcenterOrgVo dept = UcDictHelper.loadOrgVo(record.getDeptId());
            record.setRootOrgId(ValidHelper.isNotEmpty(dept.getOrgId()) ? dept.getOrgId() : dept.getId());
            record.setPersonId(ucenterPerson.getId());
            record.setIsDeleted(0);
            record.setCreateBy(userId);
            record.setCreateTime(now);
            ucenterPersonOrgMapper.insertSelective(record);

            // 主部门 设置对应orgId
            if (!item.equals("isMain", 1)) {
                return;
            }

            UcenterPerson personRecord = new UcenterPerson();
            personRecord.setId(ucenterPerson.getId());
            personRecord.setOrgId(ValidHelper.isNotEmpty(dept.getOrgId()) ? dept.getOrgId() : dept.getId());
            personRecord.setOrgName(dept.getOrgName());
            personRecord.setUpdateBy(userId);
            personRecord.setUpdateTime(now);
            ucenterPersonMapper.updateByPrimaryKeySelective(personRecord);
        });

    }

    @Override
    public Model view(Integer personId) {
        UcenterPerson person = ucenterPersonMapper.selectByPrimaryKey(personId);
        AssertHelper.EX_VALIDATION.isNotEmpty(person, "common.entity_not_found.UcenterPerson_{0}", personId);
        Model model = Model.parseObject(person);

        UcenterPersonOrg query = new UcenterPersonOrg();
        query.setPersonId(personId);
        query.setIsDeleted(0);
        List<UcenterPersonOrg> ucenterPersonOrgs = ucenterPersonOrgMapper.selectListBySelective(query);
        if (ValidHelper.isNotEmpty(ucenterPersonOrgs)) {
            model.set("orderNo", ucenterPersonOrgs.get(0).getOrderNo());
            model.set("deptId", ucenterPersonOrgs.get(0).getDeptId());
            model.set("personId", personId);
        }
        return model;
    }


    @Override
    public void deleteByPersonId(Integer personId) {
        ucenterPersonOrgMapper.deleteBySelective(
                Model.builder().set("personIdQueryIn", DataHelper.getString(personId)));
    }
}
