package pl.mgrzech.costfuel.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.adapters.ListAllFuelsAdapter;
import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Fuel;

public class AllFuelsActivity extends AppCompatActivity{

    private int carId;
    private FuelDatabase fuelDatabase = new FuelDatabase(this);
    private List<Fuel> listFuels;
    private RecyclerView recyclerView;

    /**
     * Methods is called on create this activity. It created recyclerView with all saved fuel for car.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fuels);
        getIncomingIntent();

        recyclerView = (RecyclerView) findViewById(R.id.fragmentListAllFuels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        listFuels = fuelDatabase.getAllFuelsForCarId(String.valueOf(carId));
        Collections.sort(listFuels, Fuel.Comparators.SORT_BY_DATE);
        recyclerView.setAdapter(new ListAllFuelsAdapter(listFuels, this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * The method got the Id of the car for all fuels was showed
     * Meotda pobiera id samochodu, dla którego wyświetla wszystkie tankowania.
     */
    private void getIncomingIntent(){
        if(getIntent().hasExtra("carId")){
            carId = getIntent().getIntExtra("carId",0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
