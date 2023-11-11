package dev.clownsinformatics.tiendajava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TiendaJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiendaJavaApplication.class, args);
    }

}
