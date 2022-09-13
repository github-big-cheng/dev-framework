package com.wisely.sys.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wisely.framework.api.NetApi;
import com.wisely.framework.api.NetTools;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.handler.SysDictionaryHelper;
import com.wisely.sys.vo.SysCodeVo;
import com.wisely.sys.vo.SysParameterVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class SysNetApi extends NetApi implements SysConstants {

    public SysNetApi(NetTools netTools) {
        super(netTools);
    }

    @Override
    protected String getName() {
        return "SysNetApi";
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

    /**
     * 添加资源
     *
     * @return
     */
    public Model uploadFile(Model model) {
        return this.doRequest(UPLOAD_FILE, model);
    }


    /**
     * 获取附件
     *
     * @param input
     * @return
     */
    public List<Model> loadFiles(Model input) {
        AssertHelper.EX_VALIDATION.isNotBlank(input, "sourceType", "common.parameter_required.fileType");
        AssertHelper.EX_VALIDATION.isNotEmpty(input, "sourceId", "common.parameter_required.sourceId");
        return this.request(FIND_FILES, input, new TypeReference<List<Model>>() {
        });
    }

    /**
     * 删除附件
     *
     * @param input
     * @return
     */
    public Model deleteFile(Model input) {
        AssertHelper.EX_VALIDATION.isNotBlank(input, "sourceType", "common.parameter_required.fileType");
        AssertHelper.EX_VALIDATION.isNotEmpty(input, "sourceIdQueryIn", "common.parameter_required.sourceId");
        return this.doRequest(DELETE_FILE, input);
    }


    /**
     * 获取菜单列表
     *
     * @param input
     * @return
     */
    public List<Model> functionList(Model input) {
        Model params = Model.builder(input).set("pageSize", Integer.MAX_VALUE);
        Model response = this.request(FUNCTION_FILE, params, new TypeReference<Model>() {
        });
        return response.getModelList("list");
    }


    /**
     * 获取项目列表
     *
     * @param input
     * @return
     */
    public List<Model> projectList(Model input) {
        Model params = Model.builder(input).set("pageSize", Integer.MAX_VALUE);
        Model response = this.request(PROJECT_LIST, params, new TypeReference<Model>() {
        });
        return response.getModelList("list");
    }


    // ============================================= 缓存处理 ============================================================

    /**
     * 获取系统参数
     *
     * @param orgId
     * @param keies
     * @return
     */
    public SysParameterVo loadParameter(Integer orgId, String keies) {

        if (ValidHelper.isNull(orgId) || ValidHelper.isBlank(keies)) {
            return null;
        }

        return EntityCacheManager.deserializer(
                PARAMETER_CACHE_KEY + orgId,
                keies,
                SysParameterVo.class);
    }


    /**
     * 获取code名称
     *
     * @param value
     * @return
     */
    public SysCodeVo loadSysCode(String value) {
        return SysDictionaryHelper.loadCodeVo(value);
    }

    /**
     * 获取code对应中文
     *
     * @param value
     * @return
     */
    public String loadSysCodeName(String value) {
        SysCodeVo codeVo = this.loadSysCode(value);
        return ValidHelper.isEmpty(codeVo) ? "" : codeVo.getName();
    }
}
