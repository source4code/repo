package info.source4code.soap.http.cxf;

import info.source4code.wsdl.helloworld.Greeting;
import info.source4code.wsdl.helloworld.HelloWorldPort;
import info.source4code.wsdl.helloworld.ObjectFactory;
import info.source4code.wsdl.helloworld.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldImpl implements HelloWorldPort {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HelloWorldImpl.class);

    @Override
    public Greeting sayHello(Person person) {

        String firstName = person.getFirstName();
        LOGGER.debug("firstName={}", firstName);
        String lasttName = person.getLastName();
        LOGGER.debug("lastName={}", lasttName);

        ObjectFactory factory = new ObjectFactory();
        Greeting response = factory.createGreeting();

        String greeting = "Hello " + firstName + " " + lasttName + "!";
        LOGGER.info("greeting={}", greeting);

        response.setText(greeting);
        return response;
    }
}
