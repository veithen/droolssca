package com.google.code.droolssca.invocation;

import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.provider.ImplementationProvider;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

import com.google.code.droolssca.DroolsImplementation;
import com.google.code.droolssca.Resource;

public class DroolsImplementationProvider implements ImplementationProvider {
    private final RuntimeComponent component;
    private final DroolsImplementation implementation;
    private KnowledgeBase knowledgeBase;
    
    public DroolsImplementationProvider(RuntimeComponent component, DroolsImplementation implementation) {
        this.component = component;
        this.implementation = implementation;
    }

    public boolean supportsOneWayInvocation() {
        return false;
    }

    public Invoker createInvoker(RuntimeComponentService service, Operation operation) {
        return new DroolsInvoker(operation, knowledgeBase);
    }

    public void start() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for (Resource resource : implementation.getResources()) {
            kbuilder.add(ResourceFactory.newUrlResource(resource.getLocationURL()), ResourceType.getResourceType(resource.getType()));
        }
        
        // TODO: do something with this!
        System.out.println(kbuilder.getErrors());
        
        knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    }

    public void stop() {
        knowledgeBase = null;
    }
}
