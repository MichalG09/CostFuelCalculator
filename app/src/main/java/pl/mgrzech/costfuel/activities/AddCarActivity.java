package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.models.Car;

public class AddCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String brandAddingCar = "";
    private String modelsAddingCar = "";
    private String typeFuelAddingCar = "";
    private Spinner spinnerBranchCar;
    private Spinner spinnerModelCar;
    private Spinner spinnerTypeFuel;

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

        spinnerBranchCar = (Spinner) findViewById(R.id.spinnerBranchCar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.brands_cars, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranchCar.setAdapter(adapter);
        spinnerBranchCar.setOnItemSelectedListener(this);
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

            spinnerModelCar = (Spinner) findViewById(R.id.spinnerModelCar);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    getListModelCarForBrand(brandAddingCar), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerModelCar.setAdapter(adapter);
            spinnerModelCar.setOnItemSelectedListener(this);
            modelsAddingCar = "";
            typeFuelAddingCar = "";

            Toast.makeText(parent.getContext(), "Wybierz model samochodu", Toast.LENGTH_LONG).show();

        }
        else if (parent.toString().contains("spinnerModelCar")){
            modelsAddingCar = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "Wybierz typ paliwa samochodu", Toast.LENGTH_LONG).show();

            spinnerTypeFuel = (Spinner) findViewById(R.id.spinnerTypeFuelCar);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.types_fuel, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTypeFuel.setAdapter(adapter);
            spinnerTypeFuel.setOnItemSelectedListener(this);
            typeFuelAddingCar = "";
        }
        else if (parent.toString().contains("spinnerTypeFuelCar")){
            typeFuelAddingCar = parent.getItemAtPosition(position).toString();
        }
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
            textErrorValidation = textErrorValidation + "Marka samochodu nie może być pusta ! \n";
            correctValidation = false;
        }
        if(modelsAddingCar.isEmpty()){
            textErrorValidation = textErrorValidation + "Model samochodu nie może być pusty ! \n";
            correctValidation = false;
        }
        if (typeFuelAddingCar.isEmpty()){
            textErrorValidation = textErrorValidation + "Rodzaj paliwa nie może być pusty !";
            correctValidation = false;
        }
        if(correctValidation) {
            carDatabase.addCar(new Car(brandAddingCar, modelsAddingCar, typeFuelAddingCar));
            Toast.makeText(this, "Poprawnie zapisano nowy samochód !", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AllCarsActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, textErrorValidation, Toast.LENGTH_LONG).show();
        }
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
