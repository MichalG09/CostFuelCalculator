package pl.mgrzech.costfuel.presenters;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public interface IAddCarPresenter {

    void createSpinnerBranchCar();

    void createSpinnerModelCar();

    void createSpinnerTypeFuel();

    void createSpinnerPeriodTime();

    void getBrandAddingCar(AdapterView<?> parent, int position);

    void getModelAddingCar(AdapterView<?> parent, int position);

    void getTypeFuelAddingCar(AdapterView<?> parent, int position);

    void getPeriodTimeAddingCarr(AdapterView<?> parent, int position);

    boolean saveNewCar();

    void onItemSelected(AdapterView<?> parent, int position);
}
