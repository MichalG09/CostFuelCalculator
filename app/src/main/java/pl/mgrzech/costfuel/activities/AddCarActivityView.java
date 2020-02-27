package pl.mgrzech.costfuel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.presenters.AddCarPresenter;
import pl.mgrzech.costfuel.presenters.IAddCarPresenter;

public class AddCarActivityView extends AppCompatActivity implements AdapterView.OnItemSelectedListener, IAddCarView {

    private IAddCarPresenter addCarPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        addCarPresenter = new AddCarPresenter(this, this, (Spinner)findViewById(R.id.spinnerBranchCar),
                (Spinner)findViewById(R.id.spinnerModelCar), (Spinner)findViewById(R.id.spinnerTypeFuelCar), (Spinner)findViewById(R.id.spinnerPeriodTime));

        addCarPresenter.createSpinnerBranchCar();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * The method checks which field has been completed and saved the entered value.
     * Metoda sprawdza, które pole zostało uzupełnione i zapisuje wprowadzoną wartość.
     * @param parent parent
     * @param view view
     * @param position position
     * @param id id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        addCarPresenter.onItemSelected(parent, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Method check data for a new car from layout add new car. If data is correct save new car in database or data is
     * incorrect change spinner (witj incorrect data) background color to red.
     * Meotda sprawdza poprawnośc danych z grafiki dodawania nowego samochodu. Jeżeli dane sa poprawne to zapisuje samochód w bazie danych lub jeżeli dane są
     * niepoprawne to zmienia kolor spinnera (z błędnymi danymi) na czerwony
     * @param view view
     */
    public void onSaveNewCar(View view) {

        if(addCarPresenter.saveNewCar()){
            Intent intent = new Intent(this, AllCarsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
