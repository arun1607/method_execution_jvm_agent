package com.learning.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class InstrumentionClass {

    private Set<String> methods;

    @JsonCreator
    public InstrumentionClass(@JsonProperty("methods") Set<String> methods) {
        this.methods = methods;
    }



    @Override
    public String toString() {
        return "InstrumentionClass{" +
                "methods=" + methods +
                '}';
    }
}
