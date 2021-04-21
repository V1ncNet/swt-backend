package de.team7.swt;

import de.team7.data.inmemory.repository.config.EnableInMemoryRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application class that contains the main-method and thus akts as the entrypoint into the application.
 *
 * @author Vincent Nadoll
 */
@SpringBootApplication
@EnableInMemoryRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
