package pl.mgrzech.costfuel.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.View;
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
    private TextView carAverageFuelInfo;
    private TextView carAverageFuelCostInfo;
    private CarDatabase carDatabase;
    private FuelDatabase fuelDatabase;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Car car = null;
    private Context mContext;
    private DecimalFormat decimalFormat = new DecimalFormat("##0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        getIncomingIntent();
        mContext = this;

        carDatabase = new CarDatabase(this);
        car = carDatabase.getCarById(carIdForShow);

        carMarkInfo = findViewById(R.id.carActivityCarMark);
        carMarkInfo.setText(car.getMark());

        carModelInfo = findViewById(R.id.carActivityCarModel);
        carModelInfo.setText(car.getModel());

        carTypeFuelInfo = findViewById(R.id.carActivityCarFuelType);
        carTypeFuelInfo.setText(car.getFuelType());

        carAverageFuelInfo = findViewById(R.id.carActivityCarAverageFuel);
        if(car.getAvarageFuelConsumption() == 0){
            carAverageFuelInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            carAverageFuelInfo.setText("Brak Danych");
        }
        else{
            carAverageFuelInfo.setText(decimalFormat.format(car.getAvarageFuelConsumption()));
        }

        carAverageFuelCostInfo = findViewById(R.id.carActivityCarAverageCostFuel);
        if(car.getAvarageCost() == 0){
            carAverageFuelCostInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            carAverageFuelCostInfo.setText("Brak Danych");
        }
        else if(car.getAvarageCost() < 0){
            carAverageFuelCostInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            carAverageFuelCostInfo.setText("Błędne Dane");
        }
        else{
            carAverageFuelCostInfo.setText(decimalFormat.format(car.getAvarageCost()));
        }
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
}
