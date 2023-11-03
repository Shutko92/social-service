package ru.skillbox.diplom.group42.social.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;


@Slf4j
@ComponentScan
@ActiveProfiles("test")
@TestConfiguration
public class EmbeddedPostgresConfig {
    @Bean
    @Primary
    public DataSource inMemoryDS() throws Exception {
        DataSource embeddedPostgresDS = EmbeddedPostgres.builder()
                .start()
                .getPostgresDatabase();

        return embeddedPostgresDS;
    }
}
