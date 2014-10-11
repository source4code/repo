package info.source4code.jaxb;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import info.source4code.jaxb.MarshalHelper;
import info.source4code.jaxb.model.Car;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarshalHelperTest {

    private static final Logger logger = LoggerFactory
            .getLogger(MarshalHelperTest.class);

    public static Car car;

    @BeforeClass
    public static void setUpBeforeClass() {
        car = new Car();
        car.setMake("Passat");
        car.setManufacturer("Volkswagen");
        car.setId("ABC-123");
    }

    @Test
    public void testMarshalError() throws JAXBException {
        try {
            MarshalHelper.marshalError(car);
            fail("no exception thrown");
        } catch (MarshalException e) {
            logger.error("MarshalException", e);
            assertTrue(e.toString().contains(
                    "is missing an @XmlRootElement annotation"));
        }
    }

    @Test
    public void testMarshal() throws JAXBException {
        String xml = MarshalHelper.marshal(car);
        assertTrue(xml.contains("<?xml"));
        assertTrue(xml.contains("ABC-123"));
        assertTrue(xml.contains("Passat"));
        assertTrue(xml.contains("Volkswagen"));
    }
}
