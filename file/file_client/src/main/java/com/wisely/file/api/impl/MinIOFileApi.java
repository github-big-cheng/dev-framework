package com.wisely.file.api.impl;

import com.wisely.file.api.FileApi;
import com.wisely.file.helper.MinIOHelper;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RandomHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.framework.helper.encry.DESHelper;
import com.wisely.framework.plugins.file.FileProperties;
import io.minio.MinioClient;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MinIOFileApi implements FileApi {

    public MinIOFileApi(FileProperties fileProperties) {
        FileProperties.MinIOConfig minIOConfig = fileProperties.getMinio();
        this.minioClient =
                MinIOHelper.builder(minIOConfig.getEndpoint(), minIOConfig.getAccount(), minIOConfig.getPassword());
        this.bucketName = minIOConfig.getBucketName();
        MinIOHelper.createBucketIfNotExists(minioClient, this.bucketName);

        this.separator = minIOConfig.getSeparator();
        this.seed = fileProperties.getEncryptKey();
    }


    MinioClient minioClient;

    String bucketName;

    String separator = "/";

    String seed;

    @Override
    public Model upload(String bizType, MultipartFile file) {

        Model fileIno = this.fileInfoGenerate(bizType, file);
        try {
            MinIOHelper.upload(minioClient, bucketName, fileIno.getString(FILE_REAL_PATH), file.getInputStream());
        } catch (IOException e) {
            throw SystemException.builder(e, "MinIOFileApi.upload failed!!!");
        }
        return fileIno;
    }


    private Model fileInfoGenerate(String groupName, MultipartFile file) {
        String fileExtension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename()).toLowerCase();

        String fileDir = pathGenerate(groupName);
        fileDir = StringHelper.replace(fileDir, File.separator, separator);
        String newFileName = RandomHelper.uuid() + "." + fileExtension;
        String filePath = fileDir + separator + newFileName;

        return Model.builder().set(FILE_SIZE, file.getSize())
                .set(FILE_DIR, fileDir)
                .set(FILE_NAME, file.getOriginalFilename())
                .set(FILE_PATH, filePath)
                .set(FILE_REAL_PATH, filePath)
                .set(FILE_TYPE, fileExtension)
                ;
    }


    @Override
    public Boolean delete(String path) {
        MinIOHelper.delete(minioClient, bucketName, path);
        return true;
    }

    @Override
    public Model download(Integer isEncry, String path) {
        if (ValidHelper.isEquals(isEncry, 1)) {
            AssertHelper.EX_SYSTEM.isNotBlank(this.seed, "file.seed_not_config");
        }

        String fileName = StringHelper.substring(path, path.lastIndexOf(separator));

        InputStream inputStream;
        try {
            inputStream = MinIOHelper.download(minioClient, bucketName, path);

            inputStream =
                    ValidHelper.isEquals(isEncry, 1) ?
                            new CipherInputStream(inputStream, DESHelper.getCipher("DES", Cipher.ENCRYPT_MODE, this.seed))
                            : inputStream;
        } catch (Exception e) {
            throw SystemException.builder(e, "file.file_download_failed");
        }
        return Model.builder().set("fileName", fileName).set("inputStream", inputStream);
    }
}
