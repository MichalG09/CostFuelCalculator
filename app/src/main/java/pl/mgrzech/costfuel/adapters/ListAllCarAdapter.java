package pl.mgrzech.costfuel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.mgrzech.costfuel.R;
import pl.mgrzech.costfuel.activities.AddFuelActivity;
import pl.mgrzech.costfuel.activities.CarActivity;
import pl.mgrzech.costfuel.models.Car;

public class ListAllCarAdapter extends RecyclerView.Adapter<ListAllCarAdapter.ListAllCarViewHolder> {

    private List<Car> mListCar;
    private Context mContext;

    public ListAllCarAdapter(List<Car> listCar, Context context) {
        mListCar = listCar;
        mContext = context;
    }

    @NonNull
    @Override
    public ListAllCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_row, parent, false);

        return new ListAllCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAllCarViewHolder holder, final int position) {

        Car car = mListCar.get(position);

        holder.carName.setText(car.getMark() + " " + car.getModel());

        holder.carRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, CarActivity.class);
                intent.putExtra("carId", mListCar.get(position).getId());
                mContext.startActivity(intent);
            }
        });

        holder.carRowAddFuel.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, AddFuelActivity.class);
                intent.putExtra("carId", mListCar.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListCar != null ? mListCar.size() : 0;
    }

    public class ListAllCarViewHolder extends RecyclerView.ViewHolder{

        public TextView carName;
        public LinearLayout carRow;
        public LinearLayout carRowAddFuel;

        public ListAllCarViewHolder(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.carName);
            carRow = itemView.findViewById(R.id.carRow);
            carRowAddFuel = itemView.findViewById(R.id.carRowAddFuel);
        }
    }
}
