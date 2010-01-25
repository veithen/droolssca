package com.google.code.droolssca;

import java.util.List;

import org.apache.tuscany.sca.assembly.Implementation;

public interface DroolsImplementation extends Implementation {
    String getName();
    void setName(String name);
    String getInterface();
    void setInterface(String interfaze);
    List<Resource> getResources();
}
