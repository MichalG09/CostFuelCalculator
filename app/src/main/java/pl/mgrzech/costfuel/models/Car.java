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
     * Average fuel consumption of first fuel type calculated and saved after add new / update / delete each fuel
     */
    private double averageConsumptionFirstFuel;
    /**
     * Average fuel consumption of second fuel type calculated and saved after add new / update / delete each fuel
     */
    private double averageConsumptionSecondFuel;
    /**
     * Average cost first fuel type calculated and saved after add new / update / delete each fuel
     */
    private double averageCostFirstFuel;
    /**
     * Average cost second fuel type calculated and saved after add new / update / delete each fuel
     */
    private double averageCostSecondFuel;
    /**
     * types of fuel for car
     */
    private String fuelType;

    /**
     * Number of month for date fuels take to calculation
     */
    private String periodTimeForCalculation;

    public Car(){

    }

    public Car(String mark, String model, String fuelType, String period) {
        this.mark = mark;
        this.model = model;
        this.fuelType = fuelType;
        this.averageConsumptionFirstFuel = 0;
        this.averageCostFirstFuel = 0;
        this.averageConsumptionSecondFuel = 0;
        this.averageCostSecondFuel = 0;
        this.periodTimeForCalculation = period;
    }

    public Car(String mark, String model, double averageConsumptionFirstFuel, double averageCostFirstFuel,
               double averageConsumptionSecondFuel, double averageCostSecondFuel, String fuelType, String period) {
        this.mark = mark;
        this.model = model;
        this.averageConsumptionFirstFuel = averageConsumptionFirstFuel;
        this.averageCostFirstFuel = averageCostFirstFuel;
        this.averageConsumptionSecondFuel = averageConsumptionSecondFuel;
        this.averageCostSecondFuel = averageCostSecondFuel;
        this.fuelType = fuelType;
        this.periodTimeForCalculation = period;
    }

    public Car(int id, String mark, String model, double averageConsumptionFirstFuel, double averageCostFirstFuel,
               double averageConsumptionSecondFuel, double averageCostSecondFuel, String fuelType, String period) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.averageConsumptionFirstFuel = averageConsumptionFirstFuel;
        this.averageCostFirstFuel = averageCostFirstFuel;
        this.averageConsumptionSecondFuel = averageConsumptionSecondFuel;
        this.averageCostSecondFuel = averageCostSecondFuel;
        this.fuelType = fuelType;
        this.periodTimeForCalculation = period;
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

    public double getAverageConsumptionFirstFuel() {
        return averageConsumptionFirstFuel;
    }

    public void setAverageConsumptionFirstFuel(double averageConsumptionFirstFuel) {
        this.averageConsumptionFirstFuel = averageConsumptionFirstFuel;
    }

    public double getAverageCostFirstFuel() {
        return averageCostFirstFuel;
    }

    public void setAverageCostFirstFuel(double averageCostFirstFuel) {
        this.averageCostFirstFuel = averageCostFirstFuel;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getAverageConsumptionSecondFuel() {
        return averageConsumptionSecondFuel;
    }

    public void setAverageConsumptionSecondFuel(double averageConsumptionSecondFuel) {
        this.averageConsumptionSecondFuel = averageConsumptionSecondFuel;
    }

    public double getAverageCostSecondFuel() {
        return averageCostSecondFuel;
    }

    public void setAverageCostSecondFuel(double averageCostSecondFuel) {
        this.averageCostSecondFuel = averageCostSecondFuel;
    }

    public String getPeriodTimeForCalculation() {
        return periodTimeForCalculation;
    }

    public void setPeriodTimeForCalculation(String periodTimeForCalculation) {
        this.periodTimeForCalculation = periodTimeForCalculation;
    }

    public int convertPeriodTimeToInt() {
        if(getPeriodTimeForCalculation().equals("Brak ograniczeń")){
            return 0;
        }
        else{
            return Integer.parseInt(getPeriodTimeForCalculation().substring(0,2).trim());
        }
    }

    public String toStringForCsvFile(){
        return "car," + this.getId()+ "," + this.getMark() + "," + this.getModel()+ "," + this.getAverageConsumptionFirstFuel()+ ","
                + this.getAverageConsumptionSecondFuel()+ "," + this.getAverageCostFirstFuel()+ "," + this.getAverageCostSecondFuel()
                + "," + this.getFuelType()+ "," + this.getPeriodTimeForCalculation() + "\n";
    }
}
