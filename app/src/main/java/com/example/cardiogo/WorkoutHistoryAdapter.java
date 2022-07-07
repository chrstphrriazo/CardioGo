package com.example.cardiogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutHistoryAdapter extends RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder> {

    Context context;
    List<WorkoutHistoryModel> workoutHistoryModelList;

    public WorkoutHistoryAdapter(Context context, List<WorkoutHistoryModel> workoutHistoryModelList) {
        this.context = context;
        this.workoutHistoryModelList = workoutHistoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(workoutHistoryModelList != null && workoutHistoryModelList.size() > 0){
            WorkoutHistoryModel paymentModel = workoutHistoryModelList.get(position);
            holder.idTextview.setText(paymentModel.getAveHeartRate());
            holder.nameTextview.setText(paymentModel.getAveSPo2());
            holder.paymentTextview.setText(paymentModel.getDateToday());
            holder.statusTextview.setText(paymentModel.getWorkoutStatus());
            if (paymentModel.getWorkoutStatus().toUpperCase().equals("SUCCESS")){
               holder.statusTextview.setTextColor(context.getResources().getColor(R.color.gStart));
            }else if(paymentModel.getWorkoutStatus().toUpperCase().equals("STOPPED")){
                holder.idTextview.setText("N/A");
                holder.nameTextview.setText("N/A");
                holder.statusTextview.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                holder.statusTextview.setTextColor(context.getResources().getColor(R.color.red));
            }


        }else{
            return;
        }

    }

    @Override
    public int getItemCount() {
        return workoutHistoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView idTextview,nameTextview,paymentTextview, statusTextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            idTextview = itemView.findViewById(R.id.idTextview);
            nameTextview = itemView.findViewById(R.id.nameTextview);
            paymentTextview = itemView.findViewById(R.id.paymentTextview);
            statusTextview = itemView.findViewById(R.id.statusTextview);


        }
    }
}