package com.hanium.gabojago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GabojagoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GabojagoApplication.class, args);
    }

}
