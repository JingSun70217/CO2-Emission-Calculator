import java.util.*;
import java.math.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

// To get the input value and unit.
class CO2Output
{
    public Float value;
    public String unit;
}

class CO2Calculator
{
      private HashMap<String, Integer> dict_tm = new HashMap<String, Integer>();
      
      CO2Calculator()
      {
        dict_tm.put("small-diesel-car", 142);
        dict_tm.put("small-petrol-car", 154);
        dict_tm.put("small-plugin-hybrid-car", 73);
        dict_tm.put("small-electric-car", 50);
        dict_tm.put("medium-diesel-car", 171);
        dict_tm.put("medium-petrol-car", 192);
        dict_tm.put("medium-plugin-hybrid-car", 110);
        dict_tm.put("medium-electric-car", 58);
        dict_tm.put("large-diesel-car", 209);
        dict_tm.put("large-petrol-car", 282);
        dict_tm.put("large-plugin-hybrid-car", 126);
        dict_tm.put("large-electric-car", 73);
        dict_tm.put("bus", 27);
        dict_tm.put("train", 6);
      }
      
      public CO2Output calculate(String transportation_method,
                             Float distance,
                             String unit_of_distance, 
                             String unit_of_output,
                             Boolean is_unit_of_output_given)
      {
            Integer faktor_of_distance = 1;
            Integer faktor_of_output= 1;
            
            if(unit_of_distance.equals("m"))
            {
                faktor_of_distance = 1000;
            }
            else if(unit_of_distance.equals("km"))
            {
                faktor_of_distance = 1;
            }
            else
            {
                System.out.println("Unknown distance unit");
                System.exit(1);
            }
            
            if(unit_of_output.equals("g"))
            {
                faktor_of_output = 1;
            }
            else if(unit_of_output.equals("kg"))
            {
                faktor_of_output = 1000;
            }
            else
            {
                System.out.println("Unknown output unit");
                System.exit(1);
            }
            
            CO2Output output = new CO2Output();
            Integer transport_unit_co2 = dict_tm.get(transportation_method);
            Float value = (transport_unit_co2*distance/faktor_of_distance)/faktor_of_output;

            // Transform the result to kg, if value is bigger than 1000g
            if(value >= 1000 && !is_unit_of_output_given && unit_of_output.equals("g"))
            {
                value = value/1000.f;
                unit_of_output = "kg";
            }
            
            value = Math.round(value * 10.0f)/10.0f;
            output.value = value;
            output.unit = unit_of_output;

            return output;
      }
}

class Main
{   
  public static void main(String[] args) throws Exception
  {   
    ArgumentParser parser = ArgumentParsers.newFor("co2-calculator").build()
            .defaultHelp(true)
            .description("Calculate co2 by given vehicle type and distance.");
    
    parser.addArgument("--transportation-method")
            .choices("small-diesel-car",
                     "small-petrol-car",
                     "small-plugin-hybrid-car",
                     "small-electric-car",
                     "medium-diesel-car",
                     "medium-petrol-car", 
                     "medium-plugin-hybrid-car",
                     "medium-electric-car",
                     "large-diesel-car",
                     "large-petrol-car",
                     "large-plugin-hybrid-car",
                     "large-electric-car",
                     "bus",
                     "train").required(true).help("Please select one transportation method");
    parser.addArgument("--distance").help("Please give a distance").required(true);  
    parser.addArgument("--unit-of-distance").choices("m", "km").setDefault("km").help("Please give the unit of distance"); 
    parser.addArgument("--output").choices("g", "kg").help("Please give the wished unit of output"); 
    CO2Calculator co2_calculator = new CO2Calculator();
    
    Namespace res;
    try {
            res = parser.parseArgs(args);
            String transportation_method = res.getString("transportation_method");
            Float distance = new Float(res.getString("distance"));
            String unit_of_distance = res.getString("unit_of_distance");
            String unit_of_output = "g"; 
            Boolean is_unit_of_output_given = false; 
            
            // Check whether unit of output is given
            if(res.getAttrs().containsKey("output") && res.getString("output") != null) 
            {
                is_unit_of_output_given = true;
                unit_of_output = res.getString("output");
            }
 
            CO2Output result = co2_calculator.calculate(transportation_method, distance, unit_of_distance, unit_of_output, is_unit_of_output_given);
            System.out.println("Your trip caused " + result.value + result.unit + " of CO2-equivalent");
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
  }
}