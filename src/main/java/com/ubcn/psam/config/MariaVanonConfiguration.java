package com.ubcn.psam.config;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(value = "com.ubcn.psam.mapper.maria", sqlSessionFactoryRef = "SqlSessionFactory")
@EnableTransactionManagement
public class MariaVanonConfiguration {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private ApplicationContext applicationContext;

    @Bean(name = "DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")                                       
    DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "SqlSessionFactory")
	@Primary
	SqlSessionFactory SqlSessionFactory(@Qualifier("DataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("file:config/mapper/maria/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "TransactionManager")
	@Primary
	PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}


	@Bean(name = "SqlSessionTemplate")
	@Primary
	public SqlSessionTemplate SqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory SqlSessionFactory) {
		logger.info("datasource DatabaseConfiguration SqlSessionTemplate: {}");
		return new SqlSessionTemplate(SqlSessionFactory);
	}
	

}
