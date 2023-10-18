package ru.skillbox.diplom.group42.social.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.skillbox.diplom.group42.social.service.repository.base.BaseRepositoryImpl;

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.skillbox.diplom.group42.social.service.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}