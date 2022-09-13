package com.wisely.file.api;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileApi {


    /**
     * 文件名称
     */
    String FILE_NAME = "fileName";
    /**
     * 文件保存目录
     */
    String FILE_DIR = "fileDir";
    /**
     * 文件url路径
     */
    String FILE_PATH = "filePath";
    /**
     * 文件全路径
     */
    String FILE_REAL_PATH = "fileRealPath";
    /**
     * 文件大小
     */
    String FILE_SIZE = "fileSize";
    /**
     * 文件类型
     */
    String FILE_TYPE = "fileType";

    /**
     * 默认分组
     */
    String DEFAULT_GROUP_NAME = "DEFAULT";

    /**
     * 文件上传
     *
     * @param groupName 分组名称/业务类型
     * @param file      上传的文件
     * @return
     */
    Model upload(String groupName, MultipartFile file);


    /**
     * 删除文件
     *
     * @param path 文件相对路径
     * @return
     */
    Boolean delete(String path);


    /**
     * 文件下载
     *
     * @param isEncry 是否加密 1-是 0-否
     * @param path    文件相对路径
     * @return fileName 文件名称
     * inputStream inputStream
     */
    Model download(Integer isEncry, String path);


    /**
     * 构建文件保存路径
     * rootPath/groupName/yyyy/yyyyMM
     * ex: /home/dounion/file/groupName/2020/202011
     *
     * @param groupName
     * @return
     */
    default String pathGenerate(String groupName) {
        groupName = ValidHelper.isNotBlank(groupName) ? groupName : DEFAULT_GROUP_NAME;
        return File.separator + DateHelper.formatNow("yyyy")
                + File.separator + DateHelper.formatNow("yyyyMM")
                + File.separator + groupName
                ;
    }
}
