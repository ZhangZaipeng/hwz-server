package com.hwz.platform.es;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于构造搜索引擎客户端
 */
public class EsClientFactory {
    private String namespace;
    private final String address;
    private final JestClient jestClient;
    private static final int CONN_TIMEOUT = 5 * 1000;
    private static final int READ_TIMEOUT = 60 * 1000;

    @JsonCreator
    public EsClientFactory(String address) {
        this.address = address;

        Set<String> servers = new HashSet<>();
        for (String s : this.address.split(",")) {
            servers.add(s.trim().toLowerCase());
        }
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(
                new HttpClientConfig.Builder(servers)
                        .connTimeout(CONN_TIMEOUT)
                        .readTimeout(READ_TIMEOUT)
                        .multiThreaded(true)
                        .build()
        );
        jestClient = factory.getObject();
    }

    public JestClient getJestClient() {
        return jestClient;
    }

    public String getAddress() {
        return address;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
