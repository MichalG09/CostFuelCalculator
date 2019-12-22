package pl.mgrzech.costfuel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pl.mgrzech.costfuel.models.Car;

public class CarDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 30;
    private static final String DATABASE_NAME = "databaseCostFuel";
    private static final String TABLE_CARS = "cars";
    private static final String KEY_ID = "id";
    private static final String KEY_MARK = "carMark";
    private static final String KEY_MODEL = "carModel";
    private static final String KEY_FUEL_CONSUMPTION_FIRST = "carConsumptionFirst";
    private static final String KEY_COST_FIRST = "carCostFirst";
    private static final String KEY_FUEL_CONSUMPTION_SECOND = "carConsumptionSecond";
    private static final String KEY_COST_SECOND = "carCostSecond";
    private static final String KEY_PERIOD_CALC = "periodCalc";
    private static final String KEY_FUEL_TYPE = "carFuelType";

    public CarDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " + KEY_MARK + " TEXT, " +
                KEY_MODEL + " TEXT, " + KEY_FUEL_CONSUMPTION_FIRST + " TEXT, " +
                KEY_COST_FIRST + " TEXT, " + KEY_FUEL_CONSUMPTION_SECOND + " TEXT, " +
                KEY_COST_SECOND + " TEXT, " + KEY_FUEL_TYPE + " TEXT, " +
                KEY_PERIOD_CALC + " TEXT " + ")";
        db.execSQL(CREATE_CARS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        onCreate(db);
    }

    /**
     * Method saves car in database.
     * Metoda zapisuje samochód w bazie danych.
     * @param car car for add to database
     */
    public void addCar(Car car){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MARK, car.getMark());
        contentValues.put(KEY_MODEL, car.getModel());
        contentValues.put(KEY_FUEL_CONSUMPTION_FIRST, car.getAverageConsumptionFirstFuel());
        contentValues.put(KEY_COST_FIRST, car.getAverageConsumptionFirstFuel());
        contentValues.put(KEY_FUEL_CONSUMPTION_SECOND, car.getAverageConsumptionSecondFuel());
        contentValues.put(KEY_COST_SECOND, car.getAverageCostSecondFuel());
        contentValues.put(KEY_FUEL_TYPE, car.getFuelType());
        contentValues.put(KEY_PERIOD_CALC, car.getPeriodTimeForCalculation());

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

        Cursor cursor = db.query(TABLE_CARS, new String[]{KEY_ID, KEY_MARK, KEY_MODEL, KEY_FUEL_CONSUMPTION_FIRST, KEY_COST_FIRST, KEY_FUEL_CONSUMPTION_SECOND, KEY_COST_SECOND, KEY_FUEL_TYPE, KEY_PERIOD_CALC},
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
                car.setAverageConsumptionFirstFuel(Double.parseDouble(cursor.getString(5)));
                car.setAverageCostFirstFuel(Double.parseDouble(cursor.getString(6)));
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
        contentValues.put(KEY_MARK, car.getMark());
        contentValues.put(KEY_MODEL, car.getModel());
        contentValues.put(KEY_FUEL_CONSUMPTION_FIRST, String.valueOf(car.getAverageConsumptionFirstFuel()));
        contentValues.put(KEY_COST_FIRST, String.valueOf(car.getAverageCostFirstFuel()));
        contentValues.put(KEY_FUEL_CONSUMPTION_SECOND, String.valueOf(car.getAverageConsumptionSecondFuel()));
        contentValues.put(KEY_COST_SECOND, String.valueOf(car.getAverageCostSecondFuel()));
        contentValues.put(KEY_FUEL_TYPE, car.getFuelType());
        contentValues.put(KEY_PERIOD_CALC, String.valueOf(car.getPeriodTimeForCalculation()));

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
     * Methods clears all data in car database.
     * Metoda czyśic całą bazę danych samochodów
     */
    public void clearDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CARS;
        db.execSQL(query);
    }

    /**
     * Method returns car searched by Mark from database. Used only in test.
     * Metoda zwraca samochód wyszukany przez model z bazy danych. Uzyte tylko przy testach.
     * @param mark mark of searching car
     * @return car
     */
    public Car getCarByMark(String mark){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_CARS, new String[]{KEY_ID, KEY_MARK, KEY_MODEL, KEY_FUEL_CONSUMPTION_FIRST, KEY_COST_FIRST, KEY_FUEL_CONSUMPTION_SECOND, KEY_COST_SECOND, KEY_FUEL_TYPE, KEY_PERIOD_CALC},
                KEY_MARK + "=?", new String[]{mark}, null, null, null, null);

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
}
