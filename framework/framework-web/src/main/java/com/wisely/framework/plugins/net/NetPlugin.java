package com.wisely.framework.plugins.net;

import com.wisely.framework.exception.SystemException;
import com.wisely.framework.plugins.AbstractPlugin;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * 网络通讯插件
 */
@ConditionalOnProperty(prefix = "plugins.net", value = "enabled", havingValue = "true")
public class NetPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "NetPlugin";
    }

    @Bean
    public NetProperties netProperties() {
        return new NetProperties();
    }

    /**
     * http客户端
     *
     * @param netProperties
     * @return
     */
    @Primary
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(NetProperties netProperties) {

        RestTemplate restTemplate = new RestTemplate();

        // 使用连接池
        if (netProperties.isUserPool()) {

            HttpComponentsClientHttpRequestFactory hcchrf = new HttpComponentsClientHttpRequestFactory();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(netProperties.getMaxTotal());
            connectionManager.setDefaultMaxPerRoute(netProperties.getDefaultMaxPerRoute());

            RequestConfig.Builder rcb = RequestConfig.custom();
            if (netProperties.getConnectTimeout() > 0) {
                rcb.setConnectTimeout(netProperties.getConnectTimeout());
            }
            if (netProperties.getReadTimeout() > 0) {
                rcb.setSocketTimeout(netProperties.getReadTimeout());
            }
            if (netProperties.getConnectRequestTimeout() > 0) {
                rcb.setConnectionRequestTimeout(netProperties.getConnectRequestTimeout());
            }

            hcchrf.setHttpClient(
                    HttpClientBuilder.create()
                            .setDefaultRequestConfig(rcb.build())
                            .setConnectionManager(connectionManager)
                            .setConnectionManagerShared(true) // 设置共享连接池
                            .build());

            restTemplate.setRequestFactory(hcchrf);
        }

        return restTemplate;
    }


    /**
     * https客户端
     *
     * @param netProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "restTemplate4Https")
    public RestTemplate restTemplate4Https(NetProperties netProperties) {

        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());

        // 使用连接池
        if (netProperties.isUserPool()) {

            HttpComponentsClientHttpRequestFactory hcchrf = new HttpComponentsClientHttpRequestFactory();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(netProperties.getMaxTotal());
            connectionManager.setDefaultMaxPerRoute(netProperties.getDefaultMaxPerRoute());

            RequestConfig.Builder rcb = RequestConfig.custom();
            if (netProperties.getConnectTimeout() > 0) {
                rcb.setConnectTimeout(netProperties.getConnectTimeout());
            }
            if (netProperties.getReadTimeout() > 0) {
                rcb.setSocketTimeout(netProperties.getReadTimeout());
            }
            if (netProperties.getConnectRequestTimeout() > 0) {
                rcb.setConnectionRequestTimeout(netProperties.getConnectRequestTimeout());
            }

            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            } catch (Exception e) {
                throw SystemException.builder(e, "NetPlugins.httpsClient_build_error");
            }

            hcchrf.setHttpClient(
                    HttpClientBuilder.create()
                            .setDefaultRequestConfig(rcb.build())
                            .setConnectionManager(connectionManager)
                            .setConnectionManagerShared(true) // 设置共享连接池
                            .setSSLContext(sslContext)
                            .setSSLHostnameVerifier(new NoopHostnameVerifier())
                            .build());

            restTemplate.setRequestFactory(hcchrf);
        }

        return restTemplate;
    }
}
