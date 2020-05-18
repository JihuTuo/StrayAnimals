package com.hsshy.beam.dynamic.datasource.config;
import com.hsshy.beam.dynamic.datasource.property.DataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;

/**
 * DruidDataSource
 */
public class DynamicDataSourceFactory {

    public static HikariDataSource buildDruidDataSource(DataSourceProperties properties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(properties.getJdbcUrl());
        hikariDataSource.setUsername(properties.getUsername());
        hikariDataSource.setPassword(properties.getPassword());
        hikariDataSource.setDriverClassName(properties.getDriverClassName());
        hikariDataSource.setConnectionTimeout(properties.getConnectionTimeout());
        hikariDataSource.setIdleTimeout(properties.getIdleTimeout());
        hikariDataSource.setValidationTimeout(properties.getValidationTimeout());
        hikariDataSource.setMaxLifetime(properties.getMaxLifetime());
        hikariDataSource.setMaximumPoolSize(properties.getMaximumPoolSize());
        hikariDataSource.setMinimumIdle(properties.getMinimumIdle());
        hikariDataSource.setReadOnly(false);

        try {
            hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hikariDataSource;
    }
}