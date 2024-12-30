package com.example.demo.elasticsearch;

import com.example.demo.util.Constant;
import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.KeywordProperty;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("es")
public class ElasticSearchConfig {

  @Autowired
  private static Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

  private String clusterName;
  private String esHost;
  private String esPort;

  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  @Bean
  public OpenSearchClient esClient() {
    logger.debug("Initializing OpenSearch Client");

    // Create the low-level client
    RestClient restClient = RestClient.builder(
            new HttpHost(getEsHost(), Integer.parseInt(getEsPort()))).build();

    // Create the transport with a Jackson mapper
    OpenSearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // Define the index mapping (e.g., for RESOURCE_ID field)
    TypeMapping mapping = new TypeMapping.Builder()
            .properties(Constant.RESOURCE_ID, new Property.Builder().keyword(new KeywordProperty.Builder().build()).build())
            .properties(Constant.TYPE, new Property.Builder().keyword(new KeywordProperty.Builder().build()).build())
            .build();

    // Create the index request
    CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
            .index(Constant.INDEX_VISION)
            .mappings(mapping)
            .build();

    // Create the API client
    OpenSearchClient esClient = new OpenSearchClient(transport);
    try {
      // Create the index if it doesn't already exist
      esClient.indices().create(createIndexRequest);
      logger.info("Index created: {}", Constant.INDEX_VISION);
    } catch (Exception e) {
      logger.warn("Index already exists or failed to create: {}", Constant.INDEX_VISION, e);
    }

    return esClient;
  }

  public String getEsHost() {
    return esHost;
  }

  public void setEsHost(String esHost) {
    this.esHost = esHost;
  }

  public String getEsPort() {
    return esPort;
  }

  public void setEsPort(String esPort) {
    this.esPort = esPort;
  }
}
