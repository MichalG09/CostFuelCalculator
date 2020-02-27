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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.database.DatabaseSingleton;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

import static org.junit.Assert.assertTrue;

/**
 * Tests check calculate average values (cost and fuel consuption per 100 km) for car with one type of fuel.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class CalculateAverageValuesForOneFuelTypeTest {

    private static Car testingCar;
    private static Database database;
    private static Fuel fuel;
    private CalculateAvarageFuelAndCost calculateAvarageFuelAndCost = new CalculateAvarageFuelAndCost();
    private DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private Date now = new Date();

    /**
     * Methods prepare all necesary variables.
     */
    @BeforeClass
    public static void onStart() {
        Context mContext = ApplicationProvider.getApplicationContext();
        database = DatabaseSingleton.getInstance(mContext);
        testingCar = new Car("TestOneType", "Test", "PB", "Brak ograniczeń");
        database.addCar(testingCar);
    }

    /**
     * Test check calculated average values after save new car in database. Should be 0 fo all values.
     */
    @Test
    public void aCheckAverageValuesAfterCreateAndSaveNewCar(){
        testingCar = database.getCarByMark(testingCar.getMark());
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 0 && testingCar.getAverageCostFirstFuel() == 0
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check calculated average values after add one fuel.
     * Should be 0 for all values, because have to be 2 fuel in database fo one car.
     */
    @Test
    public void bCheckAveragesValuesAfterAddOneFuelForCar(){
        fuel = new Fuel(getEarlierDate(now,0,0), "PB", 250.0, 50.0, 1000 , testingCar.getId());
        database.addFuel(fuel);
        testingCar = calculateAvarageFuelAndCost.recalculate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 0 && testingCar.getAverageCostFirstFuel() == 0
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check calculated average values after add two fuels.
     */
    @Test
    public void cCheckAveragesValuesAfterAddTwoFuelsForCar(){
        fuel = new Fuel(getEarlierDate(now,0,1), "PB", 250.0, 50.0, 1500, testingCar.getId());
        database.addFuel(fuel);
        testingCar = calculateAvarageFuelAndCost.recalculate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 10 && testingCar.getAverageCostFirstFuel() == 50
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check calculated average values after delete one fuels and in database be only one fuel.
     * Should be 0 for all values, because have to be 2 fuel in database fo one car.
     */
    @Test
    public void dCheckAveragesValuesAfterDeleteOneFuelsFromTwoForCar(){
        fuel = database.getFuelByDateAndCarId(getEarlierDate(now,0,1), testingCar.getId());
        database.deleteFuel(fuel);
        testingCar = calculateAvarageFuelAndCost.recalculate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 0 && testingCar.getAverageCostFirstFuel() == 0
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test checks calculated averages values after add two fuels one type fuel
     */
    @Test
    public void eCheckAveragesValuesAfterAddThreeFuelsForCar(){
        fuel = new Fuel(getEarlierDate(now,0,3), "PB", 250.0, 50.0, 1500, testingCar.getId() );
        database.addFuel(fuel);
        fuel = new Fuel(getEarlierDate(now,0,2), "PB", 275.0, 35.0, 1900, testingCar.getId() );
        database.addFuel(fuel);
        testingCar = calculateAvarageFuelAndCost.recalculate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(decimalFormat.format(testingCar.getAverageCostFirstFuel()).equals("58.33")
                && decimalFormat.format(testingCar.getAverageConsumptionFirstFuel()).equals("9.44")
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check average values after add fuels with incorect values.
     * Shouls be -1 (information about incorrected values).
     */
    @Test
    public void fCheckAverageValuesAfterAddIncorrectFuel(){
        database.deleteFuelsForCar(testingCar.getId());
        fuel = new Fuel(getEarlierDate(now,0,0), "PB", 275.0, 35.0, 0, testingCar.getId() );
        database.addFuel(fuel);
        fuel = new Fuel(getEarlierDate(now,0,2), "PB", 375.0, 45.0, 0, testingCar.getId() );
        database.addFuel(fuel);
        testingCar = calculateAvarageFuelAndCost.recalculate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == -1 && testingCar.getAverageCostFirstFuel() == -1
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check avarage values after change period time for calculation
     * Should take 2 fuels for calculation
     */
    @Test
    public void gCheckAverageValuesAfterAddPeriodTimeForCalc(){
        testingCar.setPeriodTimeForCalculation("1 miesiąc");
        database.updateCar(testingCar);
        database.deleteFuelsForCar(testingCar.getId());
        fuel = new Fuel(getEarlierDate(now,0,0), "PB", 275.0, 35.0, 300, testingCar.getId() );
        database.addFuel(fuel);
        fuel = new Fuel(getEarlierDate(now,0,1), "PB", 375.0, 45.0, 200, testingCar.getId() );
        database.addFuel(fuel);
        fuel = new Fuel(getEarlierDate(now,1,1), "PB", 475.0, 38.0, 100, testingCar.getId() );
        database.addFuel(fuel);
        fuel = new Fuel(getEarlierDate(now,2,2), "PB", 175.0, 28.0, 500, testingCar.getId() );
        database.addFuel(fuel);
        testingCar = calculateAvarageFuelAndCost.recalculate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 35 && testingCar.getAverageCostFirstFuel() == 275
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Meothods create date earlier for insert data
     * @param date date
     * @param month  number month for earlier date
     * @param days number days for earlier date
     * @return earlier date
     */
    private String getEarlierDate(Date date, int month, int days) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        if(month != 0){
            cal.add(Calendar.MONTH, (month * -1));
        }
        if(days != 0){
            cal.add(Calendar.DAY_OF_MONTH, (days * -1));
        }
        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * Methods clean all values added during tests.
     */
    @AfterClass
    public static void onEnd(){
        database.deleteFuelsForCar(testingCar.getId());
        database.deleteCar(testingCar);
    }
}