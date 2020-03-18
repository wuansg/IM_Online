package xyz.silverspoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import xyz.silverspoon.utils.UUIDGenerator;

@SpringBootApplication
public class ImOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImOnlineApplication.class, args);
    }


    @Bean
    public UUIDGenerator uuidGenerator() {
        return new UUIDGenerator();
    }
}
