package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.adapters.ListAllCarAdapter;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class AllCarsActivity extends AppCompatActivity {

    /**
     * List all car from datebase.
     */
    private List<Car> listCar;

    /**
     * Methods is called on create this activity. It created recyclerView with all seaved car (got from datebase).
     * Metoda jest wywoływana podczas tworzeni tej aktywności. Tworzony jest recyclerView ze wszystkimi zapisanymi samochodami (z bazy).
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cars);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fragmentListAllCars);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CarDatabase carDatabase = new CarDatabase(this);
        FuelDatabase fuelDatabase = new FuelDatabase(this);

//        fuelDatabase.calcFuelsDatabase();
        listCar = carDatabase.getAllCars();

        recyclerView.setAdapter(new ListAllCarAdapter(listCar, this));
    }

    /**
     * Method is called after use button "add new car" from main layout with all saved car.
     * Metoda dla przycisku "dodaj nowy samochód" dla głownego widoku ze wszystkimi zapisnymi samochodami.
     * @param view
     */
    public void onAddNewCar(View view) {
        Intent intent = new Intent(this, AddCarActivity.class);
        startActivity(intent);
    }

    public void clearDatabase(View view) {
        CarDatabase carDatabase = new CarDatabase(this);
        carDatabase.clearDatabase();
        FuelDatabase fuelDatabase = new FuelDatabase(this);
        fuelDatabase.clearDatabase();
        Intent intent = new Intent(this, AllCarsActivity.class);
        startActivity(intent);
    }
}
