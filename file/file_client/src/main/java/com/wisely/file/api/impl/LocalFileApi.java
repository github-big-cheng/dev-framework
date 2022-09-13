package com.wisely.file.api.impl;

import com.wisely.file.api.FileApi;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RandomHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.framework.helper.encry.DESHelper;
import com.wisely.framework.plugins.file.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 基于本地IO流的文件处理类
 */
@Slf4j
public class LocalFileApi implements FileApi {

    @Autowired
    public LocalFileApi(FileProperties fileProperties) {
        this.rootPath =
                StringHelper.endsWith(fileProperties.getLocal().getPath(), File.separator) ?
                        fileProperties.getLocal().getPath() : fileProperties.getLocal().getPath() + File.separator;
        this.seed = fileProperties.getEncryptKey();
    }


    /**
     * 文件保存根路径, ex: /home/dounion/file/
     */
    private String rootPath;

    private String seed;


    /**
     * 生成文件信息
     *
     * @param groupName 文件分组
     * @param file
     * @return
     */
    private Model fileInfoGenerate(String groupName, MultipartFile file) {

        String fileExtension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename()).toLowerCase();

        String fileDir = this.pathGenerate(groupName);
        String newFileName = RandomHelper.uuid() + "." + fileExtension;
        String filePath = fileDir + File.separator + newFileName;
        filePath = StringHelper.replace(filePath, File.separator, "/"); // 文件路径

        return Model.builder().set(FILE_SIZE, file.getSize())
                .set(FILE_DIR, rootPath + fileDir)
                .set(FILE_NAME, file.getOriginalFilename())
                .set(FILE_PATH, filePath)
                .set(FILE_REAL_PATH, rootPath + fileDir + File.separator + newFileName)
                .set(FILE_TYPE, fileExtension)
                ;
    }


    @Override
    public Model upload(String groupName, MultipartFile file) {

        // 加载文件信息
        Model fileInfo = this.fileInfoGenerate(groupName, file);
        log.debug("fileInfo:{}", fileInfo.toString());

        try {
            // 获取存储路径
            Path dirPath = Paths.get(fileInfo.getString(LocalFileApi.FILE_DIR));
            if (!dirPath.toFile().exists()) {
                java.nio.file.Files.createDirectories(dirPath);
            }

            // 创建文件
            java.nio.file.Files.write(Paths.get(fileInfo.getString(LocalFileApi.FILE_REAL_PATH)), file.getBytes());

            return fileInfo;
        } catch (IOException e) {
            throw SystemException.builder(e, "file.parent_dir_create_error");
        }
    }


    @Override
    public Boolean delete(String path) {

        Path filePath = Paths.get(path);
        if (!filePath.toFile().exists()) {
            throw SystemException.builder("delete.file_not_exists.{0}", path);
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw SystemException.builder("delete.file_delete_failed.{0}", path);
        }
        return true;
    }

    @Override
    public Model download(Integer isEncry, String path) {

        if (ValidHelper.isEquals(isEncry, 1)) {
            AssertHelper.EX_SYSTEM.isNotBlank(this.seed, "file.seed_not_config");
        }

        Path filePath = Paths.get(this.rootPath + path);
        AssertHelper.EX_VALIDATION.isTrue(filePath.toFile().exists(), "file_download.file_not_found");

        InputStream inputStream;
        try {
            inputStream =
                    ValidHelper.isEquals(isEncry, 1) ?
                            new CipherInputStream(Files.newInputStream(filePath), DESHelper.getCipher("DES", Cipher.ENCRYPT_MODE, this.seed))
                            : Files.newInputStream(filePath);
        } catch (Exception e) {
            throw SystemException.builder(e, "file.file_download_failed");
        }

        return Model.builder().set("fileName", filePath.getFileName()).set("inputStream", inputStream);
    }
}
