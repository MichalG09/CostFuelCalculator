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

import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.calculate.CalculateAvarageFuelAndCost;
import pl.mgrzech.costfuel.database.Database;
import pl.mgrzech.costfuel.models.Car;
import pl.mgrzech.costfuel.models.Fuel;

public class ListAllFuelsAdapter extends RecyclerView.Adapter<ListAllFuelsAdapter.ListAllFuelsViewHolder> {

    private List<Fuel> mListFuels;
    private Context mContext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Fuel fuel;

    public ListAllFuelsAdapter(List<Fuel> listFuels, Context context) {
        mListFuels = listFuels;
        mContext = context;
    }

    @NonNull
    @Override
    public ListAllFuelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_row, parent, false);

        return new ListAllFuelsViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAllFuelsViewHolder holder, final int position) {

        fuel = mListFuels.get(position);

        String textForFuelForListAllFuels = fuel.getDate() + " " + fuel.getFuelType() + "\n" + fuel.getCost() + "zł " + fuel.getQuantity() + "l " + fuel.getMileage() + "km";
        holder.dataFuel.setText(textForFuelForListAllFuels);

        holder.fuelRowDeleteFuel.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                fuel = mListFuels.get(position);
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(mContext.getString(R.string.all_fuels_adapter_text_confirm_delete_fuel));

                builder.setPositiveButton(mContext.getString(R.string.all_fuels_adapter_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Database database = new Database(mContext);
                        String carId = database.getCarIdForFuels(String.valueOf(fuel.getId()));
                        int result = database.deleteFuel(fuel);
                        if(result > 0){
                            Car car = database.getCarById(Integer.valueOf(carId));
                            mListFuels.remove(fuel);
                            notifyDataSetChanged();
                            car = CalculateAvarageFuelAndCost.recarkulate(database, car);
                            database.updateCar(car);
                            Toast.makeText(mContext, mContext.getString(R.string.all_fuels_adapter_correct_delete), Toast.LENGTH_LONG ).show();
                        }
                        else {
                            Toast.makeText(mContext, mContext.getString(R.string.all_fuels_adapter_error_during_delete), Toast.LENGTH_LONG ).show();
                        }
                    }
                });

                builder.setNegativeButton(mContext.getString(R.string.all_fuels_adapter_cancel), new DialogInterface.OnClickListener() {
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

        TextView dataFuel;
        LinearLayout fuelRowDeleteFuel;

        ListAllFuelsViewHolder(@NonNull View itemView) {
            super(itemView);
            dataFuel = itemView.findViewById(R.id.fuelDataRow);
            fuelRowDeleteFuel = itemView.findViewById(R.id.fuelRowDeleteFuel);
        }

    }

}
