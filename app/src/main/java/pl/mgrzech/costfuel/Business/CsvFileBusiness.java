package pl.mgrzech.costfuel.Business;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.activities.AddFuelActivity;
import pl.mgrzech.costfuel.activities.AllCarsActivity;
import pl.mgrzech.costfuel.activities.CarActivity;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.database.DatabaseSingleton;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class CsvFileBusiness {

    private Database database;
    private File file = null;

    public void saveDataToCsvFile(Context context) {
        database = DatabaseSingleton.getInstance(context);
        List<Car> allCarsList = database.getAllCars();
        List<Fuel> allFuelsList = database.getAllFuels();

        try {
            file = new File(context.getExternalFilesDir(null), "DatabaseBackup.csv");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(file);

            if(allCarsList != null && !allCarsList.isEmpty()){
                for(Car car : allCarsList){
                    writer.append(car.toStringForCsvFile());
                }
            }

            if(allFuelsList != null && !allFuelsList.isEmpty()){
                for(Fuel fuel : allFuelsList){
                    writer.append(fuel.toStringForCsvFile());
                }
            }

            writer.flush();
            writer.close();
            Toast.makeText(context, "eksport danych udany", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readDataFromCsvFile(final Context context) {

        database = DatabaseSingleton.getInstance(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Import nowej bazy danych spowoduje usunięcie wszystkich obecnych danych. Czy na pewno chcesz zaimportować dane?");

        builder.setPositiveButton((R.string.Text_Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                database.clearDatabase();
                readCsvFileAndSaveToDatabase(context, database);


                Intent intent = new Intent(context, AllCarsActivity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton((R.string.Text_No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void readCsvFileAndSaveToDatabase(Context context, Database database) {
        String readerLine = "";
        String cvsSplitBy = ",";

        try {
            file = new File(context.getExternalFilesDir(null), "DatabaseBack.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            while ((readerLine = bufferedReader.readLine()) != null) {

                String[] readerLineSplited = readerLine.split(cvsSplitBy);
                if(readerLineSplited[0].equals("car")){
                    addReadCarToDatabase(readerLineSplited, database);
                }
                else if(readerLineSplited[0].equals("fuel")){
                    addReadFuelToDatabase(readerLineSplited, database);
                }
                else{
                    Toast.makeText(context, "Błędne dane w pliku", Toast.LENGTH_LONG).show();
                }
            }
            Toast.makeText(context, "import danych udany", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            ((Activity) context).startActivityForResult(intent, 10);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addReadFuelToDatabase(String[] lineSplited, Database database) {
        Fuel newFuelFromReadFile = new Fuel(Integer.parseInt(lineSplited[1]), lineSplited[2], lineSplited[3],
                Double.parseDouble(lineSplited[4]), Double.parseDouble(lineSplited[6]),
                Integer.parseInt(lineSplited[5]), Integer.parseInt(lineSplited[7]));
        database.addFuel(newFuelFromReadFile);
    }

    private void addReadCarToDatabase(String[] lineSplited, Database database) {
        Car newCarFromReadFile = new Car(Integer.parseInt(lineSplited[1]), lineSplited[2],
                lineSplited[3], Double.parseDouble(lineSplited[4]),
                Double.parseDouble(lineSplited[5]), Double.parseDouble(lineSplited[6]),
                Double.parseDouble(lineSplited[7]), lineSplited[8], lineSplited[9]);
        database.addCar(newCarFromReadFile);
    }
}
