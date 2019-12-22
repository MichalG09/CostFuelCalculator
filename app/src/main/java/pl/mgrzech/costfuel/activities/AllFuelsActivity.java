package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_fuels);
        getIncomingIntent();

        RecyclerView recyclerView = findViewById(R.id.fragmentListAllFuels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FuelDatabase fuelDatabase = new FuelDatabase(this);
        List<Fuel> listFuels = fuelDatabase.getAllFuelsForCarId(String.valueOf(carId));

        if(!listFuels.isEmpty()){
            Collections.sort(listFuels, Fuel.Comparators.SORT_BY_DATE);
            recyclerView.setAdapter(new ListAllFuelsAdapter(listFuels, this));
        }
        else{
            LinearLayout linearLayout = findViewById(R.id.allFuelsLinearLayout);
            TextView textView = new TextView(this);
            textView.setText(getResources().getString(R.string.activity_all_fuels_no_fuels_to_show));
            linearLayout.addView(textView);
            textView.setGravity(Gravity.CENTER | Gravity.TOP);
            textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * The method got the Id of the car for all fuels will show
     * Meotda pobiera id samochodu, dla którego wyświetla wszystkie tankowania.
     */
    private void getIncomingIntent(){
        if(getIntent().hasExtra(getString(R.string.carIdIncommingIntent))){
            carId = getIntent().getIntExtra(getString(R.string.carIdIncommingIntent),0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, CarActivity.class);
        intent.putExtra(getString(R.string.carIdIncommingIntent), carId);
        startActivity(intent);
        return true;
    }
}
