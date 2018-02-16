package com.learning.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Graphite {
    private String host;
    private int port;
    private String prefix;

    @JsonCreator
    public Graphite(@JsonProperty("host") String host, @JsonProperty("port") int port, @JsonProperty("prefix") String prefix) {
        this.host = host;
        this.port = port;
        this.prefix = prefix;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public String toString() {
        return "Graphite{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}

