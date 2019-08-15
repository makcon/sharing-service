package test.maksim.sharing.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharingServiceClientConfig {

    private final SharingServiceClientFactory factory = new SharingServiceClientFactory();

    @Bean
    public SharingServiceClient contentClient(@Value("${sharing.service.base.url:http://localhost:8080}") String serviceUrl) {
        return factory.defaultClient(serviceUrl);
    }
}
