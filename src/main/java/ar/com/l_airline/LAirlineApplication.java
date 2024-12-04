package ar.com.l_airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LAirlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(LAirlineApplication.class, args);
    }
}
