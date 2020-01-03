package pl.mgrzech.costfuel;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;

import static org.junit.Assert.assertArrayEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class EditTypeFuelTest {

    private String oldTypeFuel;
    private String newTypeFuel;
    private String[] result;
    private String[] expectedResult;
    private CalculateAvarageFuelAndCost calculateAvarageFuelAndCost = new CalculateAvarageFuelAndCost();

    @Test
    public void aChangeFuel(){
        oldTypeFuel = "PB";
        newTypeFuel = "ON";
        expectedResult = new String[]{"PB"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
        public void bChangeFuel() {
        oldTypeFuel = "PB";
        newTypeFuel = "ON + AddBlue";
        expectedResult = new String[]{"PB"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void dChangeFuel(){
        oldTypeFuel = "PB + LPG";
        newTypeFuel = "ON";
        expectedResult = new String[]{"PB", "LPG"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void eChangeFuel(){
        oldTypeFuel = "PB + LPG";
        newTypeFuel = "ON + AddBlue";
        expectedResult = new String[]{"PB", "LPG"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void fChangeFuel(){
        oldTypeFuel = "PB + LPG";
        newTypeFuel = "PB";
        expectedResult = new String[]{"LPG"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void hChangeFuel(){
        oldTypeFuel = "ON";
        newTypeFuel = "PB";
        expectedResult = new String[]{"ON"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void iChangeFuel(){
        oldTypeFuel = "ON";
        newTypeFuel = "PB + LPG";
        expectedResult = new String[]{"ON"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void jChangeFuel(){
        oldTypeFuel = "ON + AddBlue";
        newTypeFuel = "PB + LPG";
        expectedResult = new String[]{"ON", "AddBlue"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void kChangeFuel(){
        oldTypeFuel = "ON + AddBlue";
        newTypeFuel = "PB";
        expectedResult = new String[]{"ON", "AddBlue"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void lChangeFuel(){
        oldTypeFuel = "ON + AddBlue";
        newTypeFuel = "ON";
        expectedResult = new String[]{"AddBlue"};
        result = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuel, newTypeFuel);
        assertArrayEquals(expectedResult, result);
    }
}