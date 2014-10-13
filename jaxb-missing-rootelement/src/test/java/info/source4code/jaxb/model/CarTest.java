package info.source4code.jaxb.model;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class CarTest {

    private static Car car;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        car = new Car();
    }

    @Test
    public void testGetMake() {
        car.setMake("Passat");
        assertEquals("Passat", car.getMake());
    }

    @Test
    public void testGetManufacturer() {
        car.setManufacturer("Volkswagen");
        assertEquals("Volkswagen", car.getManufacturer());
    }

    @Test
    public void testGetId() {
        car.setId("ABC-123");
        assertEquals("ABC-123", car.getId());
    }

    @Test
    public void testToString() {
        car.setMake("Passat");
        car.setManufacturer("Volkswagen");
        car.setId("ABC-123");

        assertEquals("Car [make=Passat, manufacturer=Volkswagen, id=ABC-123]",
                car.toString());
    }
}
