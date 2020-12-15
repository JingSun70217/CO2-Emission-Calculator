import static org.junit.Assert.*;
import org.junit.Test;

public class TestCO2Calculator {
    @Test
    public void test_co2_calculator() {
        CO2Calculator co2_calculator = new CO2Calculator();
        
        // Test case 1
        String transportation_method = "medium-diesel-car";
        Float distance = 15.f;
        String unit_of_distance = "km"; // given
        String unit_of_output = "g"; // not given
        
        assertEquals(2.6, co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, false).value, 0.001);
        assertEquals("kg", co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, false).unit);
        
        // Test case 2
        transportation_method = "large-petrol-car";
        distance = 1800.5f;
        unit_of_distance = "km"; // default
        unit_of_output = "g"; // not given
        
        assertEquals(507.7, co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, false).value, 0.001);
        assertEquals("kg", co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, false).unit);
        
        // Test case 3
        transportation_method = "train";
        distance = 14500.f;
        unit_of_distance = "m"; // given
        unit_of_output = "g"; // not given
        
        assertEquals(87.f, co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, false).value, 0.001);
        assertEquals("g", co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, false).unit);
        
        // Test case 4
        transportation_method = "train";
        distance = 14500.f;
        unit_of_distance = "m"; // given
        unit_of_output = "kg"; // given
        
        assertEquals(0.1f, co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, true).value, 0.001);
        assertEquals("kg", co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, true).unit);
    }
}
