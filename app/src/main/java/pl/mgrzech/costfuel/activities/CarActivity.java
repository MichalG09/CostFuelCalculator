package pl.mgrzech.costfuel.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Car;

public class CarActivity extends AppCompatActivity {

    private int carIdForShow;
    private TextView carMarkInfo;
    private TextView carModelInfo;
    private TextView carTypeFuelInfo;
    private String averageFuelConsumptionForFisrtFuel;
    private String averageFuelConsumptionForSecondFuel;
    private String averageFuelCostForFisrtFuel;
    private String averageFuelCostForSecondFuel;
    private TextView carAverageFuelCostInfo;
    private CarDatabase carDatabase;
    private FuelDatabase fuelDatabase;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Car car = null;
    private Context mContext;
    private DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    private String noDataMessage;
    private String errorDataMessage;

    private LinearLayout averageFuelConsumptionLinearLayout;
    private ImageButton imageButtonChangeShowData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        getIncomingIntent();
        mContext = this;

        noDataMessage = getResources().getString(R.string.no_data);
        errorDataMessage = getResources().getString(R.string.error_data);

        carDatabase = new CarDatabase(mContext);
        car = carDatabase.getCarById(carIdForShow);

        carMarkInfo = findViewById(R.id.carActivityCarMark);
        carMarkInfo.setText(car.getMark());

        carModelInfo = findViewById(R.id.carActivityCarModel);
        carModelInfo.setText(car.getModel());

        carTypeFuelInfo = findViewById(R.id.carActivityCarFuelType);
        carTypeFuelInfo.setText(car.getFuelType());

        carAverageFuelCostInfo = findViewById(R.id.carActivityCarAverageCostFuel);
        averageFuelConsumptionLinearLayout = findViewById(R.id.averangeFuelConsumptionLayout);

        if(car.getFuelType().contains("+")){
            imageButtonChangeShowData = findViewById(R.id.changeShowData);
            imageButtonChangeShowData.setVisibility(View.VISIBLE);
            imageButtonChangeShowData.setOnClickListener(clickForChangeViewDataForTwoPositions);
        }

        getDataInOnePosition();
        infoForFuelsInOnePosition();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private TextView createRowDataFuel(String temp) {
        TextView result = new TextView(this);
        result.setGravity(Gravity.CENTER);
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels3 = (int) (50 * scale + 0.5f);
        result.setWidth(pixels3);
        result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        result.setText(temp);
        return result;
    }

    /**
     * Methods for click add fuel
     * Metoda dla klikniecie dodaj tankowanie
     *
     * @param view
     */
    public void onAddFuel(View view) {
        Intent intent = new Intent(this, AddFuelActivity.class);
        intent.putExtra("carId", carIdForShow);
        startActivity(intent);
    }

    /**
     * Methods for click show all fuels
     * Metoda dla klikniecie pokaż wszystkie tankowania
     *
     * @param view
     */
    public void onShowAllFuels(View view) {
        Intent intent = new Intent(this, AllFuelsActivity.class);
        intent.putExtra("carId", carIdForShow);
        startActivity(intent);
    }

    /**
     * Methods get car id for which application will show all data
     * Metoda pobierająca car Id dla którego zostaną wyświetlona wszystkie informacje
     */
    private void getIncomingIntent(){
        if(getIntent().hasExtra("carId")){
            carIdForShow = getIntent().getIntExtra("carId", 0);
        }
    }

    public void onDeleteCar(View view) {

        builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Na pewno chcesz usunąć samochód " + car.getMark() + " " + car.getModel() + "?");

        builder.setPositiveButton("USUŃ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

             fuelDatabase = new FuelDatabase(mContext);
             fuelDatabase.deleteFuelsForCar(car.getId());

             if(carDatabase.deleteCar(car) > 0){
                 Toast.makeText(mContext, "Poprawnie usunięto samochód i tankowania !", Toast.LENGTH_LONG).show();
                 Intent intent = new Intent(mContext, AllCarsActivity.class);
                 startActivity(intent);
             }
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

    public void onEditCar(View view) {
        Intent intent = new Intent(this, EditCarActivity.class);
        intent.putExtra("carId", carIdForShow);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private void infoForEachFuels() {
        String[] fuelsType = car.getFuelType().split(getResources().getString(R.string.regex_for_split_fuel_types));
        String firstFuel = fuelsType[0].trim();
        String secondFuel = fuelsType[1].trim();

        averageFuelConsumptionLinearLayout.removeAllViews();
        imageButtonChangeShowData.setOnClickListener(clickForChangeViewDataForOnePositions);

        LinearLayout linearLayoutForAllInformationAboutFuelConsumption = getLinearLayoutForAllInformation();
        TextView textViewForNameFirstFuelType = getTextViewForDescrptionFuelType(firstFuel);
        TextView textViewForNameSecondFuelType = getTextViewForDescrptionFuelType(secondFuel);
        TextView textViewForAverageConsumptionSecondFuel = getTextViewForAverageDataCar(averageFuelConsumptionForSecondFuel);
        TextView textViewForAverageConsumptionFirstFuel = getTextViewForAverageDataCar(averageFuelConsumptionForFisrtFuel);

        linearLayoutForAllInformationAboutFuelConsumption.addView(textViewForNameFirstFuelType);
        linearLayoutForAllInformationAboutFuelConsumption.addView(textViewForAverageConsumptionFirstFuel);
        linearLayoutForAllInformationAboutFuelConsumption.addView(textViewForNameSecondFuelType);
        linearLayoutForAllInformationAboutFuelConsumption.addView(textViewForAverageConsumptionSecondFuel);

        averageFuelConsumptionLinearLayout.addView(linearLayoutForAllInformationAboutFuelConsumption);
    }

    private LinearLayout getLinearLayoutForAllInformation() {
        LinearLayout resultLinearLayout = new LinearLayout(this);
        resultLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        resultLinearLayout.setWeightSum(100);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                100);
        resultLinearLayout.setLayoutParams(param);
        return resultLinearLayout;
    }

    private TextView getTextViewForAverageDataCar(String valueToShow) {
        TextView resultTextView = new TextView(this);
        resultTextView.setGravity(Gravity.CENTER);
        resultTextView.setLayoutParams(getLayoutParamsTableRow(0,TableRow.LayoutParams.WRAP_CONTENT, 43));
        if(valueToShow.equals(noDataMessage)){
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
        else {
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        }
        resultTextView.setText(valueToShow);
        return resultTextView;
    }

    private TextView getTextViewForDescrptionFuelType(String fuelType) {
        TextView resultTextView = new TextView(this);
        resultTextView.setTextSize(10);
        resultTextView.setText(fuelType);
        resultTextView.setGravity(Gravity.TOP | Gravity.RIGHT);
        resultTextView.setLayoutParams(getLayoutParamsTableRow(0,TableRow.LayoutParams.MATCH_PARENT, 7));
        return  resultTextView;
    }

    private TableRow.LayoutParams getLayoutParamsTableRow(int layout_width, int layout_height, int weight) {
        if(weight == 0){
            return new TableRow.LayoutParams(layout_width, layout_height);
        }
        return new TableRow.LayoutParams(layout_width, layout_height, weight);
    }

    private View.OnClickListener clickForChangeViewDataForTwoPositions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDataInTwoPosition();
            infoForEachFuels();
        }
    };

    private View.OnClickListener clickForChangeViewDataForOnePositions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDataInOnePosition();
            averageFuelConsumptionLinearLayout.removeAllViews();
            infoForFuelsInOnePosition();
        }
    };

    private void infoForFuelsInOnePosition() {
        TextView carAverageFuelInfo = createRowDataFuel(averageFuelConsumptionForFisrtFuel);

        TableRow.LayoutParams paramsTextLPGtesmp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        carAverageFuelInfo.setLayoutParams(paramsTextLPGtesmp);
        averageFuelConsumptionLinearLayout.addView(carAverageFuelInfo);

        if(car.getFuelType().contains("+")){
            imageButtonChangeShowData.setOnClickListener(clickForChangeViewDataForTwoPositions);
        }
    }

    private void getDataInOnePosition(){

        if(car.getFuelType().contains("+")){
            if(car.getAverageConsumptionFirstFuel() > 0 && car.getAverageConsumptionSecondFuel() > 0){
                averageFuelConsumptionForFisrtFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel() + car.getAverageConsumptionSecondFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() > 0 && car.getAverageConsumptionSecondFuel() <= 0){
                averageFuelConsumptionForFisrtFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() <= 0 && car.getAverageConsumptionSecondFuel() > 0){
                averageFuelConsumptionForFisrtFuel = decimalFormat.format(car.getAverageConsumptionSecondFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() == 0 && car.getAverageConsumptionSecondFuel() == 0){
                averageFuelConsumptionForFisrtFuel = noDataMessage;
            }
            else {
                averageFuelConsumptionForFisrtFuel = noDataMessage;
            }

            if(car.getAverageCostFirstFuel() > 0 && car.getAverageCostSecondFuel() > 0){
                carAverageFuelCostInfo.setText(decimalFormat.format((car.getAverageCostFirstFuel() + car.getAverageCostSecondFuel()) / 2));
            }
            else if(car.getAverageCostFirstFuel() > 0 && car.getAverageCostSecondFuel() <= 0){
                carAverageFuelCostInfo.setText(decimalFormat.format(car.getAverageCostFirstFuel()));
            }
            else if(car.getAverageCostFirstFuel() <= 0 && car.getAverageCostSecondFuel() > 0){
                carAverageFuelCostInfo.setText(decimalFormat.format(car.getAverageCostSecondFuel()));
            }
            else if(car.getAverageCostFirstFuel() == 0 && car.getAverageCostSecondFuel() == 0){
                carAverageFuelCostInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                carAverageFuelCostInfo.setText(noDataMessage);
            }
            else {
                carAverageFuelCostInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                carAverageFuelCostInfo.setText("Błędne Dane");
            }
        }
        else{
            if(car.getAverageConsumptionFirstFuel() > 0){
                averageFuelConsumptionForFisrtFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() == 0){
                averageFuelConsumptionForFisrtFuel = noDataMessage;
            }
            else{
                carAverageFuelCostInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                carAverageFuelCostInfo.setText("Błędne Dane");
            }

            if(car.getAverageCostFirstFuel() > 0){
                carAverageFuelCostInfo.setText(decimalFormat.format(car.getAverageCostFirstFuel()));
            }
            else if(car.getAverageCostFirstFuel() == 0){
                carAverageFuelCostInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                carAverageFuelCostInfo.setText(noDataMessage);
            }
            else{
                carAverageFuelCostInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                carAverageFuelCostInfo.setText("Błędne Dane");
            }
        }
    }
    private void getDataInTwoPosition(){
        if(car.getAverageConsumptionFirstFuel() > 0){
            averageFuelConsumptionForFisrtFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel());
        }
        else{
            averageFuelConsumptionForFisrtFuel = noDataMessage;
        }

        if(car.getAverageConsumptionSecondFuel() > 0){
            averageFuelConsumptionForSecondFuel = decimalFormat.format(car.getAverageConsumptionSecondFuel());
        }
        else{
            averageFuelConsumptionForSecondFuel = noDataMessage;
        }

        if(car.getAverageCostFirstFuel() > 0){
            averageFuelCostForFisrtFuel = decimalFormat.format(car.getAverageCostFirstFuel());
        }
        else{
            averageFuelCostForFisrtFuel = noDataMessage;
        }

        if(car.getAverageCostSecondFuel() > 0){
            averageFuelCostForSecondFuel = decimalFormat.format(car.getAverageCostSecondFuel());
        }
        else{
            averageFuelCostForSecondFuel = noDataMessage;
        }
    }
}
