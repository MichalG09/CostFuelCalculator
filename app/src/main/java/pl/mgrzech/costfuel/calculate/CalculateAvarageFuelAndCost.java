package pl.mgrzech.costfuel.calculate;

import java.text.ParseException;
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
            return calcForTwoTypeFuel(car, fuelDatabase);
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
    private static Car calcForTwoTypeFuel(Car car, FuelDatabase fuelDatabase) {
        double sumCostFuels = 0;
        double sumQuantityFuels = 0;
        double avarageCost = 0;
        double avarageFuelConsumption = 0;
        int minMileageFirstTypeFuel = 0;
        int maxMileageFirstTypeFuel = 0;
        int maxMileageSecondTypeFuel = 0;
        int minMileageSecondTypeFuel = 0;
        double sumCostSecondTypeFuel = 0;
        double sumQuantitySecondTypeFuel = 0;
        double avarageCostSecondTypeFuel = 0;
        double avarageFuelConsumptionSecondTypeFuel = 0;
        String[] typesFuel = car.getFuelType().split("\\+");
        String typeFirstFuel = typesFuel[0].trim();
        String typeSecondFuel = typesFuel[1].trim();
        boolean canCalcForFirstFuel = fuelDatabase.calcNumberFuelsForTypeFuel(car.getId(), typeFirstFuel) > 1;
        boolean canCalcForSecondFuel = fuelDatabase.calcNumberFuelsForTypeFuel(car.getId(), typeSecondFuel) > 1;
        List<Fuel> listFuels = null;
        try {
            listFuels = fuelDatabase.getAllFuelsForCarIdInCalculationPeriod(String.valueOf(car.getId()), car.convertPeriodTimeToInt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Collections.sort(listFuels, Fuel.Comparators.SORT_BY_DATE);
        Fuel firstFuelFirstTypeForCar = null;
        Fuel firstFuelSecondTypeForCar = null;

        if(!canCalcForFirstFuel && !canCalcForSecondFuel){
            car.setAverageConsumptionFirstFuel(0);
            car.setAverageConsumptionSecondFuel(0);
            car.setAverageCostFirstFuel(0);
            car.setAverageCostSecondFuel(0);
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
                car.setAverageConsumptionFirstFuel(-1);
                car.setAverageConsumptionSecondFuel(-1);
                car.setAverageCostFirstFuel(-1);
                car.setAverageCostSecondFuel(-1);
                return car;
            }
            else if (deltaMileageFirstTypeFuel > 0 && deltaMileageSecondTypeFuel <= 0) {
                avarageCost = (sumCostFuels - firstFuelFirstTypeForCar.getCost()) / deltaMileageFirstTypeFuel;
                avarageFuelConsumption = (sumQuantityFuels - firstFuelFirstTypeForCar.getQuantity()) / deltaMileageFirstTypeFuel;
            }
            else if (deltaMileageFirstTypeFuel <= 0 && deltaMileageSecondTypeFuel > 0) {
                avarageCostSecondTypeFuel = (sumCostSecondTypeFuel - firstFuelSecondTypeForCar.getCost()) / deltaMileageSecondTypeFuel;
                avarageFuelConsumptionSecondTypeFuel = (sumQuantitySecondTypeFuel - firstFuelSecondTypeForCar.getQuantity()) / deltaMileageSecondTypeFuel;
            }
            else {
                avarageCost = (sumCostFuels - firstFuelFirstTypeForCar.getCost()) / deltaMileageFirstTypeFuel;
                avarageFuelConsumption = (sumQuantityFuels - firstFuelFirstTypeForCar.getQuantity()) / deltaMileageFirstTypeFuel;
                avarageCostSecondTypeFuel = (sumCostSecondTypeFuel - firstFuelSecondTypeForCar.getCost()) / deltaMileageSecondTypeFuel;
                avarageFuelConsumptionSecondTypeFuel = (sumQuantitySecondTypeFuel - firstFuelSecondTypeForCar.getQuantity()) / deltaMileageSecondTypeFuel;
            }

            car.setAverageConsumptionFirstFuel(avarageFuelConsumption * 100);
            car.setAverageCostFirstFuel(avarageCost * 100);
            car.setAverageConsumptionSecondFuel(avarageFuelConsumptionSecondTypeFuel * 100);
            car.setAverageCostSecondFuel(avarageCostSecondTypeFuel * 100);
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
        double avarageCost = 0;
        double avarageFuelConsumption = 0;
        int minMileageFirstTypeFuel = 0;
        int maxMileageFirstTypeFuel = 0;
        int maxMileageSecondTypeFuel = 0;
        int minMileageSecondTypeFuel = 0;
        double sumCostSecondTypeFuel = 0;
        double sumQuantitySecondTypeFuel = 0;
        double averageCostSecondTypeFuel = 0;
        double averageFuelConsumptionSecondTypeFuel = 0;
        Fuel firstFuelForCar = null;
        List<Fuel> listFuels = null;
        try {
            listFuels = fuelDatabase.getAllFuelsForCarIdInCalculationPeriod(String.valueOf(car.getId()), car.convertPeriodTimeToInt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Collections.sort(listFuels, Fuel.Comparators.SORT_BY_DATE);

        if(!listFuels.isEmpty()){
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
                    avarageCost = ((sumCostFuels - firstFuelForCar.getCost()) / (maxMileageFirstTypeFuel - minMileageFirstTypeFuel)) * 100;
                    avarageFuelConsumption = ((sumQuantityFuels - firstFuelForCar.getQuantity()) / (maxMileageFirstTypeFuel - minMileageFirstTypeFuel)) * 100;
                }
                else {
                    avarageCost = -1;
                    avarageFuelConsumption = -1;
                }
            }
            else{
                avarageCost = 0;
                avarageFuelConsumption = 0;
            }
        }

        car.setAverageConsumptionFirstFuel(avarageFuelConsumption);
        car.setAverageCostFirstFuel(avarageCost);
        car.setAverageConsumptionSecondFuel(0);
        car.setAverageCostSecondFuel(0);

        return car;
    }

    public static String[] fuelTypeForDeleting(String oldTypeFuelEditCar, String typeFuelEditCar) {
        String oldFirstFuelType = "";
        String oldSecondFuelType = "";
        String newFirstFuelType = "";
        String regexForSplitString = " + ";
        String[] result = new String[1];

        if(oldTypeFuelEditCar.contains(regexForSplitString)){
            String[] oldFuelTypes = oldTypeFuelEditCar.split("\\+");
            oldFirstFuelType = oldFuelTypes[0].trim();
            oldSecondFuelType = oldFuelTypes[1].trim();
        }
        else{
            oldFirstFuelType = oldTypeFuelEditCar;
        }

        String[] newFuelTypes = typeFuelEditCar.split("\\+");
        newFirstFuelType = newFuelTypes[0].trim();

        if(!oldFirstFuelType.equals(newFirstFuelType) && oldSecondFuelType.isEmpty()){
            result[0] = oldFirstFuelType;
        }
        else if(!oldFirstFuelType.equals(newFirstFuelType) && !oldSecondFuelType.isEmpty()){
            result = new String[2];
            result[0] = oldFirstFuelType;
            result[1] = oldSecondFuelType;
        }
        else if(oldFirstFuelType.equals(newFirstFuelType) && oldTypeFuelEditCar.contains(regexForSplitString)){
            result[0] = oldSecondFuelType;
        }
        else if(oldFirstFuelType.equals(newFirstFuelType) && typeFuelEditCar.contains(regexForSplitString)){
            result[0] = oldFirstFuelType;
        }

        return result;
    }
}
