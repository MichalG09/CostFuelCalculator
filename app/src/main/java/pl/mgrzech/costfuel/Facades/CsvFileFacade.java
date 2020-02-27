package pl.mgrzech.costfuel.Facades;

import android.content.Context;

import pl.mgrzech.costfuel.Business.CsvFileBusiness;
import pl.mgrzech.costfuel.activities.AllCarsActivity;

public class CsvFileFacade {

    CsvFileBusiness csvFileBusiness = new CsvFileBusiness();

    public void saveDataToCsvFile(Context context){
        csvFileBusiness.saveDataToCsvFile(context);
    }

    public void readDataFromCsvFile(Context context){
        csvFileBusiness.readDataFromCsvFile(context);
    }
}
