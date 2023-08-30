package com.example.acceuil_recp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;




public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private ArrayList<ScheduleItem> scheduleItems;

    public ScheduleAdapter() {
        scheduleItems = new ArrayList<>();
    }

    public void setScheduleItems(ArrayList<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleItem scheduleItem = scheduleItems.get(position);
        holder.timeTextView.setText(scheduleItem.getTime());
        holder.eventTextView.setText(scheduleItem.getEvent());

        // Vous pouvez ajouter ici la logique pour gérer les clics sur les champs du tableau
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView eventTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            eventTextView = itemView.findViewById(R.id.eventTextView);
        }
    }
}
