package pl.mgrzech.costfuel.models;

public class Car {

    /**
     * car Id
     */
    private int id;
    /**
     * Mark of car
     */
    private String mark;
    /**
     * Model of car
     */
    private String model;
    /**
     * Average fuel consumption calculated and saved after add new / update / delete each fuel
     */
    private double avarageFuelConsumption;
    /**
     * Average cost calculated and saved after add new / update / delete each fuel
     */
    private double avarageCost;
    /**
     * types of fuel for car
     */
    private String fuelType;

    public Car(){

    }

    public Car(String mark, String model, String fuelType) {
        this.mark = mark;
        this.model = model;
        this.fuelType = fuelType;
        this.avarageFuelConsumption = 0;
        this.avarageCost = 0;
    }

    public Car(String mark, String model, double avarageFuelConsumption, double avarageCost, String fuelType) {
        this.mark = mark;
        this.model = model;
        this.avarageFuelConsumption = avarageFuelConsumption;
        this.avarageCost = avarageCost;
        this.fuelType = fuelType;
    }

    public Car(int id, String mark, String model, double avarageFuelConsumption, double avarageCost, String fuelType) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.avarageFuelConsumption = avarageFuelConsumption;
        this.avarageCost = avarageCost;
        this.fuelType = fuelType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getAvarageFuelConsumption() {
        return avarageFuelConsumption;
    }

    public void setAvarageFuelConsumption(double avarageFuelConsumption) {
        this.avarageFuelConsumption = avarageFuelConsumption;
    }

    public double getAvarageCost() {
        return avarageCost;
    }

    public void setAvarageCost(double avarageCost) {
        this.avarageCost = avarageCost;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

}
