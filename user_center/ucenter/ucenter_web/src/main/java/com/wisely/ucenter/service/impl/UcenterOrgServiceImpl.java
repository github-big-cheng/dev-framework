package com.wisely.ucenter.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.caches.OrgCache;
import com.wisely.ucenter.client.handler.UcenterDictionaryHelper;
import com.wisely.ucenter.client.vo.UcenterPositionVo;
import com.wisely.ucenter.entity.UcenterOrg;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterPersonOrg;
import com.wisely.ucenter.mapper.UcenterOrgMapper;
import com.wisely.ucenter.mapper.UcenterPersonMapper;
import com.wisely.ucenter.mapper.UcenterPersonOrgMapper;
import com.wisely.ucenter.service.UcenterOrgInfoService;
import com.wisely.ucenter.service.UcenterOrgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wisely.sso.client.SsoConstants.ROLE_SUPER_ADMIN;

/**
 * 机构/部门信息(TUcenterOrg)表服务实现类
 *
 * @author xintao.li
 * @since 2021-05-31 10:28:31
 */
@Service
public class UcenterOrgServiceImpl implements UcenterOrgService {

    @Resource
    UcenterOrgMapper ucenterOrgMapper;
    @Resource
    UcenterPersonOrgMapper ucenterPersonOrgMapper;
    @Resource
    UcenterPersonMapper ucenterPersonMapper;

    @Resource
    UcenterOrgInfoService ucenterOrgInfoService;
    @Resource
    OrgCache orgCache;


    @Override
    public List<Model> findList(UcenterOrg query) {

        List<Model> list = Lists.newArrayList();

        query.setIsDeleted(0);
        List<UcenterOrg> orgList = ucenterOrgMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(orgList)) {
            return list;
        }

        Set<Integer> ids = orgList.stream().map(UcenterOrg::getId).collect(Collectors.toSet());
        Model<Integer, Model> infoModel = ucenterOrgInfoService.orgInfoQuery(StringHelper.join(ids, ","));
        orgList.forEach(org -> list.add(Model.parseObject(org).set("info", infoModel.get(org.getId()))));

        return list;
    }


    @Override
    public PageInfo findByPage(UcenterOrg query, PageVo pageVo) {

        Model input = RequestHelper.getInput();

        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        query.setIsDeleted(0);
        //不是管理员，过滤机构
        if (ValidHelper.isNull(query.getId()) &&
                !UserHelper.hasRole(ROLE_SUPER_ADMIN) &&
                input.isEmpty("isAll")) {
            query.setIdOrOrgIdQueryIn(UserHelper.getOrganizations());
        }

        List<UcenterOrg> orgList = ucenterOrgMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(orgList)) {
            return new PageInfo(orgList);
        }

        List<Model> list = Lists.newArrayList();
        Set<Integer> ids = orgList.stream().map(UcenterOrg::getId).collect(Collectors.toSet());
        Model<Integer, Model> infoModel = ucenterOrgInfoService.orgInfoQuery(StringHelper.join(ids, ","));
        orgList.forEach(org -> list.add(Model.parseObject(org).set("info", infoModel.get(org.getId()))));

        return new PageInfo(list);
    }


    @Transactional
    @Override
    public int save(UcenterOrg record) {
        try {
            // org层级
            record.setPathIds(StringHelper.join("/", record.getCode(), "/"));
            record.setPathNames(StringHelper.join("/", record.getCname(), "/"));

            //校验上级部门是否是部门本身，如果是则抛出异常
            if (ValidHelper.isNotEmpty(record.getParentId())) {
                AssertHelper.EX_VALIDATION.isNotEquals(record.getParentId(), record.getId(), "ucenter_org_save.invalid_parent_org");

                // 生成路径,用于级联查询
                com.wisely.ucenter.client.vo.UcenterOrgVo orgVo =
                        UcenterDictionaryHelper.loadOrgVo(record.getParentId());
                if (ValidHelper.isNotEmpty(orgVo)) {
                    record.setPathIds(StringHelper.join(orgVo.getPathIds(), "/", record.getCode(), "/"));
                    record.setPathNames(StringHelper.join(orgVo.getPathNames(), "/", record.getCname(), "/"));
                }
                // 设置所属机关单位
                record.setOrgId(orgVo.getId());
                record.setOrgName(orgVo.getCname());
            }

            //重复code校验
            UcenterOrg query = new UcenterOrg();
            query.setIsDeleted(0);
            query.setCode(record.getCode());
            List<UcenterOrg> ucenterOrgList = ucenterOrgMapper.selectListBySelective(query);

            if (ValidHelper.isNull(record.getId())) {
                //判断code重复校验
                AssertHelper.EX_BUSINESS.isEmpty(ucenterOrgList, "ucenter_org_save.repeat_code_found.{0}", record.getCode());

                //是否是法人 默认保存为0
                record.setCreateBy(UserHelper.getUserId());
                record.setCreateTime(DateHelper.formatNow());
                record.setIsDeleted(0);
                ucenterOrgMapper.insertSelective(record);
            } else {
                //修改需要判断code是否重复。
                if (ValidHelper.isNotEmpty(ucenterOrgList)) {
                    UcenterOrg entity = ucenterOrgMapper.selectByPrimaryKey(record.getId());
                    AssertHelper.EX_BUSINESS.isNotEmpty(entity, "common.entity_not_found.id_{0}", record.getId());

                    ucenterOrgList.forEach(item ->
                            AssertHelper.EX_BUSINESS.isNotEquals(
                                    item.getId(),
                                    entity.getId(),
                                    "ucenter_org_save.repeat_code_found.{0}",
                                    record.getCode()));
                }

                record.setUpdateBy(UserHelper.getUserId());
                record.setUpdateTime(DateHelper.formatNow());
                ucenterOrgMapper.updateByPrimaryKeySelective(record);
            }

            // 保存扩展信息
            Model input = RequestHelper.getInput();
            ucenterOrgInfoService.save(record, input.getModel("info"));
        } finally {
            UcenterOrg cache = ucenterOrgMapper.selectByPrimaryKey(record.getId());
            if (ValidHelper.isNotEmpty(cache)) {
                orgCache.syncCache(record);
            }
        }

        return record.getId();
    }

    @Override
    public Model load(Integer id) {
        UcenterOrg org = ucenterOrgMapper.selectByPrimaryKey(id);
        Model entity = Model.parseObject(org);
        if (ValidHelper.isEmpty(org)) {
            return entity;
        }

        Model<Integer, Model> orgInfoModel = ucenterOrgInfoService.orgInfoQuery(DataHelper.getString(id));
        entity.set("info", orgInfoModel.get(id));
        return entity;
    }


    @Transactional
    @Override
    public int delete(String idQueryIn) {

        Integer userId = UserHelper.getUserId();
        String now = DateHelper.formatNow();

        UcenterOrg record = new UcenterOrg();
        record.setIdQueryIn(idQueryIn);
        record.setIsDeleted(1);
        record.setUpdateBy(userId);
        record.setUpdateTime(now);
        ucenterOrgMapper.updateBySelective(record);

        ucenterOrgInfoService.save(record, null);

        return 0;
    }

    /**
     * 显示树状部门
     *
     * @param query
     * @return
     */
    @Override
    public List<Model> tree(UcenterOrg query) {

        Model input = RequestHelper.getInput();
        query.setIsDeleted(0);
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN) && input.isEmpty("isAll")) {
            query.setIdOrOrgIdQueryIn(UserHelper.getOrganizations());
        }

        List<UcenterOrg> orgList = ucenterOrgMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(orgList)) {
            return Lists.newArrayList();
        }

        Model<Integer, List<Model>> model = Model.builder();
        orgList.forEach(org -> {
            Model orgModel = Model.parseObject(org);
            model.getList(org.getParentId(), true).add(orgModel);
            orgModel.set("children", model.getList(org.getId(), true));
        });
        return model.get(null);
    }

    @Override
    public List<Model> deptPersonTree(UcenterOrg query) {

        Model input = RequestHelper.getInput();

//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start("org list query");

        query.setIsDeleted(0);
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN) && input.isEmpty("isAll")) {
            query.setIdOrOrgIdQueryIn(UserHelper.getOrganizations());
        }
        List<UcenterOrg> orgList = ucenterOrgMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(orgList)) {
            return Lists.newArrayList();
        }

//        stopWatch.stop();
//        stopWatch.start("person list query");

        UcenterPersonOrg personOrgQuery = new UcenterPersonOrg();
        personOrgQuery.setIsDeleted(0);
        if (input.isNotEmpty("isMain")) {
            personOrgQuery.setIsMain(1);
        }
        if (input.isNotBlank("personIdQueryIn")) {
            personOrgQuery.setPersonIdQueryIn(input.getString("personIdQueryIn"));
        }
        List<UcenterPersonOrg> personOrgList = ucenterPersonOrgMapper.selectListBySelective(personOrgQuery);


        // 人员信息查询
        Model<Integer, Model> personModel = Model.builder();
        UcenterPerson personQuery = new UcenterPerson();
        personQuery.setIsDeleted(0);
        List<UcenterPerson> personList = ucenterPersonMapper.selectListBySelective(personQuery);
        if (ValidHelper.isNotEmpty(personList)) {
            personList.forEach(person -> personModel.set(person.getId(), Model.parseObject(person)));
        }

        // 将人员按deptId分组
        Model<Integer, List<Model>> personOrgModel = Model.builder();
        if (ValidHelper.isNotEmpty(personOrgList)) {
            personOrgList.forEach(deptPerson -> {
                Model person = personModel.get(deptPerson.getPersonId());
                if (ValidHelper.isEmpty(person)) {
                    return;
                }
                person.set("deptId", deptPerson.getDeptId());
                personOrgModel.getList(deptPerson.getDeptId(), true).add(person);
            });
        }

//        stopWatch.stop();
//        stopWatch.start("tree build");

        Model<Integer, List<Model>> model = Model.builder();
        orgList.forEach(org -> {
            Model orgModel = Model.parseObject(org);
            // 设置人员信息
            List<Model> listPerson = personOrgModel.getList(org.getId(), true);
            orgModel.set("listPerson", listPerson);
            orgModel.set("totalNumber", listPerson.size());

            model.getList(org.getParentId(), true).add(orgModel);
            orgModel.set("children", model.getList(org.getId(), true));
        });

        return model.get(null);
    }


    @Override
    public List<Model> deptPositionTree(UcenterOrg query) {

        Model input = RequestHelper.getInput();

        query.setIsDeleted(0);
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN) && input.isEmpty("isAll")) {
            query.setIdOrOrgIdQueryIn(UserHelper.getOrganizations());
        }

        List<UcenterOrg> orgList = ucenterOrgMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(orgList)) {
            return Lists.newArrayList();
        }

        UcenterPersonOrg personOrgQuery = new UcenterPersonOrg();
        personOrgQuery.setIsDeleted(0);
        if (input.isNotEmpty("isMain")) {
            personOrgQuery.setIsMain(1);
        }
        if (input.isNotBlank("personIdQueryIn")) {
            personOrgQuery.setPersonIdQueryIn(input.getString("personIdQueryIn"));
        }
        List<UcenterPersonOrg> personOrgList = ucenterPersonOrgMapper.selectListBySelective(personOrgQuery);


        // 将职位按deptId分组
        Model<Integer, Set> orgPositionModel = Model.builder();
        if (ValidHelper.isNotEmpty(personOrgList)) {
            Set<String> deptPositionSet = Sets.newLinkedHashSet();
            personOrgList.forEach(deptPerson -> {
                String key = deptPerson.getId() + "-" + deptPerson.getPosId();
                if (deptPositionSet.contains(key)) {
                    // 职位已添加
                    return;
                }

                deptPositionSet.add(key);

                UcenterPositionVo positionVo =
                        UcenterDictionaryHelper.loadPositionVo(deptPerson.getPosId());
                if (ValidHelper.isEmpty(positionVo)) {
                    return;
                }
                orgPositionModel.getList(deptPerson.getDeptId(), true).add(Model.parseObject(positionVo));
            });
        }

        Model<Integer, List<Model>> model = Model.builder();
        orgList.forEach(org -> {
            Model orgModel = Model.parseObject(org);
            // 设置人员信息
            List<Model> listPerson = orgPositionModel.getList(org.getId(), true);
            orgModel.set("positions", listPerson);
            orgModel.set("totalNumber", listPerson.size());

            model.getList(org.getParentId(), true).add(orgModel);
            orgModel.set("children", model.getList(org.getId(), true));
        });

        return model.get(null);
    }
}
