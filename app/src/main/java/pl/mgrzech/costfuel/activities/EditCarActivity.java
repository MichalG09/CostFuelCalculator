package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.models.Car;

public class EditCarActivity  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int carIdForEdit;
    private CarDatabase carDatabase;
    private Car carForEdit = null;
    private TextView branchCar;
    private TextView modelCar;
    private ArrayAdapter<CharSequence> adapter;
    private Spinner spinnerFuelType;
    private Spinner spinnerPeriodTime;
    private String typeFuelEditCar;
    private String periodTimeEditCar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIncomingIntent();
        carDatabase = new CarDatabase(this);
        setContentView(R.layout.activity_edit_car);
        carForEdit = carDatabase.getCarById(carIdForEdit);

        branchCar = findViewById(R.id.branchEditCar);
        branchCar.setText(carForEdit.getMark());

        modelCar = findViewById(R.id.modelEditCar);
        modelCar.setText(carForEdit.getModel());

        spinnerFuelType = (Spinner) findViewById(R.id.spinnerTypeFuelCarEdit);
        createSpinner(spinnerFuelType, R.array.types_fuel, carForEdit.getFuelType());

        spinnerPeriodTime = (Spinner) findViewById(R.id.spinnerPeriodTimeEdit);
        createSpinner(spinnerPeriodTime, R.array.period_time, carForEdit.getPeriodTimeForCalculation());
    }

    private void createSpinner(Spinner spinner, int arrayId, String carData) {
        adapter = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(carForEdit.getFuelType()));
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(adapter.getPosition(carData));
    }

    /**
     * Methods get car id for which application will show all data
     * Metoda pobierająca car Id dla którego zostaną wyświetlona wszystkie informacje
     */
    private void getIncomingIntent(){
        if(getIntent().hasExtra("carId")){
            carIdForEdit = getIntent().getIntExtra("carId", 0);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.toString().contains("spinnerTypeFuelCarEdit")){
            typeFuelEditCar = adapterView.getItemAtPosition(i).toString();
        }
        else if (adapterView.toString().contains("spinnerPeriodTimeEdit")){
            periodTimeEditCar = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onSaveEditCar(View view) {
        String textErrorValidation = "";
        boolean correctValidation = true;

        if (typeFuelEditCar.isEmpty() || typeFuelEditCar.equals(getString(R.string.fuel_type_for_validate))){
            textErrorValidation = textErrorValidation + "Niepoprawnie wprowadzony rodzaj paliwa ! \n";
            correctValidation = false;
        }

        if (periodTimeEditCar.isEmpty() || periodTimeEditCar.equals(getString(R.string.choose_period_time))){
            textErrorValidation = textErrorValidation + "Niepoprawnie wprowadzony okres wyliczania kosztów ! \n";
            correctValidation = false;
        }

        if(correctValidation) {
            carForEdit.setFuelType(typeFuelEditCar);
            carForEdit.setPeriodTimeForCalculation(periodTimeEditCar);
            carDatabase.updateCar(carForEdit);
            Toast.makeText(this, "Poprawnie edytowano samochód !", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, CarActivity.class);
            intent.putExtra("carId", carForEdit.getId());
            startActivity(intent);
        }
        else{
            Toast.makeText(this, textErrorValidation, Toast.LENGTH_LONG).show();
        }
    }
}
