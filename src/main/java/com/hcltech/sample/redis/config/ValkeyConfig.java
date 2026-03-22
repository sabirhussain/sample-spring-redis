package com.hcltech.sample.redis.config;

import glide.api.GlideClient;
import glide.api.models.configuration.AdvancedGlideClientConfiguration;
import glide.api.models.configuration.GlideClientConfiguration;
import glide.api.models.configuration.NodeAddress;
import glide.api.models.configuration.TlsAdvancedConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ValkeyConfig {
    @Value("${spring.data.valkey.host}")
    private String host;
    @Value("${spring.data.valkey.port}")
    private int port;

    @Bean
    GlideClient glideClient() throws Exception {
        byte[] caCert = new ClassPathResource("certs/ca.crt")
                .getContentAsByteArray();
        log.info("CA Certificate loaded successfully, size: {} bytes", caCert.length);

        TlsAdvancedConfiguration tlsAdvanced = TlsAdvancedConfiguration.builder()
                .rootCertificates(caCert)   // raw PEM content of your CA cert
                .build();
        AdvancedGlideClientConfiguration advancedGlideClientConfiguration =
                AdvancedGlideClientConfiguration.builder().tlsAdvancedConfiguration(tlsAdvanced).build();

        GlideClientConfiguration glideConfig = GlideClientConfiguration.builder()
                .address(NodeAddress.builder().host(host).port(port).build())
                .useTLS(true)
                .advancedConfiguration(advancedGlideClientConfiguration)
                .build();
        return GlideClient.createClient(glideConfig).get(10, TimeUnit.SECONDS);
    }
}
