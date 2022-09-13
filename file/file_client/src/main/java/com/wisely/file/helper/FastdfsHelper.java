package com.wisely.file.helper;

import com.wisely.framework.helper.SpringHelper;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Slf4j
public class FastdfsHelper {


    /**
     * 文件上传
     * 最后返回fastDFS中的文件名称;group1/M00/01/04/CgMKrVvS0geAQ0pzAACAAJxmBeM793.doc
     *
     * @param bytes     文件字节
     * @param fileSize  文件大小
     * @param extension 文件扩展名
     * @return fastDfs路径
     */
    public static String uploadFile(byte[] bytes, long fileSize, String extension) {
        FastFileStorageClient fastFileStorageClient = SpringHelper.getBean(FastFileStorageClient.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = fastFileStorageClient.uploadFile(byteArrayInputStream, fileSize, extension, null);
        log.debug("{}=={}======{}", storePath.getGroup(), storePath.getPath(), storePath.getFullPath());
        return storePath.getFullPath();
    }

    /**
     * 下载文件
     * 返回文件字节流
     *
     * @param group 分组
     * @param path 文件相对路径
     * @return 文件字节流
     */
    public static byte[] downloadFile(String group, String path) {
        FastFileStorageClient fastFileStorageClient = SpringHelper.getBean(FastFileStorageClient.class);
        return fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
    }
}
