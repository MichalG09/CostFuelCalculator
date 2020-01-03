package pl.mgrzech.costfuel.activities;

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

import java.util.Arrays;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.models.Car;

import static androidx.appcompat.app.AlertDialog.*;

public class EditCarActivity  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int carIdForEdit;
    private Database database;
    private Car carForEdit = null;
    private String typeFuelEditCar;
    private String periodTimeEditCar;
    private String oldTypeFuelEditCar;
    private CalculateAvarageFuelAndCost calculateAvarageFuelAndCost = new CalculateAvarageFuelAndCost();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIncomingIntent();
        setContentView(R.layout.activity_edit_car);

        database = new Database(this);
        carForEdit = database.getCarById(carIdForEdit);
        oldTypeFuelEditCar = carForEdit.getFuelType();

        TextView branchCar = findViewById(R.id.branchEditCar);
        branchCar.setText(carForEdit.getMark());

        TextView modelCar = findViewById(R.id.modelEditCar);
        modelCar.setText(carForEdit.getModel());

        Spinner spinnerFuelType = findViewById(R.id.spinnerTypeFuelCarEdit);
        createSpinner(spinnerFuelType, R.array.types_fuel, carForEdit.getFuelType());

        Spinner spinnerPeriodTime = findViewById(R.id.spinnerPeriodTimeEdit);
        createSpinner(spinnerPeriodTime, R.array.period_time, carForEdit.getPeriodTimeForCalculation());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Methods creates spinner with basic parameters
     * @param spinner spinner to create
     * @param arrayId id array with data for spinner
     * @param carData value to show
     */
    private void createSpinner(Spinner spinner, int arrayId, String carData) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
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
        if(getIntent().hasExtra(getString(R.string.carIdIncommingIntent))){
            carIdForEdit = getIntent().getIntExtra(getString(R.string.carIdIncommingIntent), 0);
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

    /**
     * Metod validate edit car. If data is correct, car is save in database with new data. If data is incorrect, alarm message is show
     * Metoda waliduje edytowany samochód. Jeżeli dane są poprawne, samochód jest zapisywany. Jeżeli dane są niepoprawne, wyświetlany jest odpowiedni komunikat
     * @param view view
     */
    public void onSaveEditCar(View view) {
        String textErrorValidation = "";
        boolean correctValidation = true;

        if (typeFuelEditCar.isEmpty() || typeFuelEditCar.equals(getString(R.string.fuel_type_for_validate))){
            textErrorValidation = textErrorValidation + getString(R.string.edit_activity_incorrect_type_fuel);
            correctValidation = false;
        }

        if (periodTimeEditCar.isEmpty() || periodTimeEditCar.equals(getString(R.string.choose_period_time))){
            textErrorValidation = textErrorValidation + getString(R.string.edit_activity_incorrect_period);
            correctValidation = false;
        }

        if(correctValidation) {
            if(!oldTypeFuelEditCar.equals(typeFuelEditCar)) {
                if (!typeFuelEditCar.contains(oldTypeFuelEditCar)) {
                    final String[] temp = calculateAvarageFuelAndCost.fuelTypeForDeleting(oldTypeFuelEditCar, typeFuelEditCar);

                    Builder builder = new Builder(EditCarActivity.this);
                    builder.setTitle(getString(R.string.edit_activity_title_error_message));
                    builder.setMessage(getString(R.string.edit_activity_deleted_warning) + Arrays.toString(temp).replace("[", " ").replace("]", ""));

                    builder.setPositiveButton(getString(R.string.edit_activity_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            database = new Database(EditCarActivity.this);
                            for (String fuelTypeOne : temp) {
                                if (database.calcNumberFuelsForTypeFuel(carIdForEdit, fuelTypeOne) > 0)
                                    database.deleteOneTypeFuelsForCar(carIdForEdit, fuelTypeOne);
                            }
                            saveEditedCar();
                        }
                    });

                    builder.setNegativeButton(getString(R.string.edit_activity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
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

    /**
     * Method save edited car after validation new data
     * Meotda zapisuje edytowany samochód po walidacji danych
     */
    private void saveEditedCar() {
        carForEdit.setFuelType(typeFuelEditCar);
        carForEdit.setPeriodTimeForCalculation(periodTimeEditCar);
        database.updateCar(carForEdit);
        carForEdit = calculateAvarageFuelAndCost.recalculate(database, carForEdit);
        database.updateCar(carForEdit);
        Toast.makeText(this, getResources().getString(R.string.edit_activity_message_correct_save_edited_car), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, CarActivity.class);
        intent.putExtra(getString(R.string.carIdIncommingIntent), carForEdit.getId());
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
