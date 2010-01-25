package com.google.code.droolssca.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.tuscany.sca.assembly.AssemblyFactory;
import org.apache.tuscany.sca.assembly.Service;
import org.apache.tuscany.sca.contribution.Artifact;
import org.apache.tuscany.sca.contribution.ContributionFactory;
import org.apache.tuscany.sca.contribution.ModelFactoryExtensionPoint;
import org.apache.tuscany.sca.contribution.processor.BaseStAXArtifactProcessor;
import org.apache.tuscany.sca.contribution.processor.StAXArtifactProcessor;
import org.apache.tuscany.sca.contribution.resolver.ClassReference;
import org.apache.tuscany.sca.contribution.resolver.ModelResolver;
import org.apache.tuscany.sca.contribution.service.ContributionReadException;
import org.apache.tuscany.sca.contribution.service.ContributionResolveException;
import org.apache.tuscany.sca.contribution.service.ContributionWriteException;
import org.apache.tuscany.sca.interfacedef.InvalidInterfaceException;
import org.apache.tuscany.sca.interfacedef.java.JavaInterfaceContract;
import org.apache.tuscany.sca.interfacedef.java.JavaInterfaceFactory;
import org.apache.tuscany.sca.monitor.Monitor;
import org.apache.tuscany.sca.monitor.Problem;
import org.apache.tuscany.sca.monitor.Problem.Severity;
import org.apache.tuscany.sca.monitor.impl.ProblemImpl;

import com.google.code.droolssca.DroolsImplementation;
import com.google.code.droolssca.DroolsImplementationFactory;
import com.google.code.droolssca.Resource;

public class DroolsImplementationProcessor extends BaseStAXArtifactProcessor implements StAXArtifactProcessor<DroolsImplementation> {
    private static final QName IMPLEMENTATION_DROOLS = new QName("http://code.google.com/p/droolssca/", "implementation.drools");
    private static final QName RESOURCE = new QName("http://code.google.com/p/droolssca/", "resource");

    private final Monitor monitor;
    private final DroolsImplementationFactory implementationFactory;
    private final ContributionFactory contributionFactory;
    private final AssemblyFactory assemblyFactory;
    private final JavaInterfaceFactory javaFactory;
    
    public DroolsImplementationProcessor(ModelFactoryExtensionPoint modelFactories, Monitor monitor) {
        this.monitor = monitor;
        implementationFactory = modelFactories.getFactory(DroolsImplementationFactory.class);
        contributionFactory = modelFactories.getFactory(ContributionFactory.class);
        assemblyFactory = modelFactories.getFactory(AssemblyFactory.class);
        javaFactory = modelFactories.getFactory(JavaInterfaceFactory.class);
    }
    
    public QName getArtifactType() {
        return IMPLEMENTATION_DROOLS;
    }

    public Class<DroolsImplementation> getModelType() {
        return DroolsImplementation.class;
    }

    public DroolsImplementation read(XMLStreamReader reader) throws ContributionReadException, XMLStreamException {
        DroolsImplementation implementation = implementationFactory.createDroolsImplementation();
        implementation.setUnresolved(true);
        implementation.setName(reader.getAttributeValue("", "name"));
        implementation.setInterface(reader.getAttributeValue("", "interface"));
        while (nextChildElement(reader)) {
            reader.require(XMLStreamReader.START_ELEMENT, RESOURCE.getNamespaceURI(), RESOURCE.getLocalPart());
            Resource resource = new Resource();
            // TODO: more validation here
            resource.setLocation(reader.getAttributeValue("", "location"));
            resource.setType(reader.getAttributeValue("", "type"));
            reader.getElementText();
            implementation.getResources().add(resource);
        }
        return implementation;
    }

    public void resolve(DroolsImplementation implementation, ModelResolver resolver) throws ContributionResolveException {
        // TODO: review this; maybe each Resource should be resolved independently (i.e. extend Base??)
        for (Resource resource : implementation.getResources()) {
            Artifact artifact = contributionFactory.createArtifact();
            artifact.setURI(resource.getLocation());
            artifact = resolver.resolveModel(Artifact.class, artifact);
            if (artifact.getLocation() != null) {
                resource.setLocationURL(artifact.getLocation());
            } else {
                error("CouldNotLocateResource", resolver, resource.getLocation());
                return;
            }
        }
        implementation.setUnresolved(false);
        
        Service service = assemblyFactory.createService();
        JavaInterfaceContract interfaceContract = javaFactory.createJavaInterfaceContract();
        ClassReference classReference = new ClassReference(implementation.getInterface());
        classReference = resolver.resolveModel(ClassReference.class, classReference);
        Class<?> javaClass = classReference.getJavaClass();
        if (javaClass == null) {
            throw new RuntimeException(); // TODO
        }
        try {
            interfaceContract.setInterface(javaFactory.createJavaInterface(javaClass));
        } catch (InvalidInterfaceException e) {
            throw new RuntimeException(e); // TODO
        }
        service.setInterfaceContract(interfaceContract);
        
        // Set the name for the service
        service.setName(implementation.getName());
        implementation.getServices().add(service);
    }

    public void write(DroolsImplementation model, XMLStreamWriter writer) throws ContributionWriteException, XMLStreamException {
        // TODO Auto-generated method stub
        
    }
    
    private void error(String message, Object model, Object... messageParameters) {
        if (monitor != null) {
            Problem problem = new ProblemImpl(getClass().getName(), "impl-drools-validation-messages", Severity.ERROR, model, message, (Object[])messageParameters);
            monitor.problem(problem);
        }
    }
}
