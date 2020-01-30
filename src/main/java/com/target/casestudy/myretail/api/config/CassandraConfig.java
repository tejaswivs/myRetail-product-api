package com.target.casestudy.myretail.api.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraEntityClassScanner;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
@Configuration
@EnableCassandraRepositories("com.target.casestudy.myretail.api.repositories")
public class CassandraConfig extends AbstractCassandraConfiguration {

    private static final Log LOGGER = LogFactory.getLog(CassandraConfig.class);
    private static final String kepSpace = "my_retail";

    @PostConstruct
    public void startEmbeddedCassandra() throws InterruptedException, IOException, TTransportException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        LOGGER.info("Started Embedded Cassandra Cluster");
    }

    @PreDestroy
    public void stopEmbeddedCassandra(){
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
        LOGGER.info("Disposed Embedded Cassandra Cluster");
    }

    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(kepSpace)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication());
    }

    @Bean
    public CassandraCqlClusterFactoryBean cluster() {
        CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
        bean.setKeyspaceCreations(getKeyspaceCreations());
        bean.setContactPoints("127.0.0.1");
        bean.setPort(9142);
        return bean;
    }

    @Bean
    public CassandraSessionFactoryBean session() {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster().getObject());
        session.setKeyspaceName(kepSpace);
        try {
            session.setConverter(converter());
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.setStartupScripts(getStartupScripts());
        session.setSchemaAction(SchemaAction.CREATE_IF_NOT_EXISTS);
        return session;
    }

    @Override
    protected String getKeyspaceName() {
        return kepSpace;
    }

    @Bean
    public CassandraConverter converter() throws Exception {
        return new MappingCassandraConverter(mappingContext());
    }

    @Bean
    public CassandraMappingContext mappingContext() throws Exception {
        CassandraMappingContext mappingContext = new CassandraMappingContext();
        mappingContext.setInitialEntitySet(CassandraEntityClassScanner.scan("com.target.casestudy.myretail.api.domain"));
        mappingContext.setUserTypeResolver(new
                SimpleUserTypeResolver(cluster().getObject(), kepSpace));
        return mappingContext;
    }

}
