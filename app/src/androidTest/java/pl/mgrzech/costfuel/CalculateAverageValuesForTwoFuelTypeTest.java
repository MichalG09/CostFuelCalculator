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
import java.util.Locale;

import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests check calculate average values (cost and fuel consuption per 100 km) for car with two type of fuel.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class CalculateAverageValuesForTwoFuelTypeTest {

    private static Car testingCar;
    private static Database database;
    private static Fuel fuel;
    private DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
    private Date now = new Date();

    /**
     * Methods prepare all necesary variables.
     */
    @BeforeClass
    public static void onStart() {
        Context mContext = ApplicationProvider.getApplicationContext();
        database = new Database(mContext);
        testingCar = new Car("TestTwoType", "Test", "PB + LPG", "Brak ograniczeń");
        database.addCar(testingCar);
    }

    /**
     * Test check calculated average values after save new car in database. Should be 0 fo all values.
     */
    @Test
    public void aCheckAverageValuesAfterCreateAndSaveNewCar(){
        testingCar = database.getCarByMark(testingCar.getMark());
        database.deleteFuelsForCar(testingCar.getId());
        assertTrue(testingCar.getAverageCostFirstFuel() == 0 && testingCar.getAverageCostFirstFuel() == 0
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check calculated average values after add one fuel for each type fuel.
     * Should be 0 for all values, because have to be 2 fuel (each type) in database fo one car.
     */
    @Test
    public void bCheckAveragesValuesAfterAddOneFuelEachTypeForCar(){
        fuel = new Fuel("19.08.2019", "PB", 250.0, 50.0, 1000 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("20.08.2019", "LPG", 60.0, 30.0, 1000 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageCostFirstFuel() == 0 && testingCar.getAverageCostFirstFuel() == 0
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check calculated average values after add two fuels one type.
     * Should calculated average values only for type fuel where was two fuels.
     */
    @Test
    public void cCheckAveragesValuesAfterAddTwoFuelsForOneTypeForCar(){
        fuel = new Fuel("21.08.2019", "PB", 250.0, 50.0, 1500 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 10 && testingCar.getAverageCostFirstFuel() == 50
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check calculated average values after add two fuels each type.
     */
    @Test
    public void dCheckAveragesValuesAfterAddTwoFuelsEachTypeForCar(){
        fuel = new Fuel("22.08.2019", "LPG", 60.0, 30.0, 1500 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 10 && testingCar.getAverageCostFirstFuel() == 50
                && testingCar.getAverageConsumptionSecondFuel() == 6 && testingCar.getAverageCostSecondFuel() == 12);
    }

    /**
     * Test check calculated average values after delete one fuels one type.
     * Should calculated only type fuel where are min two fuels.
     */
    @Test
    public void eCheckAveragesValuesAfterDeleteOneFuelsOneTypeForCar(){
        fuel = database.getFuelByDateAndCarId("21.08.2019", testingCar.getId());
        database.deleteFuel(fuel);
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 0 && testingCar.getAverageCostFirstFuel() == 0
                && testingCar.getAverageConsumptionSecondFuel() == 6 && testingCar.getAverageCostSecondFuel() == 12);
    }

    /**
     * Test check calculated average values after delete one fuels each type and in database be only one fuel each type.
     * Should be 0 for all values, because have to be 2 fuel (each type) in database fo one car.
     */
    @Test
    public void fCheckAveragesValuesAfterDeleteOneFuelsFromTwoForCar(){
        fuel = database.getFuelByDateAndCarId("22.08.2019", testingCar.getId());
        database.deleteFuel(fuel);
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 0 && testingCar.getAverageCostFirstFuel() == 0
                && testingCar.getAverageConsumptionSecondFuel() == 0 && testingCar.getAverageCostSecondFuel() == 0);
    }

    /**
     * Test check calculated average values after more fuel each type.
     */
    @Test
    public void gCheckAveragesValuesAfterAddThreeFuelsForCar(){
        fuel = new Fuel("20.08.2019", "PB", 250.0, 50.0, 1500 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("22.08.2019", "PB", 275.0, 35.0, 1900 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("20.08.2019", "LPG", 60.0, 28.0, 1500 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("22.08.2019", "LPG", 85.0, 40.0, 1900 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(decimalFormat.format(testingCar.getAverageCostFirstFuel()).equals("58.33")
                && decimalFormat.format(testingCar.getAverageConsumptionFirstFuel()).equals("9.44")
                && decimalFormat.format(testingCar.getAverageConsumptionSecondFuel()).equals("7.56")
                && decimalFormat.format(testingCar.getAverageCostSecondFuel()).equals("16.11"));
    }

    /**
     * Test check average values after add fuels for one type with incorect values.
     * Should calc only for type with correct fuels.
     */
    @Test
    public void hCheckAverageValuesAfterAddIncorrectFuel(){
        database.deleteFuelsForCar(testingCar.getId());
        fuel = new Fuel("24.08.2019", "PB", 275.0, 35.0, 1000 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "PB", 375.0, 45.0, 1500 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("24.08.2019", "LPG", 80, 35.0, 0 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "LPG", 90, 45.0, 0 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(decimalFormat.format(testingCar.getAverageCostFirstFuel()).equals("75.00")
                && decimalFormat.format(testingCar.getAverageConsumptionFirstFuel()).equals("9.00")
                && decimalFormat.format(testingCar.getAverageConsumptionSecondFuel()).equals("0.00")
                && decimalFormat.format(testingCar.getAverageCostSecondFuel()).equals("0.00"));
    }

    /**
     * Test check average values after add fuels with incorect values.
     * Shouls be -1 (information about incorrected values).
     */
    @Test
    public void iCheckAverageValuesAfterAddIncorrectFuel(){
        database.deleteFuelsForCar(testingCar.getId());
        fuel = new Fuel("24.08.2019", "PB", 275.0, 35.0, 0 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "PB", 375.0, 45.0, 0 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("24.08.2019", "LPG", 80, 35.0, 0 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel("28.08.2019", "LPG", 90, 45.0, 0 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == -1 && testingCar.getAverageCostFirstFuel() == -1
                && testingCar.getAverageConsumptionSecondFuel() == -1 && testingCar.getAverageCostSecondFuel() == -1);
    }

    /**
     * Test check average values after change period time for calculation
     * Should change number of fuels to calculation
     */
    @Test
    public void jCheckAverageValuesAfterChangePeriodTime(){
        database.deleteFuelsForCar(testingCar.getId());
        testingCar.setPeriodTimeForCalculation("1 miesiąc");
        database.updateCar(testingCar);
        fuel = new Fuel(getEarlierDate(now, 0, 0), "PB", 275.0, 35.0, 300 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel(getEarlierDate(now, 0, 1), "PB", 375.0, 45.0, 200 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel(getEarlierDate(now, 1, 2), "PB", 475.0, 55.0, 100 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel(getEarlierDate(now, 1, 2), "PB", 95.0, 15.0, 5 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel(getEarlierDate(now, 0, 0), "LPG", 80, 35.0, 1000 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel(getEarlierDate(now, 0, 1), "LPG", 90, 45.0, 900 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel(getEarlierDate(now, 1, 2), "LPG", 70, 55.0, 800 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        fuel = new Fuel(getEarlierDate(now, 3, 3), "LPG", 100, 15.0, 600 );
        database.addFuel(fuel, String.valueOf(testingCar.getId()));
        testingCar = CalculateAvarageFuelAndCost.recarkulate(database, testingCar);
        database.updateCar(testingCar);
        assertTrue(testingCar.getAverageConsumptionFirstFuel() == 35 && testingCar.getAverageCostFirstFuel() == 275
                && testingCar.getAverageConsumptionSecondFuel() == 35 && testingCar.getAverageCostSecondFuel() == 80);
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