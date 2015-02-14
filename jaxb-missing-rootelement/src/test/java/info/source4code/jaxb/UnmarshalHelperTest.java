package info.source4code.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import info.source4code.jaxb.UnmarshalHelper;
import info.source4code.jaxb.model.Car;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnmarshalHelperTest {

    private static final Logger logger = LoggerFactory
            .getLogger(UnmarshalHelperTest.class);

    public static File file;

    @BeforeClass
    public static void setUpBeforeClass() {
        file = new File("./src/test/resources/xml/golf.xml");
    }

    @Test
    public void testUnmarshalError() {
        try {
            UnmarshalHelper.unmarshalError(file);
            fail("no exception thrown");
        } catch (Exception e) {
            logger.error("MarshalException", e);
            assertTrue(e.toString().contains("unexpected element (uri:"));
        }
    }

    @Test
    public void testUnmarshal() throws JAXBException {
        Car car = UnmarshalHelper.unmarshal(file);
        assertEquals(car.toString(),
                "Car [make=Golf, manufacturer=Volkswagen, id=DEF-456]");
    }
}
