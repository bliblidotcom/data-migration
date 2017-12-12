package com.gdn.data.migration.elasticsearch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Eko Kurniawan Khannedy
 * @since 19/10/16
 */
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchConfiguration {

  private String protocol = "http";

  private String host;

  private Integer port;

  private String index;

  public String getUrl() {
    return protocol + "://" + host + ":" + port;
  }

  public String getFullUrl() {
    return protocol + "://" + host + ":" + port + "/" + index;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }
}
