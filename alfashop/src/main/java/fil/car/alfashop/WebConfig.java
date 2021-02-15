package fil.car.alfashop;

import org.slf4j.LoggerFactory;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration 
@EnableWebMvc

@EnableJdbcRepositories("controller")
public class WebConfig 
implements ApplicationListener<ApplicationContextEvent>
{

	private static Logger log = LoggerFactory.getLogger(WebConfig.class);
	
	@Bean 
	public InternalResourceViewResolver resolver() {
		InternalResourceViewResolver res = new InternalResourceViewResolver();
		res.setPrefix("/WEB-INF/views/");
		res.setSuffix(".jsp");
		return res;
	}

	
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean emf () {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setPersistenceUnitName("myApp");
		return emf;
	}
	
	@Bean(name="transactionManager")
	public PlatformTransactionManager txManager( EntityManagerFactory emf ) {
		JpaTransactionManager txManager = new JpaTransactionManager(emf);
		txManager.setPersistenceUnitName("myApp");
		return txManager;
	}
	
	
	
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		log.info( "Receive application event : " + event.getClass().getSimpleName() );
		if ( !(event instanceof ContextClosedEvent) ) return;
		
		log.info("Stop EntityManagerFactory.");
		EntityManagerFactory emf = event.getApplicationContext().getBean(EntityManagerFactory.class);
		emf.close();
		
		log.info("Unregister H2 JDBC Driver.");
		try {
			//A changer l'url
			Driver driver = DriverManager.getDriver("jdbc:mysql://${MYSQL_HOST:localhost}:3306/carshop");
			DriverManager.deregisterDriver(driver);
		} catch( SQLException e ) {
			log.info("Unable to unregister driver.", e);
		}
		
	}
	
}
