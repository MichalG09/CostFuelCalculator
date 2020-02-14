package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.database.DatabaseSingleton;
import pl.mgrzech.costfuel.models.Car;

public class AddCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String brandAddingCar = "";
    private String modelsAddingCar = "";
    private String typeFuelAddingCar = "";
    private String periodTimeAddingCar = "";
    private Spinner spinnerBranchCar;
    private Spinner spinnerModelCar;
    private Spinner spinnerTypeFuel;
    private Spinner spinnerPeriodTIme;
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        database = DatabaseSingleton.getInstance(this);

        createSpinnerBranchCar();
        spinnerModelCar = findViewById(R.id.spinnerModelCar);
        spinnerTypeFuel = findViewById(R.id.spinnerTypeFuelCar);
        spinnerPeriodTIme = findViewById(R.id.spinnerPeriodTime);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * The method checks which field has been completed and saved the entered value.
     * Metoda sprawdza, które pole zostało uzupełnione i zapisuje wprowadzoną wartość.
     * @param parent parent
     * @param view view
     * @param position position
     * @param id id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.toString().contains(spinnerBranchCar.getResources().getResourceEntryName(spinnerBranchCar.getId()))){
            brandAddingCar = parent.getItemAtPosition(position).toString();
            changeBackgroundColor(spinnerBranchCar);
            createSpinnerModelCar(parent);
        }
        else if (parent.toString().contains(spinnerModelCar.getResources().getResourceEntryName(spinnerModelCar.getId()))){
            modelsAddingCar = parent.getItemAtPosition(position).toString();
            changeBackgroundColor(spinnerModelCar);
            if(typeFuelAddingCar.isEmpty()){
                createSpinnerTypeFuel(parent);
            }
        }
        else if (parent.toString().contains(spinnerTypeFuel.getResources().getResourceEntryName(spinnerTypeFuel.getId()))){
            changeBackgroundColor(spinnerTypeFuel);
            typeFuelAddingCar = parent.getItemAtPosition(position).toString();
            if(periodTimeAddingCar.isEmpty()){
                createSpinnerPeriodTime(parent);
            }
        }
        else if (parent.toString().contains(spinnerPeriodTIme.getResources().getResourceEntryName(spinnerPeriodTIme.getId()))){
            changeBackgroundColor(spinnerPeriodTIme);
            periodTimeAddingCar = parent.getItemAtPosition(position).toString();
        }
    }

    /**
     * Methods creates a spinner with branch cars
     * Metoda tworzy spinner z markami samochodów
     */
    private void createSpinnerBranchCar() {
        Toast.makeText(this, getString(R.string.add_car_activity_choose_branch), Toast.LENGTH_SHORT).show();
        spinnerBranchCar = findViewById(R.id.spinnerBranchCar);
        createSpinner(spinnerBranchCar, R.array.brands_cars);
    }

    /**
     * Methods creates a spinner with model cars
     * Metoda tworzy spinner z modelami samochodów
     */
    private void createSpinnerModelCar(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), getString(R.string.add_car_activity_chose_model), Toast.LENGTH_SHORT).show();

        spinnerModelCar.setVisibility(View.VISIBLE);
        createSpinner(spinnerModelCar, getListModelCarForBrand(brandAddingCar));
        modelsAddingCar = "";
    }

    /**
     * Methods creates a spinner with type fuel cars
     * Metoda tworzy spinner z rodzajem paliwa samochodów
     */
    private void createSpinnerTypeFuel(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), getString(R.string.add_car_activity_chose_fuel_type), Toast.LENGTH_SHORT).show();

        spinnerTypeFuel.setVisibility(View.VISIBLE);
        createSpinner(spinnerTypeFuel, R.array.types_fuel);
        typeFuelAddingCar = "";
    }

    /**
     * Methods creates a spinner with period tima for calculation average values
     * Metoda tworzy spinner z okresm czasu dla obliczeń średnich wartości
     */
    private void createSpinnerPeriodTime(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), getString(R.string.add_car_activity_chose_period_time), Toast.LENGTH_SHORT).show();

        spinnerPeriodTIme.setVisibility(View.VISIBLE);
        createSpinner(spinnerPeriodTIme, R.array.period_time);
        periodTimeAddingCar = "";
    }

    /**
     * Methods creates spinner with basic parameters
     * @param spinner spinner to create
     * @param textArray id array with data for spinner
     */
    private void createSpinner(Spinner spinner, int textArray){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                textArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Method check data for a new car from layout add new car. If data is correct save new car in database or data is
     * incorrect change spinner (witj incorrect data) background color to red.
     * Meotda sprawdza poprawnośc danych z grafiki dodawania nowego samochodu. Jeżeli dane sa poprawne to zapisuje samochód w bazie danych lub jeżeli dane są
     * niepoprawne to zmienia kolor spinnera (z błędnymi danymi) na czerwony
     * @param view view
     */
    public void onSaveNewCar(View view) {
        String textErrorValidation = "";
        boolean correctValidation = true;
        if(brandAddingCar.isEmpty()){
            textErrorValidation = textErrorValidation + getString(R.string.add_car_activity_incorrect_branch);
            correctValidation = false;
            spinnerBranchCar.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }
        if(modelsAddingCar.isEmpty() || modelsAddingCar.equals(getString(R.string.model_car_for_validate))){
            textErrorValidation = textErrorValidation + getString(R.string.add_car_activity_incorrect_model);
            correctValidation = false;
            spinnerModelCar.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }
        if (typeFuelAddingCar.isEmpty() || typeFuelAddingCar.equals(getString(R.string.fuel_type_for_validate))){
            textErrorValidation = textErrorValidation + getString(R.string.add_car_activity_incorrect_type_fuel);
            correctValidation = false;
            spinnerTypeFuel.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }
        if (periodTimeAddingCar.isEmpty() || periodTimeAddingCar.equals(getString(R.string.choose_period_time))){
            textErrorValidation = textErrorValidation + getString(R.string.add_car_activity_incorrect_period);
            correctValidation = false;
            spinnerPeriodTIme.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        if(correctValidation) {
            database.addCar(new Car(brandAddingCar, modelsAddingCar, typeFuelAddingCar, periodTimeAddingCar));
            Toast.makeText(this, getString(R.string.add_car_activity_corrct_message), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AllCarsActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, textErrorValidation, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Methods change background color for basic spinner background color
     * Metoda zmienia kolor spinnera na bazowy kolor
     * @param spinner spinner for color change
     *
     */
    private void changeBackgroundColor(Spinner spinner) {
        spinner.setBackgroundColor(ContextCompat.getColor(AddCarActivity.this, R.color.normalBackground));
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
