package com.yqg.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.yqg.common.dynamicdata.DynamicDataSource;
import com.yqg.common.dao.CustomRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class,
        entityManagerFactoryRef = "dynamicEntityManagerFactory",
        transactionManagerRef = "dynamicTransactionManager",
        basePackages = "com.**.dao")
@EntityScan(basePackages = "com.**.entity")
public class JpaConfiguration {

    @Bean
    PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }


    @Autowired
    JpaProperties jpaProperties;

    @Autowired
    @Qualifier("dynamicDataSource")
    private AbstractRoutingDataSource dynamicDataSource;

    @Autowired
    @Qualifier("dynamicEntityManagerFactory")
    private EntityManagerFactory dynamicEntityManagerFactory;

    /**
     * DataSource-write 配置
     *
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.write")
    @Bean(name = "writeDruidDataSource")
    @Primary
    public DataSource writeDruidDataSource() {
        return new DruidDataSource();
    }

    /**
     * DataSource-read 配置
     *
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.read")
    @Bean(name = "readDruidDataSource")
    public DataSource readDruidDataSource() {
        return new DruidDataSource();
    }


    public final static String WRITE_DATASOURCE_KEY = "writeDruidDataSource";
    public final static String READ_DATASOURCE_KEY = "readDruidDataSource";

    /**
     * 注入AbstractRoutingDataSource
     *
     * @param readDruidDataSource
     * @param writeDruidDataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "dynamicDataSource")
    public AbstractRoutingDataSource routingDataSource(@Qualifier(READ_DATASOURCE_KEY) DataSource readDruidDataSource,
                                                       @Qualifier(WRITE_DATASOURCE_KEY) DataSource writeDruidDataSource) throws Exception {

        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(WRITE_DATASOURCE_KEY, writeDruidDataSource);
        targetDataSources.put(READ_DATASOURCE_KEY, readDruidDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(writeDruidDataSource);

        return dataSource;
    }

    /**
     * EntityManagerFactory类似于Hibernate的SessionFactory,mybatis的SqlSessionFactory
     * 总之,在执行操作之前,我们总要获取一个EntityManager,这就类似于Hibernate的Session,
     * mybatis的sqlSession.
     * @return
     */
    @Bean(name = "dynamicEntityManagerFactory")
    @Primary
    public EntityManagerFactory dynamicEntityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setDataSource(dynamicDataSource);//数据源
        factory.setPackagesToScan("com.**.entity");
        factory.setJpaPropertyMap(jpaProperties.getProperties());
        factory.afterPropertiesSet();//在完成了其它所有相关的配置加载以及属性设置后,才初始化
        return factory.getObject();
    }

    /**
     * 配置事物管理器
     * @return
     */
    @Bean(name = "dynamicTransactionManager")
    @Primary
    public PlatformTransactionManager writeTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(dynamicEntityManagerFactory);
        return jpaTransactionManager;
    }
}
