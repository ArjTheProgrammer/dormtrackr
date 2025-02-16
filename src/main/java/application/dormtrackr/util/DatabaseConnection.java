package application.dormtrackr.util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    static{
        HikariConfig config = new HikariConfig();

        // Azure SQL Server connection settings
        config.setJdbcUrl("jdbc:sqlserver://property-database.database.windows.net:1433;database=PropertyManagementSystem");
        config.setUsername("rjsilagan@property-database");  // Use environment variable
        config.setPassword("Dormtrackr888");  // Use environment variable
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Optimized for Azure SQL Serverless Free Tier
        config.setMaximumPoolSize(3);      // Reduced for 2 vCores
        config.setMinimumIdle(1);          // Minimal idle connections
        config.setIdleTimeout(60000);      // 1 minute idle timeout
        config.setConnectionTimeout(5000);  // 5 seconds connection timeout
        config.setMaxLifetime(300000);     // 5 minutes max lifetime
        config.setLeakDetectionThreshold(60000); // Help identify connection leaks

        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void closePool() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
