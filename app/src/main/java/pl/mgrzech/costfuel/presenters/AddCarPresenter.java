package pl.mgrzech.costfuel.presenters;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.activities.IAddCarView;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.database.DatabaseSingleton;
import pl.mgrzech.costfuel.models.Car;

public class AddCarPresenter implements IAddCarPresenter {

    private IAddCarView addCarView;
    private Context mContext;
    private AdapterView.OnItemSelectedListener listener;

    private String brandAddingCar = "";
    private String modelAddingCar = "";
    private String typeFuelAddingCar = "";
    private String periodTimeAddingCar = "";
    private Spinner spinnerBranchCar;
    private Spinner spinnerModelCar;
    private Spinner spinnerTypeFuel;
    private Spinner spinnerPeriodTIme;

    public AddCarPresenter(IAddCarView addCarView, Context context, Spinner spinnerBranchCar,
                           Spinner spinnerModelCar, Spinner spinnerTypeFuel, Spinner spinnerPeriodTIme) {

        this.addCarView = addCarView;
        this.mContext = context;
        this.spinnerBranchCar = spinnerBranchCar;
        this.spinnerModelCar = spinnerModelCar;
        this.spinnerTypeFuel = spinnerTypeFuel;
        this.spinnerPeriodTIme = spinnerPeriodTIme;
        listener = (AdapterView.OnItemSelectedListener) mContext;
    }

    /**
     * Methods creates a spinner with branch cars
     * Metoda tworzy spinner z markami samochodów
     */
    @Override
    public void createSpinnerBranchCar() {
        Toast.makeText(mContext, mContext.getString(R.string.add_car_activity_choose_branch), Toast.LENGTH_SHORT).show();
        createSpinner(spinnerBranchCar, listener, R.array.brands_cars);
    }

    /**
     * Methods creates a spinner with model cars
     * Metoda tworzy spinner z modelami samochodów
     */
    @Override
    public void createSpinnerModelCar() {
        Toast.makeText(mContext, mContext.getString(R.string.add_car_activity_chose_model), Toast.LENGTH_SHORT).show();

        spinnerModelCar.setVisibility(View.VISIBLE);
        createSpinner(spinnerModelCar, listener, getListModelCarForBrand(brandAddingCar));
        modelAddingCar = "";
    }

    @Override
    public void createSpinnerTypeFuel() {
        Toast.makeText(mContext, mContext.getString(R.string.add_car_activity_chose_fuel_type), Toast.LENGTH_SHORT).show();

        spinnerTypeFuel.setVisibility(View.VISIBLE);
        createSpinner(spinnerTypeFuel, listener, R.array.types_fuel);
        typeFuelAddingCar = "";
    }

    @Override
    public void createSpinnerPeriodTime() {
        Toast.makeText(mContext, mContext.getString(R.string.add_car_activity_chose_period_time), Toast.LENGTH_SHORT).show();

        spinnerPeriodTIme.setVisibility(View.VISIBLE);
        createSpinner(spinnerPeriodTIme, listener, R.array.period_time);
        periodTimeAddingCar = "";
    }

    @Override
    public void getBrandAddingCar(AdapterView<?> parent, int position) {
        brandAddingCar = parent.getItemAtPosition(position).toString();
        changeBackgroundColor(spinnerBranchCar);
        createSpinnerModelCar();
    }

    @Override
    public void getModelAddingCar(AdapterView<?> parent, int position) {
        modelAddingCar = parent.getItemAtPosition(position).toString();
        changeBackgroundColor(spinnerModelCar);
        if(typeFuelAddingCar.isEmpty()){
            createSpinnerTypeFuel();
        }
    }

    @Override
    public void getTypeFuelAddingCar(AdapterView<?> parent, int position) {
        changeBackgroundColor(spinnerTypeFuel);
        typeFuelAddingCar = parent.getItemAtPosition(position).toString();
        if(periodTimeAddingCar.isEmpty()){
            createSpinnerPeriodTime();
        }
    }

    @Override
    public void getPeriodTimeAddingCarr(AdapterView<?> parent, int position) {
        changeBackgroundColor(spinnerPeriodTIme);
        periodTimeAddingCar = parent.getItemAtPosition(position).toString();
    }

    @Override
    public boolean saveNewCar() {
        String textErrorValidation = "";
        boolean correctValidation = true;
        if(brandAddingCar.isEmpty()){
            textErrorValidation = textErrorValidation + mContext.getString(R.string.add_car_activity_incorrect_branch);
            correctValidation = false;
            spinnerBranchCar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backdroundForValidationError));
        }
        if(modelAddingCar.isEmpty() || modelAddingCar.equals(mContext.getString(R.string.model_car_for_validate))){
            textErrorValidation = textErrorValidation + mContext.getString(R.string.add_car_activity_incorrect_model);
            correctValidation = false;
            spinnerModelCar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backdroundForValidationError));
        }
        if (typeFuelAddingCar.isEmpty() || typeFuelAddingCar.equals(mContext.getString(R.string.fuel_type_for_validate))){
            textErrorValidation = textErrorValidation + mContext.getString(R.string.add_car_activity_incorrect_type_fuel);
            correctValidation = false;
            spinnerTypeFuel.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backdroundForValidationError));
        }
        if (periodTimeAddingCar.isEmpty() || periodTimeAddingCar.equals(mContext.getString(R.string.choose_period_time))){
            textErrorValidation = textErrorValidation + mContext.getString(R.string.add_car_activity_incorrect_period);
            correctValidation = false;
            spinnerPeriodTIme.setBackgroundColor(ContextCompat.getColor(mContext, R.color.backdroundForValidationError));
        }

        if(correctValidation) {
            Database database = DatabaseSingleton.getInstance(mContext);
            database.addCar(new Car(brandAddingCar, modelAddingCar, typeFuelAddingCar, periodTimeAddingCar));
            Toast.makeText(mContext, mContext.getString(R.string.add_car_activity_corrct_message), Toast.LENGTH_LONG).show();
            return true;
        }
        else{
            Toast.makeText(mContext, textErrorValidation, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, int position) {
        if(parent.toString().contains(spinnerBranchCar.getResources().getResourceEntryName(spinnerBranchCar.getId()))){
            getBrandAddingCar(parent, position);
        }
        else if (parent.toString().contains(spinnerModelCar.getResources().getResourceEntryName(spinnerModelCar.getId()))){
            getModelAddingCar(parent, position);
        }
        else if (parent.toString().contains(spinnerTypeFuel.getResources().getResourceEntryName(spinnerTypeFuel.getId()))){
            getTypeFuelAddingCar(parent,position);
        }
        else if (parent.toString().contains(spinnerPeriodTIme.getResources().getResourceEntryName(spinnerPeriodTIme.getId()))){
            getPeriodTimeAddingCarr(parent, position);
        }
    }

    /**
     * Methods creates spinner with basic parameters
     * @param spinner spinner to create
     * @param textArray id array with data for spinner
     */
    private void createSpinner(Spinner spinner, AdapterView.OnItemSelectedListener listener, int textArray){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                textArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

    /**
     * Methods change background color for basic spinner background color
     * Metoda zmienia kolor spinnera na bazowy kolor
     * @param spinner spinner for color change
     *
     */
    private void changeBackgroundColor(Spinner spinner) {
        spinner.setBackgroundColor(ContextCompat.getColor(mContext, R.color.normalBackground));
    }

    /**
     * Method got a list id all models car for mark car chosen in layout.
     * Meotda daje listę wszystkich modeli samochodów dla wybranej marki samochodu.
     * @param selectedItem name branch car
     * @return id list with model cars for insert branch
     */
    private int getListModelCarForBrand(String selectedItem) {
        switch (selectedItem) {
            case "Alfa Romeo":
                return R.array.alfa_romeo_models;
            case "Audi":
                return R.array.audi_models;
            case "BMW":
                return R.array.bmw_models;
            case "Chevrolet":
                return R.array.chevrolet_models;
            case "Chrysler":
                return R.array.chrysler_mocdels;
            case "Citroen":
                return R.array.citroen_mocdels;
            case "Daewoo":
                return R.array.daewoo_models;
            case "Dodge":
                return R.array.dodge_mocels;
            case "Dacia":
                return R.array.dacia_models;
            case "Ford":
                return R.array.ford_models;
            case "Fiat":
                return R.array.fiat_models;
            case "Honda":
                return R.array.honda_models;
            case "Hyundai":
                return R.array.hyundai_models;
            case "Jeep":
                return R.array.jeep_models;
            case "Kia":
                return R.array.kia_models;
            case "Land Rover":
                return R.array.land_rover_models;
            case "Mazda":
                return R.array.mazda_models;
            case "Mercedes":
                return R.array.mercedes_models;
            case "Mini":
                return R.array.mini_models;
            case "Mitsubishi":
                return R.array.mitsubishi_models;
            case "Nissan":
                return R.array.nissan_models;
            case "Opel":
                return R.array.opel_models;
            case "Peugeot":
                return R.array.peugeot_models;
            case "Renault":
                return R.array.renault_models;
            case "Rover":
                return R.array.rover_models;
            case "Saab":
                return R.array.saab_models;
            case "Seat":
                return R.array.seat_models;
            case "Skoda":
                return R.array.skoda_models;
            case "Smart":
                return R.array.smart_models;
            case "Subaru":
                return R.array.subaru_models;
            case "Suzuki":
                return R.array.suzuki_models;
            case "Toyota":
                return R.array.toyota_models;
            case "Volkswagen":
                return R.array.volkswagen_models;
            case "Volvo":
                return R.array.volvo_models;
            default:
                return R.array.empty_array;
        }
    }
}
