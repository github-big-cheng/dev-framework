package com.wisely.ucenter.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.*;
import com.wisely.framework.helper.encry.Md5Helper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.api.SysNetApi;
import com.wisely.ucenter.caches.PersonCache;
import com.wisely.ucenter.common.UcenterConstants;
import com.wisely.ucenter.entity.*;
import com.wisely.ucenter.mapper.UcenterOrgMapper;
import com.wisely.ucenter.mapper.UcenterPersonMapper;
import com.wisely.ucenter.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wisely.sso.client.SsoConstants.ROLE_SUPER_ADMIN;

/**
 * 人员(TUcenterPerson)表服务实现类
 *
 * @author xintao.li
 * @since 2021-05-28 18:05:10
 */
@Service
public class UcenterPersonServiceImpl implements UcenterPersonService, UcenterConstants {

    private final static String INIT_PASSWORD = "123456";

    @Resource
    UcenterPersonMapper ucenterPersonMapper;
    @Resource
    UcenterOrgMapper ucenterOrgMapper;

    @Resource
    UcenterUserService ucenterUserService;
    @Resource
    UcenterPersonInfoService ucenterPersonInfoService;
    @Resource
    UcenterPersonRoleService ucenterPersonRoleService;
    @Resource
    UcenterPersonOrgService ucenterPersonOrgService;

    @Resource
    SysNetApi sysNetApi;
    @Lazy
    @Resource
    PersonCache personCache;


    @Override
    public Model findByPage(UcenterPerson query, PageVo pageVo) {
        query.setIsDeleted(0);
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            query.setOrgIdQueryIn(UserHelper.getOrganizations());
        }

        // 级联查询
        Model model = RequestHelper.getInput();
        if (model.isNotBlank("pathIds")) {
            UcenterOrg orgQuery = new UcenterOrg();
            orgQuery.setPathIdsQueryLike(model.getString("pathIds"));
            orgQuery.setIsDeleted(0);
            List<UcenterOrg> ucenterOrgList = ucenterOrgMapper.selectListBySelective(orgQuery);
            if (ValidHelper.isNotEmpty(ucenterOrgList)) {
                String ucenterIds =
                        ucenterOrgList.stream()
                                .map(org -> DataHelper.getString(org.getId()))
                                .collect(Collectors.joining(","));
                query.setDeptIdQueryIn(ucenterIds);
            }
        }

        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        query.setIsDeleted(0);
        List<UcenterPerson> dataList = ucenterPersonMapper.selectListBySelective(query);
        PageInfo pageInfo = new PageInfo(dataList);
        return Model.builder().set("total", pageInfo.getTotal()).set("list", this.selectPersonWithDetail(dataList));
    }

    /**
     * 查询人员信息以及人员对应的角色信息
     *
     * @param dataList
     * @return List<Model>
     */
    @Override
    public List<Model> selectPersonWithDetail(List<UcenterPerson> dataList) {

        List<Model> personList = Lists.newArrayList();
        if (ValidHelper.isEmpty(dataList)) {
            return personList;
        }

        //人员id
        List<Integer> personIdList = dataList.stream().map(UcenterPerson::getId).collect(Collectors.toList());
        String personIds = StringHelper.join(personIdList, ",");

        // personInfo信息
        Model<Integer, Model> personInfoModel = ucenterPersonInfoService.personInfoQuery(personIds);


        // 账号信息
        Model<Integer, Model> userModel = Model.builder();
        UcenterUser userQuery = new UcenterUser();
        userQuery.setIsDeleted(0);
        userQuery.setPersonIdQueryIn(personIds);
        List<UcenterUser> userList = ucenterUserService.list(userQuery);
        if (ValidHelper.isNotEmpty(userList)) {
            userList.forEach(user -> {
                Model item = Model.builder();
                item.set("userId", user.getId())
                        .set("account", user.getAccount())
                        .set("accountStatus", user.getStatus());
                userModel.set(user.getPersonId(), item);
            });
        }

        // 人员部门信息
        Model<Integer, List<UcenterPersonOrg>> personOrgModel = Model.builder();
        UcenterPersonOrg personOrgQuery = new UcenterPersonOrg();
        personOrgQuery.setIsDeleted(0);
        personOrgQuery.setPersonIdQueryIn(personIds);
        List<UcenterPersonOrg> personOrgList = ucenterPersonOrgService.list(personOrgQuery);
        if (ValidHelper.isNotEmpty(personOrgList)) {
            personOrgList.forEach(personOrg ->
                    personOrgModel.getList(personOrg.getPersonId(), true).add(personOrg));
        }

        //人员角色信息
        Model<Integer, Set<Integer>> personRoleModel = Model.builder();
        UcenterPersonRole personRoleQuery = new UcenterPersonRole();
        personRoleQuery.setIsDeleted(0);
        personRoleQuery.setPersonIdQueryIn(personIds);
        List<UcenterPersonRole> personRoleList = ucenterPersonRoleService.list(personRoleQuery);
        if (ValidHelper.isNotEmpty(personRoleList)) {
            personRoleList.forEach(personRole ->
                    personRoleModel.getSet(personRole.getPersonId(), true).add(personRole.getRoleId()));
        }

        // 调用SysNetApi 获取人员的头像信息
        Model<Integer, Model> imageModel = Model.builder();
        List<Model> fileList =
                sysNetApi.loadFiles(
                        Model.builder()
                                .set("sourceType", FILE_IMAGE_CODE)
                                .set("sourceIdQueryIn", personIds));
        if (ValidHelper.isNotEmpty(fileList)) {
            fileList.forEach(file -> imageModel.set(file.getInt("sourceId"), file));
        }


        dataList.forEach(person -> {

            Model temp = Model.parseObject(person);
            personList.add(temp);

            // 账号信息
            temp.putAll(userModel.get(person.getId()));

            // personInfo 信息
            if (personInfoModel.containsKey(person.getId())) {
                temp.set("info", personInfoModel.get(person.getId()));
            }

            // 角色IDs
            temp.set("roleIds", StringHelper.join(personRoleModel.getSet(person.getId()), ","));

            // 人员部门信息
            List<UcenterPersonOrg> personOrgs = personOrgModel.getList(person.getId());
            if (ValidHelper.isNotEmpty(personOrgs)) {

                List<UcenterPersonOrg> personDeptList = Lists.newArrayList();
                Set<Integer> orgIdSet = Sets.newHashSet();
                Set<Integer> deptIdSet = Sets.newHashSet();
                personOrgs.forEach(personOrg -> {
                    orgIdSet.add(personOrg.getRootOrgId());
                    deptIdSet.add(personOrg.getDeptId());
                    personDeptList.add(personOrg);
                });

                temp.set("deptIds", StringHelper.join(deptIdSet, ","));
                temp.set("orgIds", StringHelper.join(orgIdSet, ","));
                temp.set("ucenterPersonOrgs", personDeptList);
            }

            // 头像
            temp.set("personImg", imageModel.get(person.getId()));
        });

        return personList;
    }


    @Transactional
    @Override
    public int save(UcenterPerson record) {

        Model input = RequestHelper.getInput();
        UcenterUser ucenterUser = null;

        if (ValidHelper.isNull(record.getId())) {

            // 添加T_UCENTER_PERSON数据
            record.setCreateBy(UserHelper.getUserId());
            record.setCreateTime(DateHelper.formatNow());
            record.setIsDeleted(0);
            ucenterPersonMapper.insertSelective(record);

        } else {
            record.setUpdateBy(UserHelper.getUserId());
            record.setUpdateTime(DateHelper.formatNow());
            ucenterPersonMapper.updateByPrimaryKeySelective(record);

            //查询原账号信息
            UcenterUser userQuery = new UcenterUser();
            userQuery.setPersonId(record.getId());
            userQuery.setIsDeleted(0);
            List<UcenterUser> userList = ucenterUserService.list(userQuery);
            if (ValidHelper.isNotEmpty(userList)) {
                ucenterUser = userList.get(0);
            }
        }


        // 添加T_UCENTER_PERSON_INFO数据
        ucenterPersonInfoService.save(record, input.getModel("info"));

        // 1.保存账号信息
        // 新增账号
        if (ValidHelper.isEmpty(ucenterUser) && ValidHelper.isNotBlank(record.getAccount())) {
            UcenterUser user = new UcenterUser();
            user.setOrgId(record.getOrgId());
            user.setOrgName(record.getOrgName());
            user.setPersonId(record.getId());
            user.setAccount(record.getAccount());
            user.setFirstLogin(1); // 首次登录
            user.setPassword(INIT_PASSWORD);
            user.setIsErrorTime(0);
            user.setStatus(0);
            ucenterUserService.save(user);
        } else if (ValidHelper.isNotEmpty(ucenterUser)
                && ValidHelper.isBlank(record.getAccount())) {
            // 删除账号的情况
            ucenterUserService.deleteByIdQuery(DataHelper.getString(record.getUserId()));
        }

        // 保存人员角色信息
        ucenterPersonRoleService.updatePersonRole(input.getString("roleIds"), record);
        // 保存人员部门信息
        List<Model> personOrgs = input.getModelList("ucenterPersonOrgs");
        ucenterPersonOrgService.updatePersonOrgByList(personOrgs, record);

        // 保存人员头像信息 处理人员头像(保存头像到SysFile表中)
        this.personImgSave(record);

        // 刷新缓存
        UcenterPerson cacheQuery = new UcenterPerson();
        cacheQuery.setId(record.getId());
        List<UcenterPerson> dataList = ucenterPersonMapper.selectListBySelective(cacheQuery);
        List<Model> cacheList = this.selectPersonWithDetail(dataList);
        if (ValidHelper.isNotEmpty(cacheList)) {
            personCache.syncCache(cacheList.get(0));
        }
        return record.getId();
    }


    @Override
    public Model load(Integer id) {
        UcenterPerson query = new UcenterPerson();
        query.setId(id);
        query.setIsDeleted(0);
        List<UcenterPerson> dataList = ucenterPersonMapper.selectListBySelective(query);
        List<Model> personList = this.selectPersonWithDetail(dataList);
        return ValidHelper.isEmpty(personList) ? null : personList.get(0);
    }


    @Transactional
    @Override
    public int delete(String idQueryIn) {
        AssertHelper.EX_VALIDATION.isNotBlank(idQueryIn, "common.parameter_required.idQueryIn");
        List<String> personIds = Splitter.on(",").splitToList(idQueryIn);

        personIds.forEach(personIdStr -> {

            Integer personId = DataHelper.getInt(personIdStr);

            //1.删除UcenterUser
            UcenterUser user = ucenterUserService.loadByPersonId(DataHelper.getInt(personId));
            if (ValidHelper.isNotEmpty(user)) {
                user.setIsDeleted(1);
                user.setUpdateBy(UserHelper.getUserId());
                user.setUpdateTime(DateHelper.formatNow());
                ucenterUserService.updateByPrimaryKeySelective(user);
            }

            //2.删除UcenterPerson
            UcenterPerson ucenterPerson = new UcenterPerson();
            ucenterPerson.setId(personId);
            ucenterPerson.setIsDeleted(1);
            ucenterPerson.setUpdateBy(UserHelper.getUserId());
            ucenterPerson.setUpdateTime(DateHelper.formatNow());
            ucenterPersonMapper.updateByPrimaryKeySelective(ucenterPerson);

            //3.删除UcenterPersonInfo
            ucenterPersonInfoService.save(ucenterPerson, null);

            //4.删除人员角色信息
            ucenterPersonRoleService.deleteByPersonId(personId);

            //5.删除人员机构信息
            ucenterPersonOrgService.deleteByPersonId(personId);
        });
        return 1;
    }

    @Override
    public void resetPwd(String account) {

        AssertHelper.EX_VALIDATION.isNotBlank(account, "common.parameter_required.account");

        UcenterUser record = ucenterUserService.loadByAccount(account);
        AssertHelper.EX_BUSINESS.isNotEmpty(record, "person_resetpwd.user_not_found");

        record.setIsErrorTime(0);
        record.setFirstLogin(1); // 设置首次登录标识
        record.setPassword(Md5Helper.encryptPwd(record.getAccount(), INIT_PASSWORD, record.getSalt()));
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        ucenterUserService.updateByPrimaryKeySelective(record);
    }


    /**
     * 账号操作
     * 锁定/解锁
     *
     * @param accounts
     * @param accountStatus
     */
    @Transactional
    @Override
    public void accountOperation(String accounts, int accountStatus) {

        AssertHelper.EX_VALIDATION.isNotBlank(accounts, "common.parameter_required.accounts");

        UcenterUser ucenterUser = new UcenterUser();
        ucenterUser.setIsDeleted(0);
        ucenterUser.setAccountQueryIn(accounts);
        List<UcenterUser> userList = ucenterUserService.list(ucenterUser);
        if (ValidHelper.isEmpty(userList)) {
            return;
        }

        List<Integer> userIdList = Lists.newArrayList();
        List<Integer> personIdList = Lists.newArrayList();

        userList.forEach(user -> {
            userIdList.add(user.getId());
            personIdList.add(user.getPersonId());
        });

        UcenterUser userRecord = new UcenterUser();
        userRecord.setIdQueryIn(StringHelper.join(userIdList, ","));
        userRecord.setStatus(accountStatus);
        userRecord.setIsErrorTime(0);
        userRecord.setUpdateBy(UserHelper.getUserId());
        userRecord.setUpdateTime(DateHelper.formatNow());
        ucenterUserService.updateBySelective(userRecord);

        // 修改UcenterPerson的修改时间
        UcenterPerson personRecord = new UcenterPerson();
        personRecord.setIdQueryIn(StringHelper.join(personIdList, ","));
        personRecord.setUpdateBy(UserHelper.getUserId());
        personRecord.setUpdateTime(DateHelper.formatNow());
        ucenterPersonMapper.updateBySelective(personRecord);
    }


    @Override
    public void personImgSave(UcenterPerson record) {

        Model input = RequestHelper.getInput();
        if (input.isEmpty("personImg")) {
            return;
        }

        Model personImg = input.getModel("personImg");
        sysNetApi.uploadFile(
                Model.builder()
                        .set("sourceType", FILE_IMAGE_CODE)
                        .set("sourceId", record.getId())
                        .set("personId", UserHelper.getPersonId())
                        .set("uploadTime", DateHelper.formatNow())
                        .set("files", Lists.newArrayList(personImg)));
    }


    @Override
    public List<Model> loadPersonBySelective(UcenterPerson query) {
        query.setIsDeleted(0);
        List<UcenterPerson> personList = ucenterPersonMapper.selectListBySelective(query);
        return selectPersonWithDetail(personList);

    }


    @Override
    public void updatePassword(UcenterUser user, String newPassword) {
        user.setIsErrorTime(0);
        user.setPassword(Md5Helper.encryptPwd(user.getAccount(), newPassword, user.getSalt()));
        user.setUpdateBy(UserHelper.getUserId());
        user.setUpdateTime(DateHelper.formatNow());
        ucenterUserService.updateByPrimaryKeySelective(user);
    }

}
