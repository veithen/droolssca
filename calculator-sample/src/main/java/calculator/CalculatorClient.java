package calculator;

import org.apache.tuscany.sca.node.SCAClient;
import org.apache.tuscany.sca.node.SCANode;
import org.apache.tuscany.sca.node.SCANodeFactory;

public class CalculatorClient {
    public static void main(String[] args) throws Exception {
        SCANodeFactory factory = SCANodeFactory.newInstance();
        SCANode node = factory.createSCANodeFromClassLoader("Calculator.composite", CalculatorClient.class.getClassLoader());
        node.start();
        
        Calculator calculator = ((SCAClient)node).getService(Calculator.class, "CalculatorServiceComponent");
        
        AddRequest request = new AddRequest();
        request.setValue1(1.3);
        request.setValue2(-5.6);
        AddResponse response = calculator.add(request);
        System.out.println(response.getSum());

        node.stop();
    }
}
