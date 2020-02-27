package pl.mgrzech.costfuel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class Database extends SQLiteOpenHelper {

    //41
    private static final int DATABASE_VERSION = 41;
    private static final String DATABASE_NAME = "databaseCostFuel";
    private static final String TABLE_CARS = "cars";
    private static final String TABLE_FUELS = "fuels";
    private static final String KEY_ID = "id";
    private static final String CAR_KEY_MARK = "carMark";
    private static final String CAR_KEY_MODEL = "carModel";
    private static final String CAR_KEY_FUEL_CONSUMPTION_FIRST = "carConsumptionFirst";
    private static final String CAR_KEY_COST_FIRST = "carCostFirst";
    private static final String CAR_KEY_FUEL_CONSUMPTION_SECOND = "carConsumptionSecond";
    private static final String CAR_KEY_COST_SECOND = "carCostSecond";
    private static final String CAR_KEY_PERIOD_CALC = "periodCalc";
    private static final String CAR_KEY_FUEL_TYPE = "carFuelType";
    private static final String FUEL_KEY_FUEL_TYPE = "fuelType";
    private static final String FUEL_KEY_DATE = "date";
    private static final String FUEL_KEY_COST = "cost";
    private static final String FUEL_KEY_QUANTITY = "qunatity";
    private static final String FUEL_KEY_MILEAGE = "mileage";
    private static final String FUEL_KEY_CAR_ID = "carId";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " + CAR_KEY_MARK + " TEXT, " +
                CAR_KEY_MODEL + " TEXT, " + CAR_KEY_FUEL_CONSUMPTION_FIRST + " TEXT, " +
                CAR_KEY_COST_FIRST + " TEXT, " + CAR_KEY_FUEL_CONSUMPTION_SECOND + " TEXT, " +
                CAR_KEY_COST_SECOND + " TEXT, " + CAR_KEY_FUEL_TYPE + " TEXT, " +
                CAR_KEY_PERIOD_CALC + " TEXT " + ")";
        db.execSQL(CREATE_CARS_TABLE);
        String CREATE_FUELS_TABLE = "CREATE TABLE " + TABLE_FUELS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " + FUEL_KEY_FUEL_TYPE + " TEXT, " +
                FUEL_KEY_DATE + " TEXT, " + FUEL_KEY_COST + " INTEGER, " +
                FUEL_KEY_QUANTITY + " INTEGER ," + FUEL_KEY_MILEAGE + " INTEGER ," + FUEL_KEY_CAR_ID + " INTEGER " + ")";
        db.execSQL(CREATE_FUELS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUELS);
        onCreate(db);
    }

    /**
     * to delete
     */
    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUELS);
    }

    /**
     * Method saves car in database.
     * Metoda zapisuje samochód w bazie danych.
     * @param car car for add to database
     */
    public void addCar(Car car){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAR_KEY_MARK, car.getMark());
        contentValues.put(CAR_KEY_MODEL, car.getModel());
        contentValues.put(CAR_KEY_FUEL_CONSUMPTION_FIRST, car.getAverageConsumptionFirstFuel());
        contentValues.put(CAR_KEY_COST_FIRST, car.getAverageConsumptionFirstFuel());
        contentValues.put(CAR_KEY_FUEL_CONSUMPTION_SECOND, car.getAverageConsumptionSecondFuel());
        contentValues.put(CAR_KEY_COST_SECOND, car.getAverageCostSecondFuel());
        contentValues.put(CAR_KEY_FUEL_TYPE, car.getFuelType());
        contentValues.put(CAR_KEY_PERIOD_CALC, car.getPeriodTimeForCalculation());

        db.insert(TABLE_CARS, null, contentValues);
    }

    /**
     * Method returns car searched by Id from database.
     * Metoda zwraca samochód wyszukany przez Id z bazy danych.
     * @param id car id for search in database
     * @return car
     */
    public Car getCarById(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_CARS, new String[]{KEY_ID, CAR_KEY_MARK, CAR_KEY_MODEL, CAR_KEY_FUEL_CONSUMPTION_FIRST, CAR_KEY_COST_FIRST,
                        CAR_KEY_FUEL_CONSUMPTION_SECOND, CAR_KEY_COST_SECOND, CAR_KEY_FUEL_TYPE, CAR_KEY_PERIOD_CALC},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Car car = new Car(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)), Double.parseDouble(cursor.getString(5)),
                Double.parseDouble(cursor.getString(6)), cursor.getString(7),
                cursor.getString(8));
        return car;
    }

    /**
     * Method returns all car from database.
     * Metoda zwraca wszystkie samochody z bazy danych.
     * @return list all car from database
     */
    public List<Car> getAllCars(){
        List<Car> result = new ArrayList<>();

        String query  = "SELECT * FROM " + TABLE_CARS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Car car = new Car();
                car.setId(Integer.parseInt(cursor.getString(0)));
                car.setMark(cursor.getString(1));
                car.setModel(cursor.getString(2));
                car.setAverageConsumptionFirstFuel(Double.parseDouble(cursor.getString(3)));
                car.setAverageCostFirstFuel(Double.parseDouble(cursor.getString(4)));
                car.setAverageConsumptionSecondFuel(Double.parseDouble(cursor.getString(5)));
                car.setAverageCostSecondFuel(Double.parseDouble(cursor.getString(6)));
                car.setFuelType(cursor.getString(7));
                car.setPeriodTimeForCalculation(cursor.getString(8));

                result.add(car);

            }while (cursor.moveToNext());
        }

        return result;
    }

    /**
     * Method updates car by Id in database.
     * Metoda aktualizuje samochód o danym Id w bazie danych.
     * @param car updated car for save in database
     * @return if correct save return 1
     */
    public int updateCar(Car car){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAR_KEY_MARK, car.getMark());
        contentValues.put(CAR_KEY_MODEL, car.getModel());
        contentValues.put(CAR_KEY_FUEL_CONSUMPTION_FIRST, String.valueOf(car.getAverageConsumptionFirstFuel()));
        contentValues.put(CAR_KEY_COST_FIRST, String.valueOf(car.getAverageCostFirstFuel()));
        contentValues.put(CAR_KEY_FUEL_CONSUMPTION_SECOND, String.valueOf(car.getAverageConsumptionSecondFuel()));
        contentValues.put(CAR_KEY_COST_SECOND, String.valueOf(car.getAverageCostSecondFuel()));
        contentValues.put(CAR_KEY_FUEL_TYPE, car.getFuelType());
        contentValues.put(CAR_KEY_PERIOD_CALC, String.valueOf(car.getPeriodTimeForCalculation()));

        return db.update(TABLE_CARS, contentValues, KEY_ID + "=?",
                new String[]{String.valueOf(car.getId())} );
    }

    /**
     * Methods deletes car by Id in database.
     * Meotda kasuje samochód o danym Id w bazie danych.
     * @param car car for delete
     * @return if correct save return 1
     */
    public int deleteCar(Car car){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CARS, KEY_ID + "=?", new String[]{String.valueOf(car.getId())});
    }

    /**
     * Method returns car searched by Mark from database. Used only in test.
     * Metoda zwraca samochód wyszukany przez model z bazy danych. Uzyte tylko przy testach.
     * @param mark mark of searching car
     * @return car
     */
    public Car getCarByMark(String mark){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_CARS, new String[]{KEY_ID, CAR_KEY_MARK, CAR_KEY_MODEL, CAR_KEY_FUEL_CONSUMPTION_FIRST, CAR_KEY_COST_FIRST, CAR_KEY_FUEL_CONSUMPTION_SECOND, CAR_KEY_COST_SECOND, CAR_KEY_FUEL_TYPE, CAR_KEY_PERIOD_CALC},
                CAR_KEY_MARK + "=?", new String[]{mark}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Car car = new Car(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)), Double.parseDouble(cursor.getString(5)),
                Double.parseDouble(cursor.getString(6)),cursor.getString(7),
                cursor.getString(8));
        return car;
    }

    /**
     * Method saves fuel in database.
     * Metoda zapisuje tankowanie w bazie danych.
     * @param fuel fuel for add to database
     */
    public void addFuel(Fuel fuel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FUEL_KEY_FUEL_TYPE, fuel.getFuelType());
        contentValues.put(FUEL_KEY_DATE, fuel.getDate());
        contentValues.put(FUEL_KEY_COST, fuel.getCost());
        contentValues.put(FUEL_KEY_QUANTITY, fuel.getQuantity());
        contentValues.put(FUEL_KEY_MILEAGE, fuel.getMileage());
        contentValues.put(FUEL_KEY_CAR_ID, fuel.getCarId());

        db.insert(TABLE_FUELS, null, contentValues);
    }

    /**
     * Methods return all fuels for one car.
     * Metoda zwraca wszystkie tankowania dla danego samochodu.
     * @param carId car ID
     * @return list all fuels for insert car Id
     */
    public List<Fuel> getAllFuelsForCarId(String carId){
        List<Fuel> result = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();
        try{
            Cursor cursor = db.query(TABLE_FUELS, new String[]{KEY_ID, FUEL_KEY_FUEL_TYPE, FUEL_KEY_DATE, FUEL_KEY_COST, FUEL_KEY_QUANTITY,
                            FUEL_KEY_MILEAGE, FUEL_KEY_CAR_ID}, FUEL_KEY_CAR_ID + "=?",
                    new String[]{carId}, null, null, null, null);

            if(cursor.moveToFirst()){
                do{
                    Fuel fuel = new Fuel();
                    fuel.setId(Integer.parseInt(cursor.getString(0)));
                    fuel.setFuelType(cursor.getString(1));
                    fuel.setDate(cursor.getString(2));
                    fuel.setCost(Double.parseDouble(cursor.getString(3)));
                    fuel.setQuantity(Double.parseDouble(cursor.getString(4)));
                    fuel.setMileage(Integer.parseInt(cursor.getString(5)));

                    result.add(fuel);

                }while (cursor.moveToNext());
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Methods return all fuels with correct data for calculation for one car.
     * Metoda zwraca wszystkie tankowania z poprawną datą do przeliczeń dla danego samochodu.
     * @param carId car Id
     * @return list fuel for insert car Id make in insert period time
     */
    public List<Fuel> getAllFuelsForCarIdInCalculationPeriod(String carId, int period) throws ParseException {
        List<Fuel> listAllFuels = getAllFuelsForCarId(carId);
        List<Fuel> result = new ArrayList<>();

        if(period != 0){
            Date now = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Date fuelDate;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, (period * -1));
            Date start = cal.getTime();
            for(Fuel fuel : listAllFuels){
                fuelDate = simpleDateFormat.parse(fuel.getDate());
                if(fuelDate.before(now) && fuelDate.after(start)){
                    result.add(fuel);
                }
            }
        }
        else{
            return listAllFuels;
        }
        return  result;
    }

    /**
     * Methods deletes fuel by Id in database.
     * Meotda kasuje tankowanie o danym Id w bazie danych.
     * @param fuel fuel for delete
     * @return if correct save return 1
     */
    public int deleteFuel(Fuel fuel){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_FUELS, KEY_ID + "=?", new String[]{String.valueOf(fuel.getId())});
    }

    /**
     * Methods clears all data in fuel database.
     * Metoda czyści całą bazę danych tankowań
     */
    public void clearDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_FUELS;
        db.execSQL(query);
        query = "DELETE FROM " + TABLE_FUELS;
        db.execSQL(query);
    }

    /**
     * Methods return number fuels for input type fuels
     * Metoda zwraca ilość tankowań konkrtnego rodzaju paliwa
     * @param carId carId
     * @param typeFuel type fuel
     * @return number of fuel (one fuel type) for insert car Id
     */
    public int calcNumberFuelsForTypeFuel(int carId, String typeFuel) {
        int result = 0;
        String sql = "SELECT count(" + FUEL_KEY_FUEL_TYPE + ") FROM " + TABLE_FUELS + " WHERE " + FUEL_KEY_CAR_ID + "= ? AND " + FUEL_KEY_FUEL_TYPE + "=?;";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(carId), typeFuel});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }

        return result;
    }

    /**
     * Method return car id for input fuel
     * Meotda zwraca car id dla danego tankowania
     * @param fuelId fuel ID
     * @return car Id for insert fuel Id
     */
    public String getCarIdForFuels(String fuelId){
        String carId = "";

        String sql = "SELECT " + FUEL_KEY_CAR_ID + " FROM " + TABLE_FUELS + " WHERE " + KEY_ID + "=?";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[] {fuelId});

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            carId = cursor.getString(0);
        }

        return carId;
    }

    /**
     * Methods delete all fuels for one car in database.
     * Meotda kasuje wszystkie tankowania dla danego samochoduw bazie danych.
     * @param carId car ID
     */
    public void deleteFuelsForCar(int carId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FUELS, FUEL_KEY_CAR_ID + "=?", new String[]{String.valueOf(carId)});
    }

    /**
     * Methods delete all one type fuels for one car in database.
     * Meotda kasuje wszystkie tankowania jednego rodzaju paliwa dla danego samochodu w bazie danych.
     * @param carId CarId
     */
    public void  deleteOneTypeFuelsForCar(int carId, String fuelType) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FUELS, KEY_ID + "=? AND " + FUEL_KEY_FUEL_TYPE + "=?", new String[]{String.valueOf(carId), fuelType});
    }

    /**
     * Method returns fuel searched by date and carId from database. Used only in test
     * Metoda zwraca tankowanie wyszukany przez date i carId z bazy danych. Używane tylko przy testach.
     * @param date date
     * @param carId carId
     * @return Fuel for insert CarId and insert date
     */
    public Fuel getFuelByDateAndCarId(String date, int carId) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_FUELS, new String[]{KEY_ID, FUEL_KEY_FUEL_TYPE, FUEL_KEY_DATE, FUEL_KEY_COST, FUEL_KEY_QUANTITY, FUEL_KEY_MILEAGE, FUEL_KEY_CAR_ID},
                FUEL_KEY_DATE + "=?" + " AND " + FUEL_KEY_CAR_ID + "=?", new String[]{date, String.valueOf(carId)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Fuel fuel = new Fuel(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)));
        return fuel;
    }

    public List<Fuel> getAllFuels(){
        List<Fuel> result = new ArrayList<>();

        String query  = "SELECT * FROM " + TABLE_FUELS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        try{
            if(cursor.moveToFirst()){
                do{
                    Fuel fuel = new Fuel();
                    fuel.setId(Integer.parseInt(cursor.getString(0)));
                    fuel.setFuelType(cursor.getString(1));
                    fuel.setDate(cursor.getString(2));
                    fuel.setCost(Double.parseDouble(cursor.getString(3)));
                    fuel.setQuantity(Double.parseDouble(cursor.getString(4)));
                    fuel.setMileage(Integer.parseInt(cursor.getString(5)));
                    fuel.setCarId(Integer.parseInt(cursor.getString(6)));

                    result.add(fuel);

                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
