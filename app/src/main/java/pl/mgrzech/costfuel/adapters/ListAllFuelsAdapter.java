package pl.mgrzech.costfuel.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.CarDatabase;
import pl.mgrzech.costfuel.database.FuelDatabase;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class ListAllFuelsAdapter extends RecyclerView.Adapter<ListAllFuelsAdapter.ListAllFuelsViewHolder> {

    private List<Fuel> mListFuels;
    private Context mContext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Fuel fuel;
    private View mview;

    public ListAllFuelsAdapter(List<Fuel> listFuels, Context context) {
        mListFuels = listFuels;
        mContext = context;
    }

    @NonNull
    @Override
    public ListAllFuelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_row, parent, false);

        return new ListAllFuelsViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAllFuelsViewHolder holder, final int position) {

        fuel = mListFuels.get(position);

        holder.dataFuel.setText(fuel.getDate() + " " + fuel.getFuelType() + "\n" + fuel.getCost() + "zł " + fuel.getQuantity() + "l " + fuel.getMileage());

        holder.fuelRowEditFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(mContext, CarActivity.class);
//                intent.putExtra("fuelId", mListFuels.get(position).getId());
//                mContext.startActivity(intent);
            }
        });

        holder.fuelRowDeleteFuel.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                fuel = mListFuels.get(position);
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Na pewno chcesz usunąć tankowanie ?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FuelDatabase fuelDatabase = new FuelDatabase(mContext);
                        String carId = fuelDatabase.getCarIdForFuels(String.valueOf(fuel.getId()));
                        int result = fuelDatabase.deleteFuel(fuel);
                        if(result > 0){
                            CarDatabase carDatabase = new CarDatabase(mContext);
                            Car car = carDatabase.getCarById(Integer.valueOf(carId));
                            mListFuels.remove(fuel);
                            notifyDataSetChanged();
                            car = CalculateAvarageFuelAndCost.recarkulate(fuelDatabase, car);
                            carDatabase.updateCar(car);
                            Toast.makeText(mContext, "Poprawnie usunięto tankowanie", Toast.LENGTH_LONG ).show();
                        }
                        else {
                            Toast.makeText(mContext, "Błąd przy usuwaniu !", Toast.LENGTH_LONG ).show();
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
        });
    }

    @Override
    public int getItemCount() {
        return mListFuels.size();
    }

    public class ListAllFuelsViewHolder extends RecyclerView.ViewHolder{

        public TextView dataFuel;
        public LinearLayout fuelRowEditFuel;
        public LinearLayout fuelRowDeleteFuel;

        public ListAllFuelsViewHolder(@NonNull View itemView) {
            super(itemView);
            dataFuel = itemView.findViewById(R.id.fuelDataRow);
            fuelRowEditFuel = itemView.findViewById(R.id.fuelRowEditFuel);
            fuelRowDeleteFuel = itemView.findViewById(R.id.fuelRowDeleteFuel);
        }

    }

}
