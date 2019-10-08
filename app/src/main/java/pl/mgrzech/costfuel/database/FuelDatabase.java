package pl.mgrzech.costfuel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pl.mgrzech.costfuel.models.Fuel;

public class FuelDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "databaseCostFuel";
    private static final String TABLE_FUELS = "fuels";
    private static final String KEY_ID = "id";
    private static final String KEY_FUEL_TYPE = "fuelType";
    private static final String KEY_DATE = "date";
    private static final String KEY_COST = "cost";
    private static final String KEY_QUANTITY = "qunatity";
    private static final String KEY_MILEAGE = "mileage";
    private static final String KEY_CAR_ID = "carId";

    public FuelDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_FUELS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FUEL_TYPE + " TEXT, " +
                KEY_DATE + " TEXT, " + KEY_COST + " INTEGER, " +
                KEY_QUANTITY + " INTEGER ," + KEY_MILEAGE + " INTEGER ," + KEY_CAR_ID + " TEXT" + ")";
        db.execSQL(CREATE_CARS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUELS);
        onCreate(db);
    }

    /**
     * Method saves fuel in database.
     * Metoda zapisuje tankowanie w bazie danych.
     * @param fuel
     * @param carID
     */
    public void addFuel(Fuel fuel, String carID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FUEL_TYPE, fuel.getFuelType());
        contentValues.put(KEY_DATE, fuel.getDate());
        contentValues.put(KEY_COST, fuel.getCost());
        contentValues.put(KEY_QUANTITY, fuel.getQuantity());
        contentValues.put(KEY_MILEAGE, fuel.getMileage());
        contentValues.put(KEY_CAR_ID, carID);

        db.insert(TABLE_FUELS, null, contentValues);
    }

    /**
     * Method returns fuel searched by Id from database.
     * Metoda zwraca tankowanie wyszukany przez Id z bazy danych.
     * @param id
     * @return
     */
    public Fuel getFuelById(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_FUELS, new String[]{KEY_ID, KEY_FUEL_TYPE, KEY_DATE, KEY_COST, KEY_QUANTITY, KEY_MILEAGE, KEY_CAR_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

                Fuel fuel = new Fuel(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), Double.parseDouble(cursor.getString(3)),
                        Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
        return fuel;
    }

    /**
     * Methods return all fuels for one car.
     * Metoda zwraca wszystkie tankowania dla danego samochodu.
     * @param carId
     * @return
     */
    public List<Fuel> getAllFuelsForCarId(String carId){
        List<Fuel> result = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_FUELS, new String[]{KEY_ID, KEY_FUEL_TYPE, KEY_DATE, KEY_COST, KEY_QUANTITY, KEY_MILEAGE}, KEY_CAR_ID + "=?",
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

        return result;
    }

    /**
     * Method updates fuel by Id in database.
     * Metoda aktualizuje tankowanie o danym Id w bazie danych.
     * @param fuel
     * @return
     */
    public int updateFuel(Fuel fuel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FUEL_TYPE, fuel.getFuelType());
        contentValues.put(KEY_DATE, fuel.getDate());
        contentValues.put(KEY_COST, String.valueOf(fuel.getCost()));
        contentValues.put(KEY_QUANTITY, String.valueOf(fuel.getQuantity()));
        contentValues.put(KEY_MILEAGE, String.valueOf(fuel.getMileage()));

        return db.update(TABLE_FUELS, contentValues, KEY_ID + "=?",
                new String[]{String.valueOf(fuel.getId())} );
    }

    /**
     * Methods deletes fuel by Id in database.
     * Meotda kasuje tankowanie o danym Id w bazie danych.
     * @param fuel
     * @return
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
    }

    /**
     * Methods return number fuels for input type fuels
     * Metoda zwraca ilość tankowań konkrtnego rodzaju paliwa
     * @param carId
     * @param typeFuel
     * @return
     */
    public int calcNumberFuelsForTypeFuel(int carId, String typeFuel) {
        int result = 0;
        String sql = "SELECT count(" + KEY_FUEL_TYPE + ") FROM " + TABLE_FUELS + " WHERE " + KEY_CAR_ID + "= ? AND " + KEY_FUEL_TYPE + "=?;";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(carId), typeFuel});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }

        return result;
    }

    /**
     * Methods return a number types of fuel for car
     * Metoda zwraca ilość rodzajów paliw dla samochodu
     * @param carId
     * @return
     */
    public int calcTypesFuelForCarInDatabase(int carId){
        int result = 0;
        String sql = "SELECT count(DISTINCT " + KEY_FUEL_TYPE + ") FROM " + TABLE_FUELS + " WHERE " + KEY_CAR_ID + "= ?";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[] {String.valueOf(carId)});

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }

        return result;
    }

    /**
     * Method return car id for input fuel
     * Meotda zwraca car id dla danego tankowania
     * @param fuelId
     * @return
     */
    public String getCarIdForFuels(String fuelId){
        String carId = "";

        String sql = "SELECT " + KEY_CAR_ID + " FROM " + TABLE_FUELS + " WHERE " + KEY_ID + "=?";
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
     * @param carId
     * @return
     */
    public int deleteFuelsForCar(int carId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_FUELS, KEY_CAR_ID + "=?", new String[]{String.valueOf(carId)});
    }

    /**
     * Method returns fuel searched by date and carId from database. Used only in test
     * Metoda zwraca tankowanie wyszukany przez date i carId z bazy danych. Używane tylko przy testach.
     * @param date
     * @param carId
     * @return
     */
    public Fuel getFuelByDateAndCarId(String date, int carId) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_FUELS, new String[]{KEY_ID, KEY_FUEL_TYPE, KEY_DATE, KEY_COST, KEY_QUANTITY, KEY_MILEAGE, KEY_CAR_ID}, KEY_DATE + "=?" + " AND " + KEY_CAR_ID + "=?",
                new String[]{date, String.valueOf(carId)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Fuel fuel = new Fuel(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
        return fuel;
    }
}
