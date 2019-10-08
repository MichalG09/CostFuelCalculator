package pl.mgrzech.costfuel;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.text.DecimalFormat;

import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

import static org.junit.Assert.assertEquals;

/**
 * Tests check calculate average values (cost and fuel consuption per 100 km) for car with two type of fuel.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class CalculateAverageValuesForTwoFuelTypeTest {

    static Context mContext;
    static Car testingCar;
    static CarDatabase carDatabase;
    static FuelDatabase fuelDatabase;
    static Fuel fuel;
    private DecimalFormat decimalFormat = new DecimalFormat("##0.00");

    /**
     * Methods prepare all necesary variables.
     */
    @BeforeClass
    public static void onStart() {
        mContext = ApplicationProvider.getApplicationContext();
        carDatabase = new CarDatabase(mContext);
        fuelDatabase = new FuelDatabase(mContext);
        testingCar = new Car("TestTwoType", "Test", "PB + LPG");
        carDatabase.addCar(testingCar);
    }

    /**
     * Test check calculated average values after save new car in database. Should be 0 fo all values.
     */
    @Test
    public void aCheckAverageValuesAfterCreateAndSaveNewCar(){
        testingCar = carDatabase.getCarByName(testingCar.getMark());
        fuelDatabase.deleteFuelsForCar(testingCar.getId());
        assertEquals(true, testingCar.getAvarageCost() == 0 && testingCar.getAvarageFuelConsumption() == 0);
    }

    /**
     * Test check calculated average values after add one fuel for each type fuel.
     * Should be 0 for all values, because have to be 2 fuel (each type) in database fo one car.
     */
    @Test
    public void bCheckAveragesValuesAfterAddOneFuelEachTypeForCar(){
        fuel = new Fuel("19.08.2019", "PB", 250.0, 50.0, 1000 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("20.08.2019", "LPG", 60.0, 30.0, 1000 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 0 && testingCar.getAvarageFuelConsumption() == 0);
    }

    /**
     * Test check calculated average values after add two fuels one type.
     * Should calculated average values only for type fuel where was two fuels.
     */
    @Test
    public void cCheckAveragesValuesAfterAddTwoFuelsForOneTypeForCar(){
        fuel = new Fuel("21.08.2019", "PB", 250.0, 50.0, 1500 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 50 && testingCar.getAvarageFuelConsumption() == 10);
    }

    /**
     * Test check calculated average values after add two fuels each type.
     */
    @Test
    public void dCheckAveragesValuesAfterAddTwoFuelsEachTypeForCar(){
        fuel = new Fuel("22.08.2019", "LPG", 60.0, 30.0, 1500 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 62 && testingCar.getAvarageFuelConsumption() == 16);
    }

    /**
     * Test check calculated average values after delete one fuels one type.
     * Should calculated only type fuel where are min two fuels.
     */
    @Test
    public void eCheckAveragesValuesAfterDeleteOneFuelsOneTypeForCar(){
        fuel = fuelDatabase.getFuelByDateAndCarId("21.08.2019", testingCar.getId());
        fuelDatabase.deleteFuel(fuel);
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 12 && testingCar.getAvarageFuelConsumption() == 6);
    }

    /**
     * Test check calculated average values after delete one fuels each type and in database be only one fuel each type.
     * Should be 0 for all values, because have to be 2 fuel (each type) in database fo one car.
     */
    @Test
    public void fCheckAveragesValuesAfterDeleteOneFuelsFromTwoForCar(){
        fuel = fuelDatabase.getFuelByDateAndCarId("22.08.2019", testingCar.getId());
        fuelDatabase.deleteFuel(fuel);
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 0 && testingCar.getAvarageFuelConsumption() == 0);
    }

    /**
     * Test check calculated average values after more fuel each type.
     */
    @Test
    public void gCheckAveragesValuesAfterAddThreeFuelsForCar(){
        fuel = new Fuel("20.08.2019", "PB", 250.0, 50.0, 1500 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("22.08.2019", "PB", 275.0, 35.0, 1900 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("20.08.2019", "LPG", 60.0, 28.0, 1500 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("22.08.2019", "LPG", 85.0, 40.0, 1900 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, decimalFormat.format(testingCar.getAvarageCost()).equals("74.44") && testingCar.getAvarageFuelConsumption() == 17);
    }

    /**
     * Test check average values after add fuels for one type with incorect values.
     * Should calc only for type with correct fuels.
     */
    @Test
    public void hCheckAverageValuesAfterAddIncorrectFuel(){
        fuelDatabase.deleteFuelsForCar(testingCar.getId());
        fuel = new Fuel("24.08.2019", "PB", 275.0, 35.0, 1000 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "PB", 375.0, 45.0, 1500 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("24.08.2019", "LPG", 80, 35.0, 0 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "LPG", 90, 45.0, 0 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 75 && testingCar.getAvarageFuelConsumption() == 9);
    }

    /**
     * Test check average values after add fuels with incorect values.
     * Shouls be -1 (information about incorrected values).
     */
    @Test
    public void iCheckAverageValuesAfterAddIncorrectFuel(){
        fuelDatabase.deleteFuelsForCar(testingCar.getId());
        fuel = new Fuel("24.08.2019", "PB", 275.0, 35.0, 0 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "PB", 375.0, 45.0, 0 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("24.08.2019", "LPG", 80, 35.0, 0 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "LPG", 90, 45.0, 0 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == -1 && testingCar.getAvarageFuelConsumption() == -1);
    }

    /**
     * Methods clean all values added during tests.
     */
    @AfterClass
    public static void onEnd(){
        fuelDatabase.deleteFuelsForCar(testingCar.getId());
        carDatabase.deleteCar(testingCar);
    }
}