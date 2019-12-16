package pl.mgrzech.costfuel.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class AddFuelActivity extends AppCompatActivity {

    private TextView dateFuelAddingFuelText;
    private String dateFuelAddingFuel = "";
    private TextView typeFuelAddingFuelText;
    private String typeFuelAddingFuel = "";
    private TextView costAddingFuelText;
    private double costAddingFuel;
    private TextView quantityAddingFuelText;
    private double quantityAddingFuel;
    private TextView mileageAddingFuelText;
    private int mileageAddingFuel;
    private int month;
    private int year;
    private int day;
    private int carIdForFuel;
    private Spinner typeFuelSpinner;
    private FuelDatabase fuelDatabase;
    private CarDatabase carDatabase;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Car car;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Methods is called on create this activity. It created field for all necessary data.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fuel);

        getIncomingIntent();

        dateFuelAddingFuelText = findViewById(R.id.insertDataFuel);
        costAddingFuelText = findViewById(R.id.insertCostFuel);
        quantityAddingFuelText = findViewById(R.id.insertQuantityFuel);
        mileageAddingFuelText = findViewById(R.id.insertMileageFuel);
        typeFuelSpinner = findViewById(R.id.insertTypeFuelSpinner);

        fuelDatabase = new FuelDatabase(this);
        carDatabase = new CarDatabase(this);
        car = carDatabase.getCarById(carIdForFuel);

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

                changeBackgroundColor(dateFuelAddingFuelText, Color.RED);
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
                changeBackgroundColor(costAddingFuelText, Color.RED);
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
                changeBackgroundColor(quantityAddingFuelText, Color.RED);
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
                changeBackgroundColor(mileageAddingFuelText, Color.RED);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void changeBackgroundColor(TextView editText, int color) {

        try{
            ColorDrawable mileageAddingFuelTextColor = (ColorDrawable) editText.getBackground();
            if(mileageAddingFuelTextColor.getColor() == color){
                editText.setBackgroundColor(ContextCompat.getColor(AddFuelActivity.this, R.color.background));
            }
        }catch (Exception e){
        }
    }

    /**
     * Method check place decimal. If decimal places is more than 2, it delete number after second decimal pleaces.
     * Metoda sprawdza ilość miejsc dziesięnych. Jeżeli miejsc jest więcej niż 2 to usuwa liczby po drugim miejscu dziesiętnym.
     * @param value
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
        if(getIntent().hasExtra("carId")){
            carIdForFuel = getIntent().getIntExtra("carId",0);
        }
    }

    /**
     * Method of saving a new fuel with data from layout add new fuel. It validated data and saved new fuel in database
     * and calculate a average cost fuels per 100 km and average fuel consumption per 100 km for new data.
     * Metoda zapisywania nowego tankowania z grafiki dodawania nowego tankowania. Sprawdze poprawność danych i zapisuje nowe tankowanie do bazy danych.
     * Przelicza średnia spalania na 100 km i średni koszt paliwa na 100 km dla nowych danych.
     * @param view
     */
    public void saveFuel(View view) {

        String errorMessage = "";
        typeFuelAddingFuel = String.valueOf(typeFuelSpinner.getSelectedItem());

        if(dateFuelAddingFuel.isEmpty()){
            errorMessage += "Data nie może być pusta ! \n";
            dateFuelAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        String costAddingFuelStr = costAddingFuelText.getText().toString();
        if(!costAddingFuelStr.isEmpty()){
            costAddingFuel = Double.parseDouble(costAddingFuelStr);
        }
        else{
            errorMessage += "Koszt tankowania nie może być pusty ! \n";
            costAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        String quantityAddingFuelStr = quantityAddingFuelText.getText().toString();
        if(!quantityAddingFuelStr.isEmpty()){
            quantityAddingFuel = Double.parseDouble(quantityAddingFuelStr);
        }
        else{
            errorMessage += "Ilość paliwa nie może być pusta ! \n";
            quantityAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        String mileageAddingFuelStr = mileageAddingFuelText.getText().toString();
        if(!mileageAddingFuelStr.isEmpty()){
            mileageAddingFuel = Integer.parseInt(mileageAddingFuelStr);
        }
        else{
            errorMessage += "Przebieg nie może być pusty ! \n";
            mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
        }

        if(errorMessage.isEmpty()){

            Fuel newFuel = new Fuel(dateFuelAddingFuel, typeFuelAddingFuel, costAddingFuel, quantityAddingFuel, mileageAddingFuel);

            if(checkCorrectMileageFuel(newFuel, fuelDatabase)){
                fuelDatabase.addFuel(newFuel, String.valueOf(carIdForFuel));
                Intent intent = new Intent(this, CarActivity.class);
                clearView();
                Car car = carDatabase.getCarById(carIdForFuel);
                car = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, car);
                carDatabase.updateCar(car);
                intent.putExtra("carId", car.getId());
                startActivity(intent);

                Toast.makeText(this, "Poprawnie zapisano tankowanie !", Toast.LENGTH_LONG).show();
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
     * @param newFuel
     * @param fuelDatabase
     * @return
     */
    private boolean checkCorrectMileageFuel(Fuel newFuel, FuelDatabase fuelDatabase) {

        for(Fuel fuel : fuelDatabase.getAllFuelsForCarId(String.valueOf(carIdForFuel))){
            if(newFuel.getFuelType().equals(fuel.getFuelType())){
                if((getDateFromFuel(newFuel).before(getDateFromFuel(fuel))) && newFuel.getMileage() > fuel.getMileage()){
                    Toast.makeText(this, "Niepoprawny przebieg. Obecne tankowanie ma większy przebieg niż póżniejsze tankowania.", Toast.LENGTH_LONG).show();
                    mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
                    return false;
                }
                if((getDateFromFuel(newFuel).after(getDateFromFuel(fuel))) && newFuel.getMileage() < fuel.getMileage()){
                    Toast.makeText(this, "Niepoprawny przebieg. Obecne tankowanie ma mniejszy przebieg niż wcześniejsze tankowania.", Toast.LENGTH_LONG).show();
                    mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
                    return false;
                }
                if(newFuel.getMileage() == fuel.getMileage()){
                    Toast.makeText(this, "Istnieje tankowanie z takim samym przebiegiem.", Toast.LENGTH_LONG).show();
                    mileageAddingFuelText.setBackgroundColor(ContextCompat.getColor(this, R.color.backdroundForValidationError));
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Method return date guel in Date (from String)
     * Meotda zwraca date tankowania w formacie Date (ze Stringa)
     * @param fuel
     * @return
     */
    private Date getDateFromFuel(Fuel fuel) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;

        try{
            date = simpleDateFormat.parse(fuel.getDate());
        }
        catch(ParseException pe) {
        }

        return date;
    }

    /**
     * Method clear all field with input data for fuel after correct added new fuel.
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
     * @param fuelType
     * @return
     */
    private String[] typeFuelForChoosenCar(String fuelType) {
        if(fuelType.equals("PB")){
            return new String[]{"PB"};
        }
        else if(fuelType.equals("ON")){
            return new String[]{"ON"};
        }
        else if(fuelType.equals("PB + LPG")){
            return new String[]{"PB", "LPG"};
        }
        else{
            return new String[]{"ON", "AddBlue"};
        }
    }
}
