package pl.mgrzech.costfuel.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.database.FuelDatabase;
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
    private String oldTypeFuelEditCar;
    private AlertDialog.Builder builder;
    private FuelDatabase fuelDatabase;
    private Context mContext;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIncomingIntent();
        setContentView(R.layout.activity_edit_car);

        mContext = this;
        carDatabase = new CarDatabase(mContext);
        fuelDatabase = new FuelDatabase(mContext);
        carForEdit = carDatabase.getCarById(carIdForEdit);
        oldTypeFuelEditCar = carForEdit.getFuelType();

        branchCar = findViewById(R.id.branchEditCar);
        branchCar.setText(carForEdit.getMark());

        modelCar = findViewById(R.id.modelEditCar);
        modelCar.setText(carForEdit.getModel());

        spinnerFuelType = (Spinner) findViewById(R.id.spinnerTypeFuelCarEdit);
        createSpinner(spinnerFuelType, R.array.types_fuel, carForEdit.getFuelType());

        spinnerPeriodTime = (Spinner) findViewById(R.id.spinnerPeriodTimeEdit);
        createSpinner(spinnerPeriodTime, R.array.period_time, carForEdit.getPeriodTimeForCalculation());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void createSpinner(Spinner spinner, int arrayId, String carData) {
        adapter = ArrayAdapter.createFromResource(mContext,
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

        String[] str;
        String[] str2;
        if(correctValidation) {
            if(!oldTypeFuelEditCar.equals(typeFuelEditCar)) {
                if (!typeFuelEditCar.contains(oldTypeFuelEditCar)) {
                    final String[] temp = CalculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuelEditCar, typeFuelEditCar);

                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("UWAGA !");
                    builder.setMessage("Zmiana rodzaju paliwa spowoduje usunięcie tankowań dla paliwa: " + Arrays.toString(temp));

                    builder.setPositiveButton("POTWIERDŹ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fuelDatabase = new FuelDatabase(mContext);
                            for (String fuelTypeOne : temp) {
                                if (fuelDatabase.calcNumberFuelsForTypeFuel(carIdForEdit, fuelTypeOne) > 0)
                                    fuelDatabase.deleteOneTypeFuelsForCar(carIdForEdit, fuelTypeOne);
                            }
                            saveEditedCar();
                        }
                    });

                    builder.setNegativeButton("ANULUJ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }
                else{
                    saveEditedCar();
                }
            }
            else {
                saveEditedCar();
            }
        }
        else{
            Toast.makeText(this, textErrorValidation, Toast.LENGTH_LONG).show();
        }
    }

    private void saveEditedCar() {
        carForEdit.setFuelType(typeFuelEditCar);
        carForEdit.setPeriodTimeForCalculation(periodTimeEditCar);
        carDatabase.updateCar(carForEdit);
        carForEdit = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, carForEdit);
        carDatabase.updateCar(carForEdit);
        Toast.makeText(mContext, "Poprawnie edytowano samochód !", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(mContext, CarActivity.class);
        intent.putExtra("carId", carForEdit.getId());
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
