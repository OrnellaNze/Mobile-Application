package com.nzegbuna.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.nzegbuna.Database.Repository;
import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;
import com.nzegbuna.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private final Context context;
    private List<Vacation> vacations;
    private final LayoutInflater inflater;
    private final VacationInteractionListener listener;
    private Repository repository;

    public interface VacationInteractionListener {
        void onEditVacation(Vacation vacation);
        void onDeleteVacation(Vacation vacation);
        void onVacationSelected(Vacation vacation);
    }

    public VacationAdapter(Context context, List<Vacation> vacations, Repository repository, VacationInteractionListener listener) {
        this.context = context;
        this.vacations = vacations;
        this.repository = repository;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation vacation = vacations.get(position);

        holder.bind(vacation);
        holder.itemView.setOnClickListener(v -> {
            listener.onVacationSelected(vacation);
            Intent intent = new Intent(context, VacationDetails.class);
            intent.putExtra("id", vacation.getVacationId());
            intent.putExtra("title", vacation.getTitle());
            intent.putExtra("hotel", vacation.getHotel());
            intent.putExtra("startDate", vacation.getStartDate());
            intent.putExtra("endDate", vacation.getEndDate());
            context.startActivity(intent);
        });
        holder.itemView.setOnClickListener(v -> listener.onVacationSelected(vacation));
        holder.buttonEdit.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, VacationDetails.class);
                intent.putExtra("vacationId", vacations.get(position).getVacationId());
                context.startActivity(intent);
            }
        });
        holder.buttonDelete.setOnClickListener(v -> {
            // Show a confirmation dialog before deletion
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this vacation?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Call the listener method to delete the vacation if user confirms
                        listener.onDeleteVacation(vacation);

                    })
                    .setNegativeButton("Cancel", null) // Do nothing on cancellation
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return vacations != null ? vacations.size() : 0;
    }

    class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewHotel, textViewStartDate, textViewEndDate;
        RecyclerView excursionsList;
        Button buttonEdit, buttonDelete, toggle;

        VacationViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewVacationTitle);
            textViewHotel = itemView.findViewById(R.id.textViewVacationHotel);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDates);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDates);
            buttonEdit = itemView.findViewById(R.id.buttonEditVacation);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteVacation);
            toggle = itemView.findViewById(R.id.expandButton);
            excursionsList = itemView.findViewById(R.id.excursionsRecyclerView);
            excursionsList.setLayoutManager(new LinearLayoutManager(context));

            toggle.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Vacation vacation = vacations.get(position);
                    showExcursions(vacation.getVacationId());
                }
            });

        }

        private void showExcursions(int vacationId) {
            Intent intent = new Intent(context, ExcursionList.class);
            intent.putExtra("vacationId", vacationId);
            context.startActivity(intent);
        }

        void bind(Vacation vacation) {
            textViewTitle.setText(vacation.getTitle());
            textViewHotel.setText(vacation.getHotel());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            textViewStartDate.setText(sdf.format(new Date(vacation.getStartDate())));
            textViewEndDate.setText(sdf.format(new Date(vacation.getEndDate())));


        }


    }
}

