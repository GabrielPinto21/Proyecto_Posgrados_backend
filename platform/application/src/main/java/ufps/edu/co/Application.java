package ufps.edu.co;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication(scanBasePackages = "ufps.edu.co")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}