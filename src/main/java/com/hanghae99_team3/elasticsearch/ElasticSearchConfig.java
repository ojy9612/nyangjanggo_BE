package com.hanghae99_team3.elasticsearch;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.hanghae99_team3.model.resource.repository")
@ComponentScan(basePackages = {"com.hanghae99_team3"})
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private final String username = "elastic";
    private final String password = "changeme";

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public BasicCredentialsProvider credentialsProvider(){
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        basicCredentialsProvider.setCredentials(AuthScope.ANY,credentials);

        return basicCredentialsProvider;
    }

}