package com.flowers.springpostgresdemo.config;

import org.springframework.context.annotation.Configuration;

/**
 * This class previously handled data initialization.
 * Data initialization has been moved to SQL scripts that are executed
 * during database startup via Docker. See the init.sql file in resources
 * and the docker-compose.yml configuration.
 */
@Configuration
public class DataInitializer {
    // Data initialization has been moved to SQL scripts
}
