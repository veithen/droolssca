package com.google.code.droolssca.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tuscany.sca.assembly.impl.ImplementationImpl;

import com.google.code.droolssca.DroolsImplementation;
import com.google.code.droolssca.Resource;

public class DroolsImplementationImpl extends ImplementationImpl implements DroolsImplementation {
    private String name;
    private String interfaze;
    private final List<Resource> resources = new ArrayList<Resource>();
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterface() {
        return interfaze;
    }

    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
    }

    public List<Resource> getResources() {
        return resources;
    }
}
