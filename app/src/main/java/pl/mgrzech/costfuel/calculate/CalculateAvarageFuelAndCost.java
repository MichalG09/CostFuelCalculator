package pl.mgrzech.costfuel.calculate;

import java.util.Collections;
import java.util.List;

import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class CalculateAvarageFuelAndCost {

    /**
     * Method calculate average cost and average fuel consumption for car per 100 km.
     * Method checks number type fuel for car and select corrects algorithm to calculate.
     * Moetoda przelicza koszt i spalanie samochodu na 100 km
     * @param fuelDatabase
     * @param car
     * @return Car with new average values
     */
    public static Car recarkulate(FuelDatabase fuelDatabase, Car car) {

        if (car.getFuelType().contains("+")){
            return calcForTwoTypeFUel(car, fuelDatabase);
        }
        else {
            return calcForOneTypeFuel(car, fuelDatabase);
        }
    }

    /**
     * Method calculate values for car with two type fuels.
     * @param car
     * @param fuelDatabase
     * @return
     */
    private static Car calcForTwoTypeFUel(Car car, FuelDatabase fuelDatabase) {
        double sumCostFuels = 0;
        double sumQuantityFuels = 0;
        double averageCost = 0;
        double averageFuelConsumption = 0;
        int minMileageFirstTypeFuel = 0;
        int maxMileageFirstTypeFuel = 0;
        int maxMileageSecondTypeFuel = 0;
        int minMileageSecondTypeFuel = 0;
        double sumCostSecondTypeFuel = 0;
        double sumQuantitySecondTypeFuel = 0;
        double averageCostSecondTypeFuel = 0;
        double averageFuelConsumptionSecondTypeFuel = 0;
        String[] typesFuel = car.getFuelType().split("\\+");
        String typeFirstFuel = typesFuel[0].trim();
        String typeSecondFuel = typesFuel[1].trim();
        boolean canCalcForFirstFuel = fuelDatabase.calcNumberFuelsForTypeFuel(car.getId(), typeFirstFuel) > 1;
        boolean canCalcForSecondFuel = fuelDatabase.calcNumberFuelsForTypeFuel(car.getId(), typeSecondFuel) > 1;
        List<Fuel> listFuels = fuelDatabase.getAllFuelsForCarId(String.valueOf(car.getId()));
        Collections.sort(listFuels, Fuel.Comparators.SORT_BY_DATE);
        Fuel firstFuelFirstTypeForCar = null;
        Fuel firstFuelSecondTypeForCar = null;

        if(!canCalcForFirstFuel && !canCalcForSecondFuel){
            car.setAvarageFuelConsumption(0);
            car.setAvarageCost(0);
        }
        else {
            for (Fuel fuel : listFuels) {
                if (canCalcForFirstFuel && fuel.getFuelType().equals(typeFirstFuel)) {
                    if (maxMileageFirstTypeFuel < fuel.getMileage()) {
                        maxMileageFirstTypeFuel = fuel.getMileage();
                    }
                    if (minMileageFirstTypeFuel == 0 || minMileageFirstTypeFuel > fuel.getMileage()) {
                        minMileageFirstTypeFuel = fuel.getMileage();
                        firstFuelFirstTypeForCar = fuel;
                    }

                    sumCostFuels += fuel.getCost();
                    sumQuantityFuels += fuel.getQuantity();

                }
                else if (canCalcForSecondFuel && fuel.getFuelType().equals(typeSecondFuel)) {
                    if (maxMileageSecondTypeFuel < fuel.getMileage()) {
                        maxMileageSecondTypeFuel = fuel.getMileage();
                    }
                    if (minMileageSecondTypeFuel == 0 || minMileageSecondTypeFuel > fuel.getMileage()) {
                        minMileageSecondTypeFuel = fuel.getMileage();
                        firstFuelSecondTypeForCar = fuel;
                    }

                    sumCostSecondTypeFuel += fuel.getCost();
                    sumQuantitySecondTypeFuel += fuel.getQuantity();
                }
            }

            int deltaMileageFirstTypeFuel = maxMileageFirstTypeFuel - minMileageFirstTypeFuel;
            int deltaMileageSecondTypeFuel = maxMileageSecondTypeFuel - minMileageSecondTypeFuel;

            if (deltaMileageFirstTypeFuel <= 0 && deltaMileageSecondTypeFuel <= 0) {
                car.setAvarageFuelConsumption(-1);
                car.setAvarageCost(-1);
                return car;
            }
            else if (deltaMileageFirstTypeFuel > 0 && deltaMileageSecondTypeFuel <= 0) {
                averageCost = (sumCostFuels - firstFuelFirstTypeForCar.getCost()) / deltaMileageFirstTypeFuel;
                averageFuelConsumption = (sumQuantityFuels - firstFuelFirstTypeForCar.getQuantity()) / deltaMileageFirstTypeFuel;
            }
            else if (deltaMileageFirstTypeFuel <= 0 && deltaMileageSecondTypeFuel > 0) {
                averageCostSecondTypeFuel = (sumCostSecondTypeFuel - firstFuelSecondTypeForCar.getCost()) / deltaMileageSecondTypeFuel;
                averageFuelConsumptionSecondTypeFuel = (sumQuantitySecondTypeFuel - firstFuelSecondTypeForCar.getQuantity()) / deltaMileageSecondTypeFuel;
            }
            else {
                averageCost = (sumCostFuels - firstFuelFirstTypeForCar.getCost()) / deltaMileageFirstTypeFuel;
                averageFuelConsumption = (sumQuantityFuels - firstFuelFirstTypeForCar.getQuantity()) / deltaMileageFirstTypeFuel;
                averageCostSecondTypeFuel = (sumCostSecondTypeFuel - firstFuelSecondTypeForCar.getCost()) / deltaMileageSecondTypeFuel;
                averageFuelConsumptionSecondTypeFuel = (sumQuantitySecondTypeFuel - firstFuelSecondTypeForCar.getQuantity()) / deltaMileageSecondTypeFuel;
            }

            car.setAvarageFuelConsumption((averageFuelConsumption + averageFuelConsumptionSecondTypeFuel) * 100);
            car.setAvarageCost((averageCost + averageCostSecondTypeFuel) * 100);
        }

        return car;
    }

    /**
     * Method calculate values for car with one type fuel.
     * @param car
     * @param fuelDatabase
     * @return
     */
    private static Car calcForOneTypeFuel(Car car, FuelDatabase fuelDatabase) {

        double sumCostFuels = 0;
        double sumQuantityFuels = 0;
        double averageCost = 0;
        double averageFuelConsumption = 0;
        int minMileageFirstTypeFuel = 0;
        int maxMileageFirstTypeFuel = 0;
        int maxMileageSecondTypeFuel = 0;
        int minMileageSecondTypeFuel = 0;
        double sumCostSecondTypeFuel = 0;
        double sumQuantitySecondTypeFuel = 0;
        double averageCostSecondTypeFuel = 0;
        double averageFuelConsumptionSecondTypeFuel = 0;
        Fuel firstFuelForCar = null;
        List<Fuel> listFuels = fuelDatabase.getAllFuelsForCarId(String.valueOf(car.getId()));
        Collections.sort(listFuels, Fuel.Comparators.SORT_BY_DATE);

        if(fuelDatabase.calcNumberFuelsForTypeFuel(car.getId(), car.getFuelType()) > 1){
            for (Fuel fuel : listFuels) {
                if(minMileageFirstTypeFuel == 0 || minMileageFirstTypeFuel > fuel.getMileage()){
                    minMileageFirstTypeFuel = fuel.getMileage();
                    firstFuelForCar = fuel;
                }
                if (maxMileageFirstTypeFuel < fuel.getMileage()) {
                    maxMileageFirstTypeFuel = fuel.getMileage();
                }

                sumCostFuels += fuel.getCost();
                sumQuantityFuels += fuel.getQuantity();

            }

            if ((maxMileageFirstTypeFuel - minMileageFirstTypeFuel) > 0) {
                averageCost = ((sumCostFuels - firstFuelForCar.getCost()) / (maxMileageFirstTypeFuel - minMileageFirstTypeFuel)) * 100;
                averageFuelConsumption = ((sumQuantityFuels - firstFuelForCar.getQuantity()) / (maxMileageFirstTypeFuel - minMileageFirstTypeFuel)) * 100;
            }
            else {
                averageCost = -1;
                averageFuelConsumption = -1;
            }
        }
        else{
            averageCost = 0;
            averageFuelConsumption = 0;
        }

        car.setAvarageFuelConsumption(averageFuelConsumption);
        car.setAvarageCost(averageCost);

        return car;
    }

}
