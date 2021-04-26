package cc.kevinlu.snow.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(scanBasePackages = { "cc.kevinlu.snow.server" })
@EnableDiscoveryClient
@RefreshScope
public class SnowmanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowmanApplication.class, args);
    }

}
