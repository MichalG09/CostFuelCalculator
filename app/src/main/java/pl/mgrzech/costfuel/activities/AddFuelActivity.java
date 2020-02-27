package pl.mgrzech.costfuel.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.database.DatabaseSingleton;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class AddFuelActivity extends AppCompatActivity {

    private TextView dateFuelAddingFuelText;
    private String dateFuelAddingFuel = "";
    private TextView costAddingFuelText;
    private double costAddingFuel;
    private TextView quantityAddingFuelText;
    private double quantityAddingFuel;
    private TextView mileageAddingFuelText;
    private int mileageAddingFuel;
    private int carIdForFuel;
    private Spinner typeFuelSpinner;
    private Database database;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Car car;

    /**
     * Methods is called on create this activity. It created field for all necessary data.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fuel);
        getIncomingIntent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        dateFuelAddingFuelText = findViewById(R.id.insertDataFuel);
        costAddingFuelText = findViewById(R.id.insertCostFuel);
        quantityAddingFuelText = findViewById(R.id.insertQuantityFuel);
        mileageAddingFuelText = findViewById(R.id.insertMileageFuel);
        typeFuelSpinner = findViewById(R.id.insertTypeFuelSpinner);

        database = DatabaseSingleton.getInstance(this);
        car = database.getCarById(carIdForFuel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeFuelForChoosenCar(car.getFuelType()));
        typeFuelSpinner.setAdapter(adapter);

        dateFuelAddingFuel = simpleDateFormat.format(new Date());
        dateFuelAddingFuelText.setText(dateFuelAddingFuel);
        dateFuelAddingFuelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddFuelActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                changeBackgroundColor(dateFuelAddingFuelText);
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dayStr = String.valueOf(day);
                String monthStr = String.valueOf(month);
                if(day < 10){
                    dayStr = "0" + dayStr;

                }
                if(month < 10){
                    monthStr = "0" + monthStr;
                }
                dateFuelAddingFuel = dayStr + "." + monthStr + "." + year;

                dateFuelAddingFuelText.setText(dateFuelAddingFuel);
            }
        };

        costAddingFuelText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                changeBackgroundColor(costAddingFuelText);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkDecimalPleaces(s);
            }
        });

        quantityAddingFuelText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                changeBackgroundColor(quantityAddingFuelText);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkDecimalPleaces(s);
            }
        });

        mileageAddingFuelText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeBackgroundColor(mileageAddingFuelText);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mileageAddingFuelText.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Methods change background color for basic TextView
     * Metoda zmienia kolor TextView na kolor bazowy TextView
     * @param editText editText where background color will change
     *
     */
    private void changeBackgroundColor(TextView editText) {

        try{
            ColorDrawable mileageAddingFuelTextColor = (ColorDrawable) editText.getBackground();
            if(mileageAddingFuelTextColor.getColor() == Color.RED){
                editText.setBackgroundColor(ContextCompat.getColor(this, R.color.basicBackground));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method check place decimal. If decimal places is more than 2, it delete number after second decimal pleaces.
     * Metoda sprawdza ilość miejsc dziesięnych. Jeżeli miejsc jest więcej niż 2 to usuwa liczby po drugim miejscu dziesiętnym.
     * @param value value insert into input field
     */
    public void checkDecimalPleaces(Editable value){
        String valueStr = value.toString ();
        int indexDecimalPlaces = valueStr.indexOf( "." );

        if (indexDecimalPlaces <= 0) {
            return;
        }
        if (valueStr.length() - indexDecimalPlaces - 1 > 2)
        {
            value.delete(indexDecimalPlaces + 3, indexDecimalPlaces + 4);
        }
    }

    /**
     * The method got the Id of the car for which fuel was added
     * Meotda pobiera id samochodu, dla którego dodawane jest tankowanie.
     */
    private void getIncomingIntent(){
        if(getIntent().hasExtra(getString(R.string.carIdIncommingIntent)))
            carIdForFuel = getIntent().getIntExtra(getString(R.string.carIdIncommingIntent), 0);
    }

    /**
     * Method of saving a new fuel with data from layout add new fuel. It validated data and saved new fuel in database
     * and calculate a average cost fuels per 100 km and average fuel consumption per 100 km for new data.
     * Metoda zapisywania nowego tankowania z grafiki dodawania nowego tankowania. Sprawdze poprawność danych i zapisuje nowe tankowanie do bazy danych.
     * Przelicza średnia spalania na 100 km i średni koszt paliwa na 100 km dla nowych danych.
     * @param view view
     */
    public void saveFuel(View view) {

        String errorMessage = "";
        String typeFuelAddingFuel = String.valueOf(typeFuelSpinner.getSelectedItem());
        CalculateAvarageFuelAndCost calculateAvarageFuelAndCost = new CalculateAvarageFuelAndCost();

        if(dateFuelAddingFuel.isEmpty()){
            errorMessage += getString(R.string.add_fuel_activity_incorrect_date);
            dateFuelAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        String costAddingFuelStr = costAddingFuelText.getText().toString();
        if(!costAddingFuelStr.isEmpty()){
            costAddingFuel = Double.parseDouble(costAddingFuelStr);
        }
        else{
            errorMessage += getString(R.string.add_fuel_activity_incorrect_cost_fuel);
            costAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        String quantityAddingFuelStr = quantityAddingFuelText.getText().toString();
        if(!quantityAddingFuelStr.isEmpty()){
            quantityAddingFuel = Double.parseDouble(quantityAddingFuelStr);
        }
        else{
            errorMessage += getString(R.string.add_fuel_activity_incorrect_quantity_fuel);
            quantityAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        String mileageAddingFuelStr = mileageAddingFuelText.getText().toString();
        if(!mileageAddingFuelStr.isEmpty()){
            mileageAddingFuel = Integer.parseInt(mileageAddingFuelStr);
        }
        else{
            errorMessage += getString(R.string.add_fuel_activity_incorrect_mileage);
            mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        if(errorMessage.isEmpty()){

            Fuel newFuel = new Fuel(dateFuelAddingFuel, typeFuelAddingFuel, costAddingFuel, quantityAddingFuel, mileageAddingFuel, carIdForFuel);

            if(checkCorrectMileageFuel(newFuel, database)){
                database.addFuel(newFuel);
                clearView();
                car = database.getCarById(carIdForFuel);
                car = calculateAvarageFuelAndCost.recalculate(database, car);
                database.updateCar(car);
                Intent intent = new Intent(this, CarActivity.class);
                intent.putExtra(getString(R.string.carIdIncommingIntent), car.getId());
                startActivity(intent);

                Toast.makeText(this, getString(R.string.add_fuel_activity_correct_fuel), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method check correct mileage fuel. Earlier fuel can not have higher mileage. Later fuel can not have lower mileage.
     * Meotda sprawdza czy przebieg tankowania jest poprawny. Wcześniejsze tankowanie nie może mieć wiekszego przebiegu.
     * Późniejsze tankowanie nie może mieć mniejszego tankowania.
     * @param newFuel Fuel for check to correct milleage
     * @param fuelDatabase database witj fuels
     * @return if correct return true, if incorrect return false
     */
    private boolean checkCorrectMileageFuel(Fuel newFuel, Database fuelDatabase) {

        for(Fuel fuel : fuelDatabase.getAllFuelsForCarId(String.valueOf(carIdForFuel))){
            if(newFuel.getFuelType().equals(fuel.getFuelType())){
                if((getDateFromFuel(newFuel).before(getDateFromFuel(fuel))) && newFuel.getMileage() > fuel.getMileage()){
                    Toast.makeText(this, getString(R.string.add_fuel_activity_incorrect_mileage_too_high), Toast.LENGTH_LONG).show();
                    mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
                    return false;
                }
                if((getDateFromFuel(newFuel).after(getDateFromFuel(fuel))) && newFuel.getMileage() < fuel.getMileage()){
                    Toast.makeText(this, getString(R.string.add_fuel_activity_incorrect_mileage_too_low), Toast.LENGTH_LONG).show();
                    mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
                    return false;
                }
                if(newFuel.getMileage() == fuel.getMileage()){
                    Toast.makeText(this, getResources().getString(R.string.add_fuel_activity_incorrect_mileage_doubled), Toast.LENGTH_LONG).show();
                    mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method return date fuel in Date (from String)
     * Meotda zwraca date tankowania w formacie Date (ze Stringa)
     * @param fuel fuel for get date
     * @return date
     */
    private Date getDateFromFuel(Fuel fuel) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        Date date = null;

        try{
            date = simpleDateFormat.parse(fuel.getDate());
        }
        catch(ParseException pe) {
            pe.printStackTrace();
        }
        return date;
    }

    /**
     * Method clear all input field for fuel after correct added new fuel.
     * metoda czyści wszystkie pola do wprowadzania danych po poprawnym zapisaniu tankowania.
     */
    private void clearView(){
        dateFuelAddingFuelText.setText("");
        costAddingFuelText.setText("");
        quantityAddingFuelText.setText("");
        mileageAddingFuelText.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method check type fuel for choosen car.
     * Metoda sprawdza typ paliwa dla danego samochodu
     * @param fuelType fuel type
     * @return return table with single fuel type
     */
    private String[] typeFuelForChoosenCar(String fuelType) {
        switch (fuelType) {
            case "PB":
                return new String[]{"PB"};
            case "ON":
                return new String[]{"ON"};
            case "PB + LPG":
                return new String[]{"PB", "LPG"};
            case "ON + AddBlue":
                return new String[]{"ON", "AddBlue"};
        }
        return new String[]{""};
    }
}
