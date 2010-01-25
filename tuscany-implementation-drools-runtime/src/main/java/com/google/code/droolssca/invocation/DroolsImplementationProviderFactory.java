package com.google.code.droolssca.invocation;

import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.provider.ImplementationProvider;
import org.apache.tuscany.sca.provider.ImplementationProviderFactory;
import org.apache.tuscany.sca.runtime.RuntimeComponent;

import com.google.code.droolssca.DroolsImplementation;

public class DroolsImplementationProviderFactory implements ImplementationProviderFactory<DroolsImplementation>{
    public DroolsImplementationProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        
    }
    
    public Class<DroolsImplementation> getModelType() {
        return DroolsImplementation.class;
    }

    public ImplementationProvider createImplementationProvider(RuntimeComponent component, DroolsImplementation implementation) {
        return new DroolsImplementationProvider(component, implementation);
    }
}
