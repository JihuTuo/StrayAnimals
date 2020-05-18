package com.hsshy.beam.dynamic.datasource.property;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
/**
 * <p>数据库数据源配置</p>
 */
@Data
public class DataSourceProperties {

    private String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/beam?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";

    private String username = "root";

    private String password = "root";

    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    private long connectionTimeout = 60000L;

    private long idleTimeout = 58000L;

    private long validationTimeout = 3000L;

    private long maxLifetime = 60000L;

    private int maximumPoolSize = 30;

    private int minimumIdle = 10;

}
