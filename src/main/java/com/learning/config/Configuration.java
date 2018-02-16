package com.learning.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

/**
 * @author Will Fleury
 */
public class Configuration {

    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    private final static ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory()) {
        {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
            setVisibilityChecker(getSerializationConfig().getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        }
    };

    public static Configuration createConfig(String filename) {
        if (filename == null) {
            LOGGER.log(FINE, "Found config file: {0}", filename);
            throw new RuntimeException("Configuration file is required");
        }


        try {
            return createConfig(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Configuration createConfig(InputStream is) {
        try {
            return MAPPER.readValue(is, Configuration.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }


    private Graphite graphite;

    private Map<String, List<String>> classes;

    @JsonCreator
    public Configuration(
            @JsonProperty("classes") Map<String, List<String>> classes, @JsonProperty("graphite") Graphite graphite) {
        this.graphite = graphite;
        this.classes = classes == null ? Collections.emptyMap() : classes;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "classes=" + classes +
                ", graphite=" + graphite +
                '}';
    }

    public boolean isClassAllowed(String className) {
        return classes.containsKey(className);
    }

    public boolean isMethodAllowed(String className, String methodName) {
        return classes.get(className).contains(methodName);
    }


    public Graphite getGraphite() {
        return graphite;
    }

}
