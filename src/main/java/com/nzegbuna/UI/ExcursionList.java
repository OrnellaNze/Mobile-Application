package com.nzegbuna.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nzegbuna.Database.Repository;
import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;
import com.nzegbuna.R;

import java.util.List;


public class ExcursionList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExcursionAdapter adapter;
    private int vacID;  //
    private int excursionID;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recyclerViewExcursions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());

        vacID = getIntent().getIntExtra("vacationId", -1);
        if (vacID == -1) {
            loadAllExcursions();
        } else {
            loadExcursions();
        }


        Button goToVacations = findViewById(R.id.buttonGoToExcursions);
        Button backFromExcursions = findViewById(R.id.buttonBackFromVacations);

        goToVacations.setOnClickListener(v -> {
            // Navigate back to VacationList
            Intent intent = new Intent(this, VacationList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        backFromExcursions.setOnClickListener(v -> {
            // Go back to previous activity or close current activity
            finish();
        });

        loadAllExcursions();
        loadExcursions();
    }

    private void editExcursion(Excursion excursion) {
        Intent intent = new Intent(this, ExcursionDetails.class);
        intent.putExtra("EXCURSION_ID", excursion.getExcursionId());
        intent.putExtra("vacationId", excursion.getVacationId());
        startActivity(intent);
    }

    private void deleteExcursion(Excursion excursion) {
        // Show confirmation dialog before deletion
        new AlertDialog.Builder(this)
                .setTitle("Delete Excursion")
                .setMessage("Are you sure you want to delete this excursion?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteExcursion(excursion);
                    refreshExcursions();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshExcursions() {
        List<Excursion> updatedList = repository.getAssociatedExcursions(vacID); // Make sure you use the correct ID
        adapter.setExcursions(updatedList);
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.excursion_list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_vacation_button:
                addVacations();
                return true;
            case R.id.view_all_excursions:
                loadAllExcursions();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadAllExcursions() {
        List<Excursion> excursions = repository.getAllExcursions();
        adapter = new ExcursionAdapter(this, excursions, repository, this::editExcursion, this::deleteExcursion);
        recyclerView.setAdapter(adapter);
    }

    private void loadExcursions() {
        vacID = getIntent().getIntExtra("vacationId", -1);
        if (vacID != -1) {
            List<Excursion> excursions = repository.getAssociatedExcursions(vacID);
            adapter = new ExcursionAdapter(this, excursions, repository, this::editExcursion, this::deleteExcursion);
            recyclerView.setAdapter(adapter);
        } else {
            loadAllExcursions();
        }
    }



    public void addVacations() {
        Intent intent = new Intent(ExcursionList.this, VacationDetails.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        loadExcursions();
        loadAllExcursions();
    }
}
