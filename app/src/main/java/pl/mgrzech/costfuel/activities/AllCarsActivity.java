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

import java.util.List;

import pl.mgrzech.costfuel.Facades.CsvFileFacade;
import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.adapters.ListAllCarAdapter;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.database.DatabaseSingleton;
import pl.mgrzech.costfuel.models.Car;

/**
 * Start Activity
 */
public class AllCarsActivity extends AppCompatActivity {

    private CsvFileFacade csvFileFacade = new CsvFileFacade();
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

        Database database = DatabaseSingleton.getInstance(this);
        List<Car> listCar = database.getAllCars();

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
        else if (id == R.id.menu_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.menu_export) {
            csvFileFacade.saveDataToCsvFile(this);
            return true;
        }
        else if (id == R.id.menu_import) {
            csvFileFacade.readDataFromCsvFile(this);

//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("*/*");
//            startActivityForResult(intent, 10);
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
        Intent intent = new Intent(this, AddCarActivityView.class);
        startActivity(intent);
    }

}
