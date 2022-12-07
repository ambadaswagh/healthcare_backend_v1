package com.healthcare;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.healthcare")
public class DbUnitIntegrationTestConfiguration {

    @Value("${spring.datasource.schema}")
    private String schemaName;

    @Autowired
    private DataSource dataSource;

    @Bean("dbUnitDatabaseConfig")
    public DatabaseConfigBean getDatabaseConfigBean() {
        DatabaseConfigBean databaseConfig = new DatabaseConfigBean();
        databaseConfig.setDatatypeFactory(new MySqlDataTypeFactory());
        databaseConfig.setMetadataHandler(new MySqlMetadataHandler());
        databaseConfig.setCaseSensitiveTableNames(true);

        return databaseConfig;
    }

    @Bean("dbUnitDatabaseConnection")
    public DatabaseDataSourceConnectionFactoryBean getConnectionFactoryBean() {
        DatabaseDataSourceConnectionFactoryBean connectionFactoryBean = new DatabaseDataSourceConnectionFactoryBean();
        connectionFactoryBean.setDatabaseConfig(getDatabaseConfigBean());
        connectionFactoryBean.setDataSource(dataSource);
        connectionFactoryBean.setSchema(schemaName);

        return connectionFactoryBean;
    }
}
