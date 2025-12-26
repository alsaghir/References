package com.github.alsaghir.server.infrastructure.db;

import com.github.alsaghir.server.infrastructure.db.postgres.V20220902__Base_tables;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = V20220902__Base_tables.class)
public class Postgres {
}