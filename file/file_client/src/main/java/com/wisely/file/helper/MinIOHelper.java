package com.wisely.file.helper;

import com.wisely.framework.exception.SystemException;
import io.minio.*;

import java.io.FileInputStream;
import java.io.InputStream;

public class MinIOHelper {

    /**
     * 获取MinIO客户端
     *
     * @param endPoint
     * @param account
     * @param password
     * @return
     */
    public static MinioClient builder(String endPoint, String account, String password) {
        return MinioClient.builder().endpoint(endPoint).credentials(account, password).build();
    }


    /**
     * 桶不存在时创建
     *
     * @param client
     * @param bucketName
     */
    public static void createBucketIfNotExists(MinioClient client, String bucketName) {
        try {
            if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw SystemException.builder(e, "MinioClient.createBucketIfNotExists failed!!!");
        }
    }
    
    /**
     * 通过流上传文件
     *
     * @param client
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     * @return
     */
    public static ObjectWriteResponse upload(MinioClient client, String bucketName, String objectName, InputStream inputStream) {
        try {
            return client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        } catch (Exception e) {
            throw SystemException.builder(e, "MinioClient.upload failed!!!");
        }
    }

    /**
     * 获取文件流
     *
     * @param client
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public static InputStream download(MinioClient client, String bucketName, String objectName) {
        try {
            return client.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());

        } catch (Exception e) {
            throw SystemException.builder(e, "MinioClient.download failed!!!");
        }
    }

    /**
     * 断点下载
     *
     * @param client
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return 流
     */
    public InputStream downloadWithOffset(MinioClient client, String bucketName, String objectName, long offset, long length) {
        try {
            return client.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
        } catch (Exception e) {
            throw SystemException.builder(e, "MinioClient.downloadWithOffset failed!!!");
        }
    }

    /**
     * 文件删除
     *
     * @param client
     * @param bucketName
     * @param objectName
     */
    public static void delete(MinioClient client, String bucketName, String objectName) {
        try {
            client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            throw SystemException.builder(e, "MinioClient.delete failed!!!");
        }
    }

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\Big_程\\Pictures\\Camera Roll\\388d16a0fab674ffd1b3637321e05a0c.jpg");

        MinioClient client = MinIOHelper.builder("http://47.93.42.175:8051/", "admin", "12345678");
        ObjectWriteResponse response = upload(client, "default", "2022/09/123.jpg", fileInputStream);
        System.out.println(response);
    }
}
