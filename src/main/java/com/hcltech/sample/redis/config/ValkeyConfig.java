package com.hcltech.sample.redis.config;

import glide.api.GlideClient;
import glide.api.models.configuration.AdvancedGlideClientConfiguration;
import glide.api.models.configuration.GlideClientConfiguration;
import glide.api.models.configuration.TlsAdvancedConfiguration;
import io.valkey.springframework.data.valkey.connection.ValkeyConnectionFactory;
import io.valkey.springframework.data.valkey.connection.ValkeyStandaloneConfiguration;
import io.valkey.springframework.data.valkey.connection.valkeyglide.ValkeyGlideConnectionFactory;
import io.valkey.springframework.data.valkey.core.StringValkeyTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

@Configuration
public class ValkeyConfig {
    @Value("${spring.data.valkey.host}")
    private String host;
    @Value("${spring.data.valkey.port}")
    private int port;

    @Bean
    ValkeyConnectionFactory valkeyConnectionFactory(final SslBundles sslBundles) throws Exception {
        SSLContext.setDefault(sslBundles.getBundle("valkey-tls").createSslContext());
        ValkeyStandaloneConfiguration standaloneConfiguration = new ValkeyStandaloneConfiguration(host, port);

        byte[] caCert = new ClassPathResource("certs/ca.crt").getContentAsByteArray();
        GlideClientConfiguration glideClientConfiguration = GlideClientConfiguration.builder().advancedConfiguration(AdvancedGlideClientConfiguration.builder().tlsAdvancedConfiguration(TlsAdvancedConfiguration.builder().rootCertificates(caCert).build()).build()).build();

        GlideClient glideClient = GlideClient.createClient(glideClientConfiguration).get(2, TimeUnit.SECONDS);
        return new ValkeyGlideConnectionFactory(standaloneConfiguration);
    }

    @Bean
    StringValkeyTemplate stringValkeyTemplate(ValkeyConnectionFactory factory) {
        return new StringValkeyTemplate(factory);
    }
}
