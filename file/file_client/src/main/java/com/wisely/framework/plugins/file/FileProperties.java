package com.wisely.framework.plugins.file;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileProperties {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 模式
     * 默认-local
     * fastdfs
     */
    private String mode = "local";

    /**
     * 文件最大限制
     */
    private String maxFileSize = "50MB";

    /**
     * 请求最大限制
     */
    private String maxRequestSize = "50MB";

    /**
     * 下载加密key
     */
    private String encryptKey = "6A32D40E1838B8151B3A36C30CF86ADA";

    /**
     * 本地文件
     */
    private LocalConfig local = new LocalConfig();

    /**
     * fastdfs
     */
    private FastdfsConfig fastdfs = new FastdfsConfig();

    /**
     * minio
     */
    private MinIOConfig minio = new MinIOConfig();

    /**
     * 本地文件模式
     */
    @Setter
    @Getter
    public static class LocalConfig {
        private String path;
    }

    /**
     * fastdfs模式
     */
    @Setter
    @Getter
    public static class FastdfsConfig {
        /**
         * 分组名称
         */
        private String groupName = "group1";
        /**
         * 服务IP
         * 支持多个
         */
        private String serverIp;
        /**
         * 服务端口
         * 默认22122
         */
        private Integer serverPort = 22122;
        /**
         * 最大连接数
         */
        private Integer maxConnection = 5;
        /**
         * 连接超时时间
         */
        private Integer connectionTimeout = 2000;
        /**
         * 网络超时时间
         */
        private Integer networkTimeout = 3000;
        /**
         * token有效性
         */
        private boolean stealToken = false;
        /**
         * 密钥
         */
        private String secretKey;
        /**
         * 字符集编码
         */
        private String charset = "UTF-8";
    }

    @Setter
    @Getter
    public static class MinIOConfig {
        /**
         * MinIO服务地址
         */
        private String endpoint;
        /**
         * 文件分隔符
         */
        private String separator = "/";
        /**
         * 桶名称
         */
        private String bucketName = "default";
        /**
         * accessKey
         */
        private String account = "admin";
        /**
         * secretKey
         */
        private String password = "sdect@12345678";
    }


}
