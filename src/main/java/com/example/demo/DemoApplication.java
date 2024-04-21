package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(RoleService roleService) {
//        return args -> {
//            roleService.saveRole(new Role(null, ERole.MANAGER));
//            roleService.saveRole(new Role(null, ERole.STAFF));
//        };
//    }
}
