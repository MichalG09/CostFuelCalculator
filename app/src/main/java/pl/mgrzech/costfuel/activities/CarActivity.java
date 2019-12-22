package pl.mgrzech.costfuel.activities;

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

/**
 * Start Activity
 */
public class CarActivity extends AppCompatActivity {

    private int carIdForShow;
    private String averageFuelConsumptionForFirstFuel;
    private String averageFuelConsumptionForSecondFuel;
    private String averageFuelCostForFirstFuel;
    private String averageFuelCostForSecondFuel;
    private CarDatabase carDatabase;
    private FuelDatabase fuelDatabase;
    private Car car;
    private DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    private String noDataMessage;
    private String errorDataMessage;
    private LinearLayout averageFuelConsumptionLinearLayout;
    private ImageButton imageButtonChangeShowDataForFuelConsumption;
    private String charToCheckTwoTypesFuel = "+";
    private String firstFuel;
    private String secondFuel;
    private LinearLayout averageFuelCostLinearLayout;
    private ImageButton imageButtonChangeShowDataForFuelCost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        getIncomingIntent();

        noDataMessage = getResources().getString(R.string.no_data);
        errorDataMessage = getResources().getString(R.string.error_data);

        carDatabase = new CarDatabase(this);
        car = carDatabase.getCarById(carIdForShow);

        TextView carMarkInfo = findViewById(R.id.carActivityCarMark);
        carMarkInfo.setText(car.getMark());

        TextView carModelInfo = findViewById(R.id.carActivityCarModel);
        carModelInfo.setText(car.getModel());

        TextView carTypeFuelInfo = findViewById(R.id.carActivityCarFuelType);
        carTypeFuelInfo.setText(car.getFuelType());

        averageFuelConsumptionLinearLayout = findViewById(R.id.averangeFuelConsumptionLayout);
        averageFuelCostLinearLayout = findViewById(R.id.averangeFuelCostLayout);

        if(car.getFuelType().contains(charToCheckTwoTypesFuel)){
            imageButtonChangeShowDataForFuelConsumption = findViewById(R.id.changeShowDataForFuelConsumption);
            imageButtonChangeShowDataForFuelConsumption.setVisibility(View.VISIBLE);
            imageButtonChangeShowDataForFuelConsumption.setOnClickListener(clickForChangeFuelConsumptionForTwoPositions);

            imageButtonChangeShowDataForFuelCost = findViewById(R.id.changeShowDataForFuelCost);
            imageButtonChangeShowDataForFuelCost.setVisibility(View.VISIBLE);
            imageButtonChangeShowDataForFuelCost.setOnClickListener(clickForChangeFuelCostDataForTwoPositions);

            String[] fuelsType = car.getFuelType().split(getResources().getString(R.string.regex_for_split_fuel_types));
            firstFuel = fuelsType[0].trim();
            secondFuel = fuelsType[1].trim();
        }

        getAverageFuelConsumptionToShowInOnePosition();
        getAverageFuelCostToShowForOnePosition();
        showAverageFuelConsumptionInOnePosition();
        showAverageCostFuelInOnePosition();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Methods get car id for which application will show all data
     * Metoda pobierająca car Id dla którego zostaną wyświetlona wszystkie informacje
     */
    private void getIncomingIntent(){
        if(getIntent().hasExtra(getString(R.string.carIdIncommingIntent))){
            carIdForShow = getIntent().getIntExtra(getString(R.string.carIdIncommingIntent), 0);
        }
    }

    /**
     * Methods for click add fuel
     * Metoda dla klikniecie dodaj tankowanie
     *
     * @param view view
     */
    public void onAddFuel(View view) {
        Intent intent = new Intent(this, AddFuelActivity.class);
        intent.putExtra(getString(R.string.carIdIncommingIntent), carIdForShow);
        startActivity(intent);
    }

    /**
     * Methods for click show all fuels
     * Metoda dla klikniecie pokaż wszystkie tankowania
     *
     * @param view view
     */
    public void onShowAllFuels(View view) {
        Intent intent = new Intent(this, AllFuelsActivity.class);
        intent.putExtra(getString(R.string.carIdIncommingIntent), carIdForShow);
        startActivity(intent);
    }

    /**
     * Methods for click delete Car
     * Metoda dla klikniecie usuń samochód
     *
     * @param view view
     */
    public void onDeleteCar(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(getResources().getString(R.string.car_activity_message_to_confirm_delete_car) + " "+ car.getMark() + " " + car.getModel() + "?");

        builder.setPositiveButton(getResources().getString(R.string.activity_car_command_delete_car), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

             fuelDatabase = new FuelDatabase(CarActivity.this);
             fuelDatabase.deleteFuelsForCar(car.getId());

             if(carDatabase.deleteCar(car) > 0){
                 Toast.makeText(CarActivity.this, getResources().getString(R.string.car_activity_message_deleted_car), Toast.LENGTH_LONG).show();
                 Intent intent = new Intent(CarActivity.this, AllCarsActivity.class);
                 startActivity(intent);
             }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.car_activity_command_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Methods for click edit car
     * Metoda dla klikniecie edytuj samochód
     *
     * @param view view
     */
    public void onEditCar(View view) {
        Intent intent = new Intent(this, EditCarActivity.class);
        intent.putExtra(getString(R.string.carIdIncommingIntent), carIdForShow);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method creates two pleaces for show averages fuel consumption for each fuel type
     * Metoda tworzy dwa miejsca dla średniego spalania dla każdego rodzaju paliwa
     */
    private void showAverageConsumptionFuelInTwoPosition() {

        averageFuelConsumptionLinearLayout.removeAllViews();
        imageButtonChangeShowDataForFuelConsumption.setOnClickListener(clickForChangeFuelConsumptionForOnePositions);

        LinearLayout linearLayoutForTwoPositionFuelConsumption = getLinearLayoutToShowAverageValues();
        TextView textViewForNameFirstFuelType = getTextViewForNameFuelType(firstFuel);
        TextView textViewForNameSecondFuelType = getTextViewForNameFuelType(secondFuel);
        TextView textViewForAverageConsumptionSecondFuel = getTextViewForAverageValue(averageFuelConsumptionForSecondFuel);
        TextView textViewForAverageConsumptionFirstFuel = getTextViewForAverageValue(averageFuelConsumptionForFirstFuel);

        linearLayoutForTwoPositionFuelConsumption.addView(textViewForNameFirstFuelType);
        linearLayoutForTwoPositionFuelConsumption.addView(textViewForAverageConsumptionFirstFuel);
        linearLayoutForTwoPositionFuelConsumption.addView(textViewForNameSecondFuelType);
        linearLayoutForTwoPositionFuelConsumption.addView(textViewForAverageConsumptionSecondFuel);

        averageFuelConsumptionLinearLayout.addView(linearLayoutForTwoPositionFuelConsumption);
    }

    /**
     * Method creates two pleaces for show averages fuel cost for each fuel type
     * Metoda tworzy dwa miejsca dla średniego kosztu dla każdego rodzaju paliwa
     */
    private void showAverageCostFuelInTwoPosition() {
        averageFuelCostLinearLayout.removeAllViews();
        imageButtonChangeShowDataForFuelCost.setOnClickListener(clickForChangeFuelCostDataForOnePositions);

        LinearLayout linearLayoutForTwoPositionFuelCost = getLinearLayoutToShowAverageValues();
        TextView textViewForNameFirstFuelType = getTextViewForNameFuelType(firstFuel);
        TextView textViewForNameSecondFuelType = getTextViewForNameFuelType(secondFuel);
        TextView textViewForAverageCostFirstFuel = getTextViewForAverageValue(averageFuelCostForFirstFuel);
        TextView textViewForAverageCostSecondFuel = getTextViewForAverageValue(averageFuelCostForSecondFuel);

        linearLayoutForTwoPositionFuelCost.addView(textViewForNameFirstFuelType);
        linearLayoutForTwoPositionFuelCost.addView(textViewForAverageCostFirstFuel);
        linearLayoutForTwoPositionFuelCost.addView(textViewForNameSecondFuelType);
        linearLayoutForTwoPositionFuelCost.addView(textViewForAverageCostSecondFuel);

        averageFuelCostLinearLayout.addView(linearLayoutForTwoPositionFuelCost);
    }

    /**
     * Method creates LinearLayout with parameters for show data about average values
     * Meotda tworzy sparametryzowana LinearLayout dla pokazania wartości średnich
     * @return LinearLayout
     */
    private LinearLayout getLinearLayoutToShowAverageValues() {
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

    private TextView getTextViewForAverageValue(String valueToShow) {
        TextView resultTextView = new TextView(this);
            resultTextView.setGravity(Gravity.CENTER);
            resultTextView.setLayoutParams(getLayoutParamsForTableRow(0,TableRow.LayoutParams.WRAP_CONTENT, 43));
            if(valueToShow.equals(noDataMessage)){
                resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
            else {
                resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            }
        resultTextView.setText(valueToShow);
        return resultTextView;
    }

    private TextView getTextViewForNameFuelType(String fuelType) {
        TextView resultTextView = new TextView(this);
        resultTextView.setTextSize(10);
        resultTextView.setText(fuelType);
        resultTextView.setGravity(Gravity.TOP | Gravity.END);
        resultTextView.setLayoutParams(getLayoutParamsForTableRow(0,TableRow.LayoutParams.MATCH_PARENT, 7));
        return  resultTextView;
    }

    private TableRow.LayoutParams getLayoutParamsForTableRow(int layout_width, int layout_height, int weight) {
        if(weight == 0){
            return new TableRow.LayoutParams(layout_width, layout_height);
        }
        return new TableRow.LayoutParams(layout_width, layout_height, weight);
    }

    private View.OnClickListener clickForChangeFuelConsumptionForTwoPositions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAverageValuesToShowInTwoPosition();
            showAverageConsumptionFuelInTwoPosition();
        }
    };

    private View.OnClickListener clickForChangeFuelConsumptionForOnePositions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAverageFuelConsumptionToShowInOnePosition();
            averageFuelConsumptionLinearLayout.removeAllViews();
            showAverageFuelConsumptionInOnePosition();
        }
    };

    private View.OnClickListener clickForChangeFuelCostDataForTwoPositions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAverageValuesToShowInTwoPosition();
            showAverageCostFuelInTwoPosition();
        }
    };

    private View.OnClickListener clickForChangeFuelCostDataForOnePositions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAverageFuelCostToShowForOnePosition();
            averageFuelCostLinearLayout.removeAllViews();
            showAverageCostFuelInOnePosition();
        }
    };

    private void showAverageFuelConsumptionInOnePosition() {
        averageFuelConsumptionLinearLayout.addView(createTextViewForShowOnePosition(averageFuelConsumptionForFirstFuel));

        if(car.getFuelType().contains(charToCheckTwoTypesFuel)){
            imageButtonChangeShowDataForFuelConsumption.setOnClickListener(clickForChangeFuelConsumptionForTwoPositions);
        }
    }

    private void showAverageCostFuelInOnePosition() {
        averageFuelCostLinearLayout.addView(createTextViewForShowOnePosition(averageFuelCostForFirstFuel));

        if(car.getFuelType().contains(charToCheckTwoTypesFuel)){
            imageButtonChangeShowDataForFuelCost.setOnClickListener(clickForChangeFuelCostDataForTwoPositions);
        }
    }

    private TextView createTextViewForShowOnePosition(String valueToShow) {
        TextView result = new TextView(this);
        result.setGravity(Gravity.CENTER);
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels3 = (int) (50 * scale + 0.5f);
        result.setWidth(pixels3);
        if(valueToShow.equals(noDataMessage)){
            result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        else {
            result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        }
        result.setText(valueToShow);
        result.setLayoutParams(getLayoutParamsForTableRow(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0));
        return result;
    }

    private void getAverageFuelConsumptionToShowInOnePosition(){

        if(car.getFuelType().contains(charToCheckTwoTypesFuel)){
            if(car.getAverageConsumptionFirstFuel() > 0 && car.getAverageConsumptionSecondFuel() > 0){
                averageFuelConsumptionForFirstFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel() + car.getAverageConsumptionSecondFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() > 0 && car.getAverageConsumptionSecondFuel() <= 0){
                averageFuelConsumptionForFirstFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() <= 0 && car.getAverageConsumptionSecondFuel() > 0){
                averageFuelConsumptionForFirstFuel = decimalFormat.format(car.getAverageConsumptionSecondFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() == 0 && car.getAverageConsumptionSecondFuel() == 0){
                averageFuelConsumptionForFirstFuel = noDataMessage;
            }
            else {
                averageFuelConsumptionForFirstFuel = noDataMessage;
            }
        }
        else{
            if(car.getAverageConsumptionFirstFuel() > 0){
                averageFuelConsumptionForFirstFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel());
            }
            else if(car.getAverageConsumptionFirstFuel() == 0){
                averageFuelConsumptionForFirstFuel = noDataMessage;
            }
            else{
                averageFuelCostForFirstFuel = noDataMessage;
            }
        }
    }

    private void getAverageFuelCostToShowForOnePosition(){

        if(car.getFuelType().contains(charToCheckTwoTypesFuel)){
            if(car.getAverageCostFirstFuel() > 0 && car.getAverageCostSecondFuel() > 0){
                averageFuelCostForFirstFuel = decimalFormat.format(car.getAverageCostFirstFuel() + car.getAverageCostSecondFuel());
            }
            else if(car.getAverageCostFirstFuel() > 0 && car.getAverageCostSecondFuel() <= 0){
                averageFuelCostForFirstFuel = decimalFormat.format(car.getAverageCostFirstFuel());
            }
            else if(car.getAverageCostFirstFuel() <= 0 && car.getAverageCostSecondFuel() > 0){
                averageFuelCostForFirstFuel = decimalFormat.format(car.getAverageCostSecondFuel());
            }
            else if(car.getAverageCostFirstFuel() == 0 && car.getAverageCostSecondFuel() == 0){
                averageFuelCostForFirstFuel = noDataMessage;
            }
            else {
                averageFuelCostForFirstFuel = errorDataMessage;
            }
        }
        else{
            if(car.getAverageCostFirstFuel() > 0){
                averageFuelCostForFirstFuel = decimalFormat.format(car.getAverageCostFirstFuel());
            }
            else if(car.getAverageCostFirstFuel() == 0){
                averageFuelCostForFirstFuel = noDataMessage;
            }
            else{
                averageFuelCostForFirstFuel = errorDataMessage;
            }
        }
    }

    private void getAverageValuesToShowInTwoPosition(){
        if(car.getAverageConsumptionFirstFuel() > 0){
            averageFuelConsumptionForFirstFuel = decimalFormat.format(car.getAverageConsumptionFirstFuel());
        }
        else{
            averageFuelConsumptionForFirstFuel = noDataMessage;
        }

        if(car.getAverageConsumptionSecondFuel() > 0){
            averageFuelConsumptionForSecondFuel = decimalFormat.format(car.getAverageConsumptionSecondFuel());
        }
        else{
            averageFuelConsumptionForSecondFuel = noDataMessage;
        }

        if(car.getAverageCostFirstFuel() > 0){
            averageFuelCostForFirstFuel = decimalFormat.format(car.getAverageCostFirstFuel());
        }
        else{
            averageFuelCostForFirstFuel = noDataMessage;
        }

        if(car.getAverageCostSecondFuel() > 0){
            averageFuelCostForSecondFuel = decimalFormat.format(car.getAverageCostSecondFuel());
        }
        else{
            averageFuelCostForSecondFuel = noDataMessage;
        }
    }
}
