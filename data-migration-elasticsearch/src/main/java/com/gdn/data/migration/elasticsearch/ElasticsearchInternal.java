package com.gdn.data.migration.elasticsearch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdn.data.migration.core.Internal;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eko Kurniawan Khannedy
 * @since 20/10/16
 */
public class ElasticsearchInternal implements Internal {

  private ElasticsearchConfiguration configuration;

  private OkHttpClient okHttpClient;

  private ObjectMapper objectMapper;

  private TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<Map<String, Object>>() {
  };

  public ElasticsearchInternal(ElasticsearchConfiguration configuration, OkHttpClient okHttpClient,
                               ObjectMapper objectMapper) {
    this.configuration = configuration;
    this.okHttpClient = okHttpClient;
    this.objectMapper = objectMapper;
  }

  /**
   * Get count of version type in elasticsearch
   *
   * @return count
   * @throws IOException
   */
  private Long getCount() {
    try {
      Request request = new Request.Builder()
          .url(configuration.getFullUrl() + "/" + VERSION_NAME + "/_count")
          .get()
          .build();

      ResponseBody body = okHttpClient.newCall(request).execute().body();
      String json = body.string();
      body.close();
      Map<String, Object> map = objectMapper.readValue(json, mapTypeReference);

      return Long.valueOf(map.get("count").toString());
    } catch (Throwable tx) {
      throw new RuntimeException(tx);
    }
  }

  @Override
  public void ensureVersion() {
    if (getCount() == 0L) {
      updateVersion(0L);
    }
  }

  @Override
  public void updateVersion(Long version) {
    try {
      Map<String, Object> map = new HashMap<>();
      map.put(VERSION_NAME, version);

      String json = objectMapper.writeValueAsString(map);

      Request request = new Request.Builder()
          .url(configuration.getFullUrl() + "/" + VERSION_NAME + "/" + VERSION_NAME)
          .put(RequestBody.create(MediaType.parse("application/json"), json))
          .build();

      okHttpClient.newCall(request).execute().close();

      Thread.sleep(1000L); // elasticsearch is not realtime, so we sleep 1 second
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Long currentVersion() {
    try {
      Request request = new Request.Builder()
          .url(configuration.getFullUrl() + "/" + VERSION_NAME + "/" + VERSION_NAME)
          .get()
          .build();

      ResponseBody body = okHttpClient.newCall(request).execute().body();
      String json = body.string();
      body.close();

      Map<String, Object> map = objectMapper.readValue(json, mapTypeReference);
      Map<String, Object> source = (Map<String, Object>) map.get("_source");

      return Long.valueOf(source.get(VERSION_NAME).toString());
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
