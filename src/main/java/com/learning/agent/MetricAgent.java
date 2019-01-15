package com.learning.agent;

import com.learning.config.ArgParser;
import com.learning.config.Configuration;
import com.learning.reporting.MetricReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class MetricAgent {
    private final static Logger logger = LoggerFactory.getLogger(MetricAgent.class);

    public static void premain(String agentArguments, Instrumentation instrumentation) {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        logger.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
        logger.info("Starting agent with arguments " + agentArguments);

        ArgParser argParser = new ArgParser(agentArguments);

        Configuration config = Configuration.createConfig(argParser.getConfigFilename());
        MetricReporter metricReporter = MetricReporter.getInstance(config);
//        metricReporter.startJmxReporter();
//        metricReporter.init();
//        metricReporter.startGraphiteReporter();
        // define the class transformer to use
        instrumentation.addTransformer(new TimedClassTransformer(config));
    }


}