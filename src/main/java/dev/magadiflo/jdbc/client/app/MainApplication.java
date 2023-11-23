package dev.magadiflo.jdbc.client.app;

import dev.magadiflo.jdbc.client.app.post.IPostService;
import dev.magadiflo.jdbc.client.app.post.Post;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(@Qualifier("jdbcClientService") IPostService postService) {
        return args -> {
            postService.create(new Post("1234", "Hello World", "hello-world", LocalDate.now(), 1, "java, spring boot"));
        };
    }

}
