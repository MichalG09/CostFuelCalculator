package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.database.CarDatabase;
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
    private ArrayAdapter<CharSequence> adapter;

    /**
     * car datebase
     */
    private CarDatabase carDatabase = new CarDatabase(this);

    /**
     * Methods is called on create this activity. It created spinner with all mark cars.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        createSpinnerBranchCar();
        spinnerModelCar = (Spinner) findViewById(R.id.spinnerModelCar);
        spinnerModelCar.setVisibility(View.INVISIBLE);
        spinnerTypeFuel = (Spinner) findViewById(R.id.spinnerTypeFuelCar);
        spinnerTypeFuel.setVisibility(View.INVISIBLE);
        spinnerPeriodTIme = (Spinner) findViewById(R.id.spinnerPeriodTime);
        spinnerPeriodTIme.setVisibility(View.INVISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * The method checks which field has been completed and saved the entered value.
     * Metoda sprawdza, które pole zostało uzupełnione i zapisuje wprowadzoną wartość.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.toString().contains("spinnerBranchCar")){
            brandAddingCar = parent.getItemAtPosition(position).toString();
            changeBackgroundColor(spinnerBranchCar, R.color.background);
            createSpinnerModelCar(parent);
        }
        else if (parent.toString().contains("spinnerModelCar")){
            modelsAddingCar = parent.getItemAtPosition(position).toString();
            changeBackgroundColor(spinnerModelCar, R.color.background);
            if(typeFuelAddingCar.isEmpty()){
                createSpinnerTypeFuel(parent);
            }
        }
        else if (parent.toString().contains("spinnerTypeFuelCar")){
            changeBackgroundColor(spinnerTypeFuel, R.color.background);
            typeFuelAddingCar = parent.getItemAtPosition(position).toString();
            if(periodTimeAddingCar.isEmpty()){
                createSpinnerPeriodTime(parent);
            }
        }
        else if (parent.toString().contains("spinnerPeriodTime")){
            changeBackgroundColor(spinnerPeriodTIme, R.color.background);
            periodTimeAddingCar = parent.getItemAtPosition(position).toString();
        }
    }

    private void createSpinnerBranchCar() {
        Toast.makeText(this, "Wybierz markę samochodu", Toast.LENGTH_SHORT).show();
        spinnerBranchCar = (Spinner) findViewById(R.id.spinnerBranchCar);
        createSpinner(spinnerBranchCar, R.array.brands_cars);
    }

    private void createSpinnerModelCar(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Wybierz model samochodu", Toast.LENGTH_SHORT).show();

        spinnerModelCar.setVisibility(View.VISIBLE);
        createSpinner(spinnerModelCar, getListModelCarForBrand(brandAddingCar));
        modelsAddingCar = "";
    }

    private void createSpinnerTypeFuel(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Wybierz typ paliwa samochodu", Toast.LENGTH_SHORT).show();

        spinnerTypeFuel.setVisibility(View.VISIBLE);
        createSpinner(spinnerTypeFuel, R.array.types_fuel);
        typeFuelAddingCar = "";
    }

    private void createSpinnerPeriodTime(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Wybierz okres przeliczania kosztów", Toast.LENGTH_SHORT).show();

        spinnerPeriodTIme.setVisibility(View.VISIBLE);
        createSpinner(spinnerPeriodTIme, R.array.period_time);
        periodTimeAddingCar = "";
    }

    private void createSpinner(Spinner spinner, int textArray){
        adapter = ArrayAdapter.createFromResource(this,
                textArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Method of saving a new car with data from layout add new car. It validated data and saved new car in database.
     * Metoda zapisywania nowego samochodu z grafiki dodawania nowego samochodu. Sprawdze poprawność danych i zapisuje nowy samochód do bazy danych.
     * @param view
     */
    public void onSaveNewCar(View view) {
        String textErrorValidation = "";
        boolean correctValidation = true;
        if(brandAddingCar.isEmpty()){
            textErrorValidation = textErrorValidation + "Niepoprawnie wprowadzona marka samochodu ! \n";
            correctValidation = false;
            spinnerBranchCar.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }
        if(modelsAddingCar.isEmpty() || modelsAddingCar.equals(getString(R.string.model_car_for_validate))){
            textErrorValidation = textErrorValidation + "Niepoprawnie wprowadzony model samochodu ! \n";
            correctValidation = false;
            spinnerModelCar.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }
        if (typeFuelAddingCar.isEmpty() || typeFuelAddingCar.equals(getString(R.string.fuel_type_for_validate))){
            textErrorValidation = textErrorValidation + "Niepoprawnie wprowadzony rodzaj paliwa ! \n";
            correctValidation = false;
            spinnerTypeFuel.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }
        if (periodTimeAddingCar.isEmpty() || periodTimeAddingCar.equals(getString(R.string.choose_period_time))){
            textErrorValidation = textErrorValidation + "Niepoprawnie wprowadzony okres wyliczania kosztów ! \n";
            correctValidation = false;
            spinnerPeriodTIme.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        if(correctValidation) {
            carDatabase.addCar(new Car(brandAddingCar, modelsAddingCar, typeFuelAddingCar, periodTimeAddingCar));
            Toast.makeText(this, "Poprawnie zapisano nowy samochód !", Toast.LENGTH_LONG).show();
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

    private void changeBackgroundColor(Spinner spinner, int color) {
        spinner.setBackgroundColor(ContextCompat.getColor(AddCarActivity.this, color));
    }

    /**
     * Method got a list all models car for mark car chosen in layout.
     * Meotda daje listę wszystkich modeli samochodów dla wybranej marki samochodu.
     * @param selectedItem
     * @return
     */
    private int getListModelCarForBrand(String selectedItem) {
        if(selectedItem.equals("Alfa Romeo")){
            return R.array.alfa_romeo_models;
        }
        else if (selectedItem.equals("Audi")){
            return R.array.audi_models;
        }
        else if (selectedItem.equals("BMW")){
            return R.array.bmw_models;
        }
        else if (selectedItem.equals("Chevrolet")){
            return R.array.chevrolet_models;
        }
        else if (selectedItem.equals("Chrysler")){
            return R.array.chrysler_mocdels;
        }
        else if (selectedItem.equals("Citroen")){
            return R.array.citroen_mocdels;
        }
        else if (selectedItem.equals("Daewoo")){
            return R.array.daewoo_models;
        }
        else if (selectedItem.equals("Dodge")){
            return R.array.dodge_mocels;
        }
        else if (selectedItem.equals("Dacia")){
            return R.array.dacia_models;
        }
        else if (selectedItem.equals("Ford")){
            return R.array.ford_models;
        }
        else if (selectedItem.equals("Fiat")){
            return R.array.fiat_models;
        }
        else if (selectedItem.equals("Honda")){
            return R.array.honda_models;
        }
        else if (selectedItem.equals("Hyundai")){
            return R.array.hyundai_models;
        }
        else if (selectedItem.equals("Jeep")){
            return R.array.jeep_models;
        }
        else if (selectedItem.equals("Kia")){
            return R.array.kia_models;
        }
        else if (selectedItem.equals("Land Rover")){
            return R.array.land_rover_models;
        }
        else if (selectedItem.equals("Mazda")){
            return R.array.mazda_models;
        }
        else if (selectedItem.equals("Mercedes")){
            return R.array.mercedes_models;
        }
        else if (selectedItem.equals("Mini")){
            return R.array.mini_models;
        }
        else if (selectedItem.equals("Mitsubishi")){
            return R.array.mitsubishi_models;
        }
        else if (selectedItem.equals("Nissan")){
            return R.array.nissan_models;
        }
        else if (selectedItem.equals("Opel")){
            return R.array.opel_models;
        }
        else if (selectedItem.equals("Peugeot")){
            return R.array.peugeot_models;
        }
        else if (selectedItem.equals("Renault")){
            return R.array.renault_models;
        }
        else if (selectedItem.equals("Rover")){
            return R.array.rover_models;
        }
        else if (selectedItem.equals("Saab")){
            return R.array.saab_models;
        }
        else if (selectedItem.equals("Seat")){
            return R.array.seat_models;
        }
        else if (selectedItem.equals("Skoda")){
            return R.array.skoda_models;
        }
        else if (selectedItem.equals("Smart")){
            return R.array.smart_models;
        }
        else if (selectedItem.equals("Subaru")){
            return R.array.subaru_models;
        }
        else if (selectedItem.equals("Suzuki")){
            return R.array.suzuki_models;
        }
        else if (selectedItem.equals("Toyota")){
            return R.array.toyota_models;
        }
        else if (selectedItem.equals("Volkswagen")){
            return R.array.volkswagen_models;
        }
        else if (selectedItem.equals("Volvo")){
            return R.array.volvo_models;
        }
        return R.array.empty_array;
    }

}
