package com.wisely.file.api.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.io.Files;
import com.wisely.file.api.FileApi;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.framework.helper.encry.DESHelper;
import com.wisely.framework.plugins.file.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基于fastdfs的文件处理类
 */
public class FdfsFileApi implements FileApi {


    @Resource
    private FastFileStorageClient storageClient;

    private String groupName;

    private String seed;

    @Autowired
    public FdfsFileApi(FileProperties fileProperties) {
        this.groupName = fileProperties.getFastdfs().getGroupName();
        this.seed = fileProperties.getEncryptKey();
    }


    @Override
    public Model upload(String groupName, MultipartFile file) {

        String fileExtension = Files.getFileExtension(file.getName());

        StorePath storePath;
        try {
            storePath = storageClient.uploadFile(this.groupName, file.getInputStream(), file.getSize(), fileExtension);
        } catch (IOException e) {
            throw SystemException.builder(e, "file.upload_failed");
        }

        return Model.builder().set(FILE_SIZE, file.getSize())
                .set(FILE_DIR, StringHelper.substring(storePath.getFullPath(), 0, storePath.getFullPath().lastIndexOf(File.separator)))
                .set(FILE_NAME, file.getOriginalFilename())
                .set(FILE_PATH, storePath.getPath())
                .set(FILE_REAL_PATH, storePath.getFullPath())
                .set(FILE_TYPE, fileExtension);
    }

    @Override
    public Boolean delete(String path) {
        storageClient.deleteFile(this.groupName, path);
        return true;
    }

    @Override
    public Model download(Integer isEncry, String path) {

        if (ValidHelper.isEquals(isEncry, 1)) {
            AssertHelper.EX_SYSTEM.isNotBlank(this.seed, "file.seed_not_config");
        }

        String fileName = StringHelper.substring(path, path.lastIndexOf(File.separator));
        byte[] bytes = storageClient.downloadFile(this.groupName, path, new DownloadByteArray());

        InputStream inputStream;
        try {
            inputStream =
                    ValidHelper.isEquals(isEncry, 1) ?
                            new CipherInputStream(new ByteArrayInputStream(bytes), DESHelper.getCipher("DES", Cipher.ENCRYPT_MODE, this.seed))
                            : new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            throw SystemException.builder(e, "file.file_download_failed");
        }

        return Model.builder().set("fileName", fileName).set("inputStream", inputStream);
    }
}
