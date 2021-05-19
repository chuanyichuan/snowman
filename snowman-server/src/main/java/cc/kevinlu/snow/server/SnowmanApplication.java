package cc.kevinlu.snow.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cc.kevinlu.snow.server.utils.helper.ThreadPoolHelper;

@SpringBootApplication(scanBasePackages = { "cc.kevinlu.snow.server" })
@EnableDiscoveryClient
@RefreshScope
@EnableAsync
public class SnowmanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowmanApplication.class, args);
    }

    @Bean(name = "taskExecutor")
    @Primary
    public ThreadPoolTaskExecutor taskExecutor(TaskExecutionProperties properties) {
        return ThreadPoolHelper.newTaskThreadPool(properties);
    }

}
