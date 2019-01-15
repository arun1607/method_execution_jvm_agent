package com.learning.reporting;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.learning.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class MetricReporter {
    private final static Logger logger = LoggerFactory.getLogger(MetricReporter.class);

    private static MetricReporter instance = null;

    private static MetricRegistry metricRegistry = new MetricRegistry();
    private Configuration configuration;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public MetricReporter(Configuration configuration) {
        this.configuration = configuration;
    }

    public static MetricReporter getInstance(Configuration configuration) {
        if (instance == null) {
            instance = new MetricReporter(configuration);
        }
        return instance;
    }

    public void init() {

    }

    public void startJmxReporter() {
        logger.info("Init JMX reporter");

        JmxReporter jmxReporter = JmxReporter
                .forRegistry(metricRegistry)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .convertRatesTo(TimeUnit.MINUTES)
                .build();
        jmxReporter.start();
    }

    public void startGraphiteReporter() {
        String host = configuration.getGraphite().getHost();
        int port = configuration.getGraphite().getPort();
        String prefix = configuration.getGraphite().getPrefix();
        logger.info("Init Graphite reporter: host={}, port={}, prefix={}", host, port, prefix);
        Graphite graphite = new Graphite(new InetSocketAddress(host, port));
        GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                .prefixedWith(prefix)
                .build(graphite);
        graphiteReporter.start(5, TimeUnit.SECONDS);
    }

    // called by instrumented methods
    public static void reportTime(String name, long timeInMs) {
//        reportTimeInSeparateThread(name, timeInMs);
        printExecutionTime(name, timeInMs);
    }

    public static void printExecutionTime(String name, long timeInMs) {
        logger.warn(" -------------------------------------------------");
        logger.warn(Thread.currentThread().getName() + " {} took {} milli seconds to execute", name, timeInMs);
        logger.warn(" -------------------------------------------------");
    }

    private static void reportTimeInSeparateThread(String name, long timeInMs) {
        executorService.submit(() -> {
            logger.info("Updating Graphite data");
            Timer timer = metricRegistry.timer(name);
            timer.update(timeInMs, TimeUnit.MILLISECONDS);
            printExecutionTime(name, timeInMs);
        });
    }
}
