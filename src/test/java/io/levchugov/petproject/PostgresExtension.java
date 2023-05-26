package io.levchugov.petproject;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.Extension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;


@Slf4j
public class PostgresExtension implements Extension {

    static {
        PostgreSQLContainer container =
                new PostgreSQLContainer<>("postgres:12.9")
                        .withLogConsumer(new Slf4jLogConsumer(log))
                        .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                                new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(49432), new
                                        ExposedPort(5432)))
                        ));

        container.start();

        System.setProperty("spring.datasource.url", container.getJdbcUrl() + "&currentSchema=public");
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());

    }

}
