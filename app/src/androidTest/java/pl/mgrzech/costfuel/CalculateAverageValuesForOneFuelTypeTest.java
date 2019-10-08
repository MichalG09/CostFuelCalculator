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
 * Tests check calculate average values (cost and fuel consuption per 100 km) for car with one type of fuel.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class CalculateAverageValuesForOneFuelTypeTest {

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
        testingCar = new Car("TestOneType", "Test", "PB");
        carDatabase.addCar(testingCar);
    }

    /**
     * Test check calculated average values after save new car in database. Should be 0 fo all values.
     */
    @Test
    public void aCheckAverageValuesAfterCreateAndSaveNewCar(){
        testingCar = carDatabase.getCarByName(testingCar.getMark());
        assertEquals(true, testingCar.getAvarageCost() == 0 && testingCar.getAvarageFuelConsumption() == 0);
    }

    /**
     * Test check calculated average values after add one fuel.
     * Should be 0 for all values, because have to be 2 fuel in database fo one car.
     */
    @Test
    public void bCheckAveragesValuesAfterAddOneFuelForCar(){
        fuel = new Fuel("19.08.2019", "PB", 250.0, 50.0, 1000 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 0 && testingCar.getAvarageFuelConsumption() == 0);
    }

    /**
     * Test check calculated average values after add two fuels.
     */
    @Test
    public void cCheckAveragesValuesAfterAddTwoFuelsForCar(){
        fuel = new Fuel("20.08.2019", "PB", 250.0, 50.0, 1500 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 50 && testingCar.getAvarageFuelConsumption() == 10);
    }

    /**
     * Test check calculated average values after delete one fuels and in database be only one fuel.
     * Should be 0 for all values, because have to be 2 fuel in database fo one car.
     */
    @Test
    public void dCheckAveragesValuesAfterDeleteOneFuelsFromTwoForCar(){
        fuel = fuelDatabase.getFuelByDateAndCarId("20.08.2019", testingCar.getId());
        fuelDatabase.deleteFuel(fuel);
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, testingCar.getAvarageCost() == 0 && testingCar.getAvarageFuelConsumption() == 0);
    }

    @Test
    public void eCheckAveragesValuesAfterAddThreeFuelsForCar(){
        fuel = new Fuel("20.08.2019", "PB", 250.0, 50.0, 1500 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("22.08.2019", "PB", 275.0, 35.0, 1900 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, testingCar);
        carDatabase.updateCar(testingCar);
        assertEquals(true, decimalFormat.format(testingCar.getAvarageCost()).equals("58.33") && decimalFormat.format(testingCar.getAvarageFuelConsumption()).equals("9.44"));
    }

    /**
     * Test check average values after add fuels with incorect values.
     * Shouls be -1 (information about incorrected values).
     */
    @Test
    public void fCheckAverageValuesAfterAddIncorrectFuel(){
        fuelDatabase.deleteFuelsForCar(testingCar.getId());
        fuel = new Fuel("24.08.2019", "PB", 275.0, 35.0, 0 );
        fuelDatabase.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "PB", 375.0, 45.0, 0 );
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