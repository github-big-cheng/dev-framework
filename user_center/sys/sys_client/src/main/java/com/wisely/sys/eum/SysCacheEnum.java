package com.wisely.sys.eum;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.handler.SysDictHelper;
import com.wisely.sys.vo.SysCodeVo;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Function;

@Getter
public enum SysCacheEnum implements SysConstants {

    /**
     * 默认的空返回，避免空指针
     */
    EMPTY(null, (keys) -> null),

    /**
     * SysCode.value --> SysCode.name
     */
    CODE_VALUE(CODE_MAPPER_KEY, (keys) -> {
        String localeStr = DEFAULT_LOCALE;
        if (ValidHelper.isSize(keys, 2) && StringHelper.isNotBlank(keys[1])) {
            localeStr = DataHelper.getString(keys[1]);
        }
        SysCodeVo codeVo = SysDictHelper.loadCodeVo(keys[0], localeStr);
        return ValidHelper.isNotEmpty(codeVo) ? codeVo.getName() : null;
    });


    SysCacheEnum(String mapper, Function<String[], String> valueFunction) {
        this.mapper = mapper;
        this.valueFunction = valueFunction;
    }

    private String mapper;

    private Function<String[], String> valueFunction;


    public String getMapper() {
        return mapper;
    }

    private final static Model<String, SysCacheEnum> MAPPER_MODEL = Model.builder();

    static {
        Arrays.stream(values()).forEach(x -> {
            MAPPER_MODEL.set(x.getMapper(), x);
        });
    }

    public static Model getMapperModel() {
        return MAPPER_MODEL;
    }

    public static SysCacheEnum loadByMapper(String mapper) {
        return MAPPER_MODEL.containsKey(mapper) ? MAPPER_MODEL.get(mapper) : EMPTY;
    }

    public String loadValue(String... keys) {
        return this.getValueFunction().apply(keys);
    }
}
