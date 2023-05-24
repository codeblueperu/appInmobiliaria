package mac.inmobiliaria.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("mac.inmobiliaria.repository")
@ComponentScan("mac.inmobiliaria*")
public class DataConexion {
	
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(jpaAdapter);
		factory.setPackagesToScan(new String[] { "mac.inmobiliaria.model" });
		factory.setDataSource(dataSource());
		factory.setJpaProperties(addProperties());
		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory.getObject();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource driver = new DriverManagerDataSource();
		driver.setDriverClassName("com.mysql.cj.jdbc.Driver");
		
		driver.setUrl("jdbc:mysql://localhost:3306/macinmobiliariadata?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true");
		driver.setUsername("root");
		driver.setPassword("");
		//janpier159
		
//		driver.setUrl("jdbc:mysql://localhost:3306/jampiert_inmobiliaria?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true");
//		driver.setUsername("jampiert_inmobiliaria");
//		driver.setPassword("APUaf2023**");


		return driver;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		EntityManagerFactory emf = entityManagerFactory();
		return new JpaTransactionManager(emf);
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource datasource){
	    JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
	    return jdbcTemplate;
	}

	Properties addProperties() {
		Properties propiedad = new Properties();
		propiedad.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		//#CREAR LAS TABLAS
		propiedad.setProperty("hibernate.hbm2ddl.auto", "update");
		//#VALIDA TABLAS
		//propiedad.setProperty("hibernate.hbm2ddl.auto", "validate");
		propiedad.setProperty("hibernate.show_sql", "true");
		propiedad.setProperty("hibernate.format_sql", "true");
		return propiedad;
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

}
