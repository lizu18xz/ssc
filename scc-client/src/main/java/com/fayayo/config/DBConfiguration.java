package com.fayayo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.fayayo.bean.DBParam;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author dalizu on 2019/9/27.
 * @version v1.0
 * @desc
 */
@Configuration
@ConditionalOnProperty(name = "system.db-enable",havingValue = "true")
@AutoConfigureAfter(ParamConfiguration.class)
public class DBConfiguration {

    @Autowired
    ParamConfiguration paramConfiguration;

    @Bean
    public DataSource dataSource(){

        DBParam dbParam = paramConfiguration.getDbParam();

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbParam.getUrl());
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(dbParam.getUsername());
        dataSource.setPassword(dbParam.getPassword());
        dataSource.setInitialSize(1);
        dataSource.setMaxActive(20);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        List<Resource> resources = Lists.newArrayList();
        resources.addAll(Arrays.asList(
                new PathMatchingResourcePatternResolver().getResource("classpath:mapper/*.xml")
        ));

        sessionFactoryBean.setMapperLocations(resources.toArray(
                new Resource[resources.size()]
        ));
        return sessionFactoryBean.getObject();
    }


    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){

        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
