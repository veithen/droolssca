package com.google.code.droolssca.impl;

import com.google.code.droolssca.DroolsImplementation;
import com.google.code.droolssca.DroolsImplementationFactory;

public class DroolsImplementationFactoryImpl implements DroolsImplementationFactory {
    public DroolsImplementation createDroolsImplementation() {
        return new DroolsImplementationImpl();
    }
}
