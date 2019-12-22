package pl.mgrzech.costfuel.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Fuel {

    /**
     * id car
     */
    private int id;
    /**
     * date of fuel
     */
    private String date;
    /**
     * type of refueled fuel
     */
    private String fuelType;
    /**
     * cost of refueled fuel
     */
    private double cost;
    /**
     * quantity od refueled fuel
     */
    private double quantity;
    /**
     * milleage car in refueled fuel
     */
    private int mileage;

    public Fuel(){

    }

    public Fuel(int id, String date, String fuelType, double cost, double quantity, int mileage) {
        this.id = id;
        this.date = date;
        this.fuelType = fuelType;
        this.cost = cost;
        this.quantity = quantity;
        this.mileage = mileage;
    }

    public Fuel(String date, String fuelType, double cost, double quantity, int mileage) {
        this.date = date;
        this.fuelType = fuelType;
        this.cost = cost;
        this.quantity = quantity;
        this.mileage = mileage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }


    public static class Comparators {

        /**
         * Static methods sorts list of fuels by date.
         */
        public static Comparator<Fuel> SORT_BY_DATE = new Comparator<Fuel>() {
            @Override
            public int compare(Fuel f1, Fuel f2) {
                int result = 1;
                try {
                    Date date1 = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(f1.getDate());
                    Date date2 = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(f2.getDate());

                    if(date1.before(date2)){
                        result = -1;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return result;
            }
        };
    }
}
