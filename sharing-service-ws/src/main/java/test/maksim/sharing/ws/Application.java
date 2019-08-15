package test.maksim.sharing.ws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AsyncListenableTaskExecutor serviceExecutor(@Value("${service-executor.core.pool.size:12}") int corePoolSize,
                                                       @Value("${service-executor.max.pool.size:15}") int maxPoolSize) {
        var executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("sharing-ws-");
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);

        return executor;
    }
}
