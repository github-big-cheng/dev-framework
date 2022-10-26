package com.wisely.ucenter.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.*;
import com.wisely.framework.helper.encry.Md5Helper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.api.SysNetApi;
import com.wisely.ucenter.caches.UserCache;
import com.wisely.ucenter.client.handler.UcDictHelper;
import com.wisely.ucenter.client.vo.UcenterOrgVo;
import com.wisely.ucenter.common.UcenterConstants;
import com.wisely.ucenter.entity.*;
import com.wisely.ucenter.mapper.*;
import com.wisely.ucenter.service.MenuService;
import com.wisely.ucenter.service.UcenterPersonInfoService;
import com.wisely.ucenter.service.UcenterUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 人员(TUcenterPerson)表服务实现类
 *
 * @author xintao.li
 * @since 2021-05-31 10:00:05
 */
@Service
public class UcenterUserServiceImpl implements UcenterUserService, UcenterConstants, SsoConstants {

    @Resource
    UcenterUserMapper ucenterUserMapper;
    @Resource
    UcenterPersonMapper ucenterPersonMapper;
    @Resource
    UcenterPersonRoleMapper ucenterPersonRoleMapper;
    @Resource
    UcenterRoleMapper ucenterRoleMapper;
    @Resource
    UcenterPersonOrgMapper ucenterPersonOrgMapper;


    @Resource
    MenuService menuService;
    @Resource
    UcenterPersonInfoService ucenterPersonInfoService;

    @Resource
    SysNetApi sysNetApi;
    @Resource
    UserCache userCache;


    @Override
    public List<UcenterUser> list(UcenterUser query) {
        query.setIsDeleted(0);
        return ucenterUserMapper.selectListBySelective(query);
    }

    @Override
    public PageInfo findByPage(UcenterUser query, PageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        query.setIsDeleted(0);
        return new PageInfo(ucenterUserMapper.selectListBySelective(query));
    }

    @Override
    public int save(UcenterUser record) {

        if (ValidHelper.isNull(record.getId())) {
            //添加账号提前验证账号是否重复
            UcenterUser user = this.loadByAccount(record.getAccount());
            AssertHelper.EX_VALIDATION.isEmpty(user, "save.entity_exists_account");

            if (ValidHelper.isNull(record.getStatus())) {
                record.setStatus(1);
            }
            record.setSalt(RandomHelper.uuid(7));
            record.setPassword(Md5Helper.encryptPwd(record.getAccount(), record.getPassword(), record.getSalt()));
            record.setIsErrorTime(0);
            record.setCreateBy(UserHelper.getUserId());
            record.setCreateTime(DateHelper.formatNow());
            record.setIsDeleted(0);
            ucenterUserMapper.insertSelective(record);
        } else {
            //修改账号重复校验
            UcenterUser ucenterUser = ucenterUserMapper.selectByPrimaryKey(record.getId());
            if (!ValidHelper.isEquals(ucenterUser.getAccount(), record.getAccount())) {
                UcenterUser user = this.loadByAccount(record.getAccount());
                AssertHelper.EX_VALIDATION.isEmpty(user, "save.entity_exists.account");
            }
            record.setPassword(Md5Helper.encryptPwd(record.getAccount(), record.getPassword(), ucenterUser.getSalt()));
            record.setUpdateBy(UserHelper.getUserId());
            record.setUpdateTime(DateHelper.formatNow());
            ucenterUserMapper.updateByPrimaryKeySelective(record);
        }

        // 刷新缓存
        userCache.syncCache(record);
        return record.getId();
    }

    @Override
    public UcenterUser load(Integer id) {
        return ucenterUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Integer id) {
        UcenterUser ucenterUser = this.load(id);
        ucenterUser.setIsDeleted(1);
        ucenterUser.setUpdateBy(UserHelper.getUserId());
        ucenterUser.setUpdateTime(DateHelper.formatNow());
        return ucenterUserMapper.updateByPrimaryKeySelective(ucenterUser);
    }

    @Override
    public UcenterUser loadByPersonId(Integer personId) {
        UcenterUser ucenterUser = new UcenterUser();
        ucenterUser.setIsDeleted(0);
        ucenterUser.setPersonId(personId);
        List<UcenterUser> ucenterUserList = ucenterUserMapper.selectListBySelective(ucenterUser);
        if (ValidHelper.isEmpty(ucenterUserList)) {
            return null;
        }
        return ucenterUserList.get(0);
    }

    @Override
    public void updateByPrimaryKeySelective(UcenterUser ucenterUser) {
        ucenterUserMapper.updateByPrimaryKeySelective(ucenterUser);
    }

    @Override
    public UcenterUser loadByAccount(String account) {
        UcenterUser user = new UcenterUser();
        user.setIsDeleted(0);
        user.setAccount(account);
        List<UcenterUser> ucenterUserList = ucenterUserMapper.selectListBySelective(user);
        if (ValidHelper.isEmpty(ucenterUserList)) {
            return null;
        }
        return ucenterUserList.get(0);
    }

    @Override
    public void updateBySelective(UcenterUser ucenterUser) {
        ucenterUserMapper.updateBySelective(ucenterUser);
    }


    /**
     * 通过ticket获取ssoUser 并且将ssoUser写入redis
     *
     * @return
     */
    @Override
    public SsoUser loadSsoUser() {

        Model input = RequestHelper.getInput();
        AssertHelper.EX_VALIDATION.isNotEmpty(input, "personId", "common.parameter_required.personId");


        Integer personId = input.getInt("personId");
        SsoUser ssoUser = new SsoUser();
        // 用户基本信息
        UcenterUser user = this.loadByPersonId(personId);
        if (ValidHelper.isNotEmpty(user)) {
            ssoUser.setId(user.getId());
            ssoUser.setAccount(user.getAccount());
            ssoUser.setFirstLogin(user.getFirstLogin());
        }

        // 人员基本信息
        UcenterPerson person = ucenterPersonMapper.selectByPrimaryKey(personId);
        if (ValidHelper.isNotEmpty(person)) {
            ssoUser.setOrgId(person.getOrgId());
            ssoUser.setOrgName(person.getOrgName());
            ssoUser.setPersonId(personId);
            ssoUser.setJobNo(person.getJobNo());
            ssoUser.setIdNo(person.getIdNo());
            ssoUser.setPersonName(person.getName());
            ssoUser.setSex(person.getSex());
            ssoUser.setBirthday(person.getBirthday());
            ssoUser.setMobile(person.getMobile());

            //头像信息从Sys获取
            List<Model> modelList =
                    sysNetApi.loadFiles(
                            Model.builder()
                                    .set("sourceType", FILE_IMAGE_CODE)
                                    .set("sourceId", personId));
            if (ValidHelper.isNotEmpty(modelList)) {
                ssoUser.setImgPath(modelList.get(0).getString("filePath"));
            }

            // 部门
            UcenterPersonOrg personOrgQuery = new UcenterPersonOrg();
            personOrgQuery.setIsDeleted(0);
            personOrgQuery.setPersonId(personId);
            personOrgQuery.setOrderBy("isMainDesc"); // 主部门在最前
            List<UcenterPersonOrg> deptList = ucenterPersonOrgMapper.selectListBySelective(personOrgQuery);
            if (ValidHelper.isNotEmpty(deptList)) {

                // 设置部门信息
                ssoUser.setDeptInfo(DataHelper.getModelList(deptList));

                Set<Integer> orgIdSet = Sets.newLinkedHashSet();
                Set<Integer> deptIdSet = Sets.newLinkedHashSet();
                //设置所有部门
                deptList.stream().forEach(x -> {
                    orgIdSet.add(x.getRootOrgId());
                    deptIdSet.add(x.getDeptId());
                    //设置主部门
                    if (ValidHelper.isEquals(x.getIsMain(), 1)) {
                        ssoUser.setMainDeptId(x.getDeptId());
                        UcenterOrgVo deptVo = UcDictHelper.loadOrgVo(x.getDeptId());
                        if (ValidHelper.isNotEmpty(deptVo)) {
                            ssoUser.setMainDeptName(deptVo.getCname());
                        }
                    }
                });
                ssoUser.setOrganizations(StringHelper.join(orgIdSet, ",")); // 机构
                ssoUser.setDepartments(StringHelper.join(deptIdSet, ",")); // 部门
            }

            // 角色
            UcenterRole queryRole = new UcenterRole();
            queryRole.setIsDeleted(0);
            List<UcenterRole> ucenterRoles = ucenterRoleMapper.selectListBySelective(queryRole);
            Model rolesModel = Model.builder();
            ucenterRoles.stream().forEach(d -> rolesModel.set(DataHelper.getString(d.getId()), Model.parseObject(d)));

            UcenterPersonRole roleQuery = new UcenterPersonRole();
            roleQuery.setPersonId(personId);
            roleQuery.setIsDeleted(0);
            List<UcenterPersonRole> roles = ucenterPersonRoleMapper.selectListBySelective(roleQuery);
            if (ValidHelper.isNotEmpty(roles)) {
                roles.stream().forEach(x -> {
                    Model role = rolesModel.getModel(DataHelper.getString(x.getRoleId()));
                    if (ValidHelper.isNotEmpty(role)) {
                        ssoUser.getRoleCodes().add(role.getString("code"));
                    }
                });
            }
        }

        // 权限
        Model model = Model.builder();
        if (!ssoUser.getRoleCodes().contains(ROLE_SUPER_ADMIN)) {
            // 非管理员，查询指定权限
            model.set("personId", personId);
        }
        List<Model> functionList = menuService.loadFunctionByAuth(personId);
        if (ValidHelper.isNotEmpty(functionList)) {
            functionList.stream().forEach(x -> {
                if (ValidHelper.isNotEmpty(x)) {
                    ssoUser.getAuthCodes().add(x.getString("code"));
                }
            });
        }

        // 扩展属性及信息
        Model<Integer, Model> personInfoModel =
                ucenterPersonInfoService.personInfoQuery(DataHelper.getString(personId));
        ssoUser.getExtendedProperties().putAll(personInfoModel.get(personId));

        //保存SsoUser与ticket的映射关系
        return ssoUser;
    }

    @Override
    public int deleteByIdQuery(String idQueryIn) {

        AssertHelper.EX_VALIDATION.isNotBlank(idQueryIn, "common.parameter_required.idQueryIn");

        UcenterUser query = new UcenterUser();
        query.setIsDeleted(0);
        query.setIdQueryIn(idQueryIn);
        List<UcenterUser> userList = ucenterUserMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(userList)) {
            return -1;
        }

        UcenterUser ucenterUser = new UcenterUser();
        ucenterUser.setIdQueryIn(idQueryIn);
        ucenterUser.setIsDeleted(1);
        ucenterUser.setUpdateBy(UserHelper.getUserId());
        ucenterUser.setUpdateTime(DateHelper.formatNow());
        try {
            ucenterUserMapper.updateBySelective(ucenterUser);
        } finally {
            userCache.invalidate(userList.toArray(new UcenterUser[]{}));
        }

        return 0;
    }
}
