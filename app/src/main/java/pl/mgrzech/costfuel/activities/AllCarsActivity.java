package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.adapters.ListAllCarAdapter;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class AllCarsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cars);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.fragmentListAllCars);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CarDatabase carDatabase = new CarDatabase(this);
        List<Car> listCar = carDatabase.getAllCars();
        recyclerView.setAdapter(new ListAllCarAdapter(listCar, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_instruction) {
            Intent intent = new Intent(this, InstructionActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method for button "add new car" from main layout with all saved car.
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
