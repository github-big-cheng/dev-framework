package com.wisely.framework.handler.regular;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.RegexHelper;
import com.wisely.framework.helper.StringHelper;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RegularTypeContext {

    /**
     * 默认初始化正则
     */
    final static Model<String, Pattern> INIT_PATTERNS =
            Model.builder()
                    .set("letter", RegexHelper.onlyLetterRegex)
                    .set("number", RegexHelper.numberRegex)
                    .set("money", RegexHelper.moneyRegex)
                    .set("url", RegexHelper.urlRegex)
                    .set("tyshxydm", RegexHelper.tyshxydmRegex)
                    .set("idCard", RegexHelper.idCardRegex)
                    .set("postCode", RegexHelper.postCodeRegex)
                    .set("areaNo", RegexHelper.areaNoRegex)
                    .set("longitude", RegexHelper.longitudeRegex)
                    .set("latitude", RegexHelper.latitudeRegex);

    public RegularTypeContext(Map<String, Pattern> patterns) {
        this.patternModel = Model.builder(INIT_PATTERNS);
        this.patternModel.putAll(patterns);
    }


    private Model<String, Pattern> patternModel;

    /**
     * 根据预定义的类型校验
     *
     * @param types
     * @param str
     * @return
     */
    public boolean matchesByTypes(String types, String str) {

        Stream<String> typeStream = StringHelper.splitToStream(types, ",");

        boolean result = typeStream.anyMatch(type -> {
            Pattern pattern = this.patternModel.get(type);
            return pattern != null && pattern.matcher(str).find();
        });

        return result;
    }

}
