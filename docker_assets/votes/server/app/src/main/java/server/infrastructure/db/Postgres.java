package server.infrastructure.db;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import server.infrastructure.db.postgres.V20221203__Base_tables;

@Configuration
@ComponentScan(basePackageClasses = V20221203__Base_tables.class)
public class Postgres {
}