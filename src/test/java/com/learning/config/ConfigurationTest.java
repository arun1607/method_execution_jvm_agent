package com.learning.config;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void createConfig() {
        Configuration configuration = Configuration.createConfig(getClass().getClassLoader().getResourceAsStream("application.yaml"));
        Assert.assertTrue(configuration.isClassAllowed("com.identityforge.idfserver.backend.tops.transport.TopsSocketConnection"));
        Assert.assertTrue(configuration.isMethodAllowed("com.identityforge.idfserver.backend.tops.transport.TopsSocketConnection", "getEncrypted"));
        Assert.assertEquals(2003, configuration.getGraphite().getPort());
    }

}