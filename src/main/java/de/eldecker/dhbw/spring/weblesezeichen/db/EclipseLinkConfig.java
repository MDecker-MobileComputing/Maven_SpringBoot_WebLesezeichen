package de.eldecker.dhbw.spring.weblesezeichen.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;


@Configuration
public class EclipseLinkConfig {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {

        EclipseLinkJpaVendorAdapter eclipseLinkJpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        eclipseLinkJpaVendorAdapter.setShowSql(true);
        eclipseLinkJpaVendorAdapter.setGenerateDdl(false);
        eclipseLinkJpaVendorAdapter.setDatabasePlatform("org.eclipse.persistence.platform.database.H2Platform");
        return eclipseLinkJpaVendorAdapter;
    }
}