package com.learn.finance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Data
@Configuration
@ConfigurationProperties(value = "swift", ignoreUnknownFields = false)
public class DownStreamProperties {

  private Url client;

  /**
   * Maps to the configuration ${nba.client.apikey}
   */
  private String apiKeyHeader;

  /**
   * Maps to the configuration ${nba.client.host}
   */
  private String hostHeader;

  @Data
  public static class Url {
    private String url;
  }


}
