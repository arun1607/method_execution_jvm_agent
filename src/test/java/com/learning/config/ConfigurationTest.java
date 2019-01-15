package com.learning.config;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void createConfig() {
        Configuration configuration = Configuration.createConfig(getClass().getClassLoader().getResourceAsStream("application.yaml"));
        Assert.assertTrue(configuration.isClassAllowed("com.identityforge.idfserver.backend.rest.parser.RestParser"));
        Assert.assertTrue(configuration.isMethodAllowed("com.identityforge.idfserver.backend.rest.parser.RestParser", "parseEntries"));
        Assert.assertEquals(2003, configuration.getGraphite().getPort());
    }

}