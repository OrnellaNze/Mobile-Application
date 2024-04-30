package com.nzegbuna.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.nzegbuna.Database.Repository;
import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;
import com.nzegbuna.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;


public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> excursions;
    private LayoutInflater inflater;
    private Consumer<Excursion> onEdit;
    private Consumer<Excursion> onDelete;
    private Context context;
    private Repository repository;

    public ExcursionAdapter(Context context, List<Excursion> excursions, Repository repository, Consumer<Excursion> onEdit, Consumer<Excursion> onDelete) {
        this.context = context;
        this.excursions = excursions;
        this.repository = repository;  // Repository is correctly initialized here
        this.inflater = LayoutInflater.from(context);
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }


    @Override
    public ExcursionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExcursionViewHolder holder, int position) {
        Excursion excursion = excursions.get(position);
        holder.titleTextView.setText(excursion.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        holder.dateTextView.setText(sdf.format(new Date(excursion.getDate())));

        holder.editButton.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("EXCURSION_ID", excursion.getExcursionId());
                intent.putExtra("vacationId", excursion.getVacationId());
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Invalid position", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this excursion?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (position != RecyclerView.NO_POSITION) {
                            Excursion toDelete = excursions.get(position);
                            repository.deleteExcursion(toDelete);  // Delete from database
                            excursions.remove(position);  // Remove from the list
                            notifyItemRemoved(position);  // Notify the adapter
                            notifyItemRangeChanged(position, excursions.size());  // Adjust any affected item positions
                            Toast.makeText(context, "Excursion deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        Button editButton;
        Button deleteButton;

        ExcursionViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.excursionTitleTextView);
            dateTextView = itemView.findViewById(R.id.excursionDateTextView);
            editButton = itemView.findViewById(R.id.buttonEditExcursion);
            deleteButton = itemView.findViewById(R.id.buttonDeleteExcursion);
        }

        void bind(Excursion excursion) {
            titleTextView.setText(excursion.getTitle());
            dateTextView.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date(excursion.getDate())));
        }




    }
    public void setExcursions (List < Excursion > excursions) {
        excursions = excursions;
        notifyDataSetChanged();
    }
}
