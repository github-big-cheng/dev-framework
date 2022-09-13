package com.wisely.file.controller;

import com.wisely.file.api.FileApi;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.crypto.CipherInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


@RequestMapping("/file")
@Slf4j
public class FileController {


    @Resource
    FileApi fileApi;


    /**
     * 文件上传接口
     * 详细参数请见 converter/file/upload.yml
     *
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Object upload() {

        Model input = RequestHelper.getInput();
        List<MultipartFile> files = input.getList("file");

        AssertHelper.EX_VALIDATION.isNotBlank(input, "groupName", "common.parameter_required.groupName");
        AssertHelper.EX_VALIDATION.isNotEmpty(files, "common.parameter_required.file");

        String groupName = input.getString("groupName");

        List<Model> rst = Lists.newArrayList();
        files.forEach(file -> {
            rst.add(fileApi.upload(groupName, file));
        });
        return ResponseBuilder.buildSuccess(rst);
    }


    /**
     * 图片删除接口
     *
     * @param path
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String path) {

        // 为安全起见，暂不支持附件删除
        AssertHelper.EX_SYSTEM.isTrue(false, "system.operation_not_allowed_at_moment");

        AssertHelper.EX_VALIDATION.isNotBlank(path, "common.parameter_required.path");

        fileApi.delete(path);

        return ResponseBuilder.buildSuccess();
    }


    @RequestMapping("/download")
    public void download() {

        Model input = RequestHelper.getInput();
        String path = input.getString("filePath");
        AssertHelper.EX_VALIDATION.isNotBlank(path, "common.parameter_required", "filePath");

        HttpServletResponse response = RequestHelper.getResponse();
        // 设置返回头
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");

        Model fileInfo = fileApi.download(input.getInt("isEncry"), path);
        try (
                InputStream is = (InputStream) fileInfo.get("inputStream");
                OutputStream os = response.getOutputStream()
        ) {
            log.debug("is instanceof CipherInputStream : {}", (is instanceof CipherInputStream));

            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = fileInfo.getString("fileName");
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) > 0) {
                os.write(bytes, 0, len);
            }

            os.flush();

        } catch (Exception e) {
            throw SystemException.builder(e, "file_download.file_download_failed");
        }

    }


}
