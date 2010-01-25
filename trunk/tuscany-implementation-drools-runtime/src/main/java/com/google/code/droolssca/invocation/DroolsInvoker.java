package com.google.code.droolssca.invocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.interfacedef.java.JavaInterface;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.invocation.Message;
import org.drools.KnowledgeBase;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.rule.QueryResults;

public class DroolsInvoker implements Invoker {
    private static final String RESULT_IDENTIFIER = "result";
    
    private final Operation operation;
    private final KnowledgeBase knowledgeBase;
    
    public DroolsInvoker(Operation operation, KnowledgeBase knowledgeBase) {
        this.operation = operation;
        this.knowledgeBase = knowledgeBase;
    }

    public Message invoke(Message msg) {
        if (operation.getInterface() instanceof JavaInterface) {
            List<Command<?>> commands = new ArrayList<Command<?>>();
            for (Object arg : msg.<Object[]>getBody()) {
                commands.add(CommandFactory.newInsert(arg));
            }
            commands.add(CommandFactory.newFireAllRules());
            // TODO: this is only useful if the method has a non void result!
            commands.add(CommandFactory.newQuery(RESULT_IDENTIFIER, operation.getName()));
            ExecutionResults results = knowledgeBase.newStatelessKnowledgeSession().execute(CommandFactory.newBatchExecution(commands));
            msg.setBody(((QueryResults)results.getValue(RESULT_IDENTIFIER)).iterator().next().get("response"));
        } else {
            msg.setFaultBody("Unsupported service contract"); // TODO: do we need to wrap this in an exception??
        }
        return msg;
    }
}
