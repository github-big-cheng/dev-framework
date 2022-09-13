package com.wisely.ucenter.client.api;

import com.wisely.framework.api.NetApi;
import com.wisely.framework.api.NetTools;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.eum.UcenterCacheEnum;
import com.wisely.ucenter.client.handler.UcenterDictionaryHelper;
import com.wisely.ucenter.client.vo.UcenterPersonVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class UcenterNetApi extends NetApi implements UcenterConstants {

    public UcenterNetApi(NetTools netTools) {
        super(netTools);
    }

    @Override
    protected String getName() {
        return "UcenterNetApi";
    }

    /**
     * 标准接口 - 检查返回状态
     * code == 0
     *
     * @param url
     * @param request
     * @param <T>
     * @return
     */
    private <T> T request(String url, Model request, TypeReference<T> typeReference) {
        Model modelResponse = this.doRequest(url, request);
        AssertHelper.EX_THIRD_PARTY.isEquals(modelResponse.getInt(ResponseBuilder.KEY_CODE, -1), 0,
                modelResponse.getString(ResponseBuilder.KEY_MESSAGE, "common.response_failed.{0}.{1}"),
                url,
                modelResponse);
        return JsonHelper.cast(modelResponse.get(ResponseBuilder.KEY_DATA), typeReference);
    }


    /************缓存操作方法开始************/
    /**
     * 获取ID获取人员信息
     *
     * @param personId 人员ID
     * @return UcenterPersonVo
     */
    public UcenterPersonVo loadUcenterPerson(Integer personId) {
        AssertHelper.EX_VALIDATION.isNotEmpty(personId, "common.parameter_required.personId");
        return EntityCacheManager.deserializer(
                PERSON_CACHE_KEY,
                DataHelper.getString(personId),
                UcenterPersonVo.class);
    }

    /************缓存操作方法结束************/


    /**
     * 通过personId获取SsoUser
     *
     * @return
     */
    public Model loadSsoUser(Integer personId) {
        return this.request(LOAD_SSOUSER, Model.builder().set("personId", personId), new TypeReference<Model>() {
        });
    }


    /**
     * 按角色查询人员列表
     *
     * @param model orgType org类型 部门-'12001-10'
     *              orgIdQueryIn 机构/部门ID
     *              roleCodeQueryIn 多角色code
     * @return
     */
    public List<Model> loadPersonBySelective(Model model) {
        return this.request(SELECTIVE_PERSON, model, new TypeReference<List<Model>>() {
        });
    }

    /**
     * 获取部门人员信息
     * 包含部门排序
     *
     * @param model type: 12001-10、12001-20
     *              personIds: 人员ids
     * @return
     */
    public List<Model> deptPersonList(Model model) {
        Model result = this.request(DEPT_PERSON, model, new TypeReference<Model>() {
        });
        return result.getModelList("list");
    }

    /**
     * 获取部门List
     *
     * @param input
     * @return
     */
    public List<Model> orgList(Model input) {
        return this.request(ORG_LIST, input, new TypeReference<List<Model>>() {
        });
    }

    /**
     * 获取部门树
     *
     * @param model ids 批量部门id
     * @return
     */
    public List<Model> loadDeptTree(Model model) {
        return this.request(DEPT_TREE, model, new TypeReference<List<Model>>() {
        });
    }

    /**
     * 获取部门人员树
     *
     * @param model personIdsQuery 批量人员id
     * @param model ids 批量部门ID
     * @return
     */
    public List<Model> loadDeptPersonTree(Model model) {
        return this.request(DEPT_PERSON_TREE, model, new TypeReference<List<Model>>() {
        });
    }

    /**
     * 获取角色
     *
     * @return
     */
    public List<Model> roleList(Model input) {
        return this.request(ROLE_LIST, input, new TypeReference<List<Model>>() {
        });
    }

    /**
     * 人员列表查询
     *
     * @param input
     * @return
     */
    public List<Model> loadPersonList(Model input) {
        return this.request(PERSON_LIST, input, new TypeReference<List<Model>>() {
        });
    }

}
