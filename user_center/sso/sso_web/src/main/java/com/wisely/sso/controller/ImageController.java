package com.wisely.sso.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.sso.common.VerifyImageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wisely.sso.client.SsoConstants.VERIFY_IMAGE_PREFIX;

@RestController
@Slf4j
@RequestMapping("/verifyImage")
public class ImageController {


    @Value("${verifyImage.file_path}")
    private String IMAGE_PATH;
    @Value("${verifyImage.file_suffix}")
    private String IMAGE_SUFFIX;

    /**
     * 获取图片信息
     *
     * @return
     */
    @RequestMapping("/generate")
    public Object generate() {
        try {
            //根据源图片和抠图形状生成新的图片信息以流的形式返回到前端
            Model imageModel = VerifyImageHelper.getVerifyImage(VerifyImageHelper.getRandomImage(IMAGE_PATH, file -> file.isFile() && file.getName().endsWith(IMAGE_SUFFIX)));
            //将剪裁好了以后的图片信息以当前时间戳为key，存入redis，时长2分钟
            RedisHelper.set(VERIFY_IMAGE_PREFIX + RequestHelper.getSessionId(), imageModel.getString(VerifyImageHelper.LOCATION_X_KEY), 1000 * 60 * 2);

            //移出随机生成的抠图位置坐标，不返回给前端
            imageModel.remove(VerifyImageHelper.LOCATION_X_KEY);

            return ResponseBuilder.buildSuccess(imageModel);
        } catch (Exception e) {
            log.error("image generate failed:{}", e);
            throw SystemException.builder(e, "image generate failed...");
        }
    }

}
