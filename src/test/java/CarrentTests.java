import carrent.models.Vehicle;
import carrent.services.VehicleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CarrentTests {

    private VehicleValidator validator;
    private Vehicle validVehicle;

    @BeforeEach
      void setUp() {
        validator = new VehicleValidator();
        validVehicle = new Vehicle();
        validVehicle.setBrand("Ford");
        validVehicle.setModel("Raptor");
        validVehicle.setYear(2023);
        validVehicle.setPlate("WA 12345");
        validVehicle.setCategory("Diesel");
    }

    @Test
    void testValidBaseVehicle() {
        assertDoesNotThrow(() -> validator.validate(validVehicle, null));
    }

    @Test
    void testInvalidBrand() {
        validVehicle.setBrand("A");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(validVehicle, null));
        assertTrue(exception.getMessage().contains("Marka"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1899, 2030})
    void testInvalidYear(int year) {
        validVehicle.setYear(year);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(validVehicle, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB", "TO-LONG-PLATE-123", "WA-123"})
    void testInvalidPlate(String plate) {
        validVehicle.setPlate(plate);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(validVehicle, null));
    }

    @Test
    void testMissingAttribute() {
        Map<String, Object> required = Map.of("???", "STRING");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validate(validVehicle, required));
        assertEquals("Brak atrybutu: ???", exception.getMessage());
    }
}