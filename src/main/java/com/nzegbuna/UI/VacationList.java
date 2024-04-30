package com.nzegbuna.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nzegbuna.Database.Repository;
import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;
import com.nzegbuna.R;


import java.util.List;


public class VacationList extends AppCompatActivity {
    private RecyclerView vacationRecyclerView;
    private VacationAdapter vacationAdapter;
    private List<Vacation> vacations;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fabAddVacation = findViewById(R.id.addVacationFAB);
        fabAddVacation.setOnClickListener(v -> navigateToAddVacationActivity());


        repository = new Repository(getApplication());
        vacationRecyclerView = findViewById(R.id.vacationListRecyclerView);
        vacationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadVacations();
    }

    private void loadVacations() {
        new Thread(() -> {
            List<Vacation> loadedVacations = repository.getAllVacations();
            runOnUiThread(() -> {
                vacationAdapter = new VacationAdapter(this, loadedVacations, repository, new VacationAdapter.VacationInteractionListener() {
                    @Override
                    public void onEditVacation(Vacation vacation) {

                        editVacation(vacation);
                    }

                    @Override
                    public void onDeleteVacation(Vacation vacation) {
                        deleteVacation(vacation);
                    }

                    @Override
                    public void onVacationSelected(Vacation vacation) {
                        // Example action when a vacation is selected
                        onVacationClicked(vacation);
                    }
                });
                vacationRecyclerView.setAdapter(vacationAdapter);
            });
        }).start();
    }


    private void editVacation(Vacation vacation) {
        Intent intent = new Intent(this, VacationDetails.class);
        intent.putExtra("vacationId", vacation.getVacationId());
        startActivity(intent);
    }

    private void deleteVacation(Vacation vacation) {
            List<Excursion> associatedExcursions = repository.getAssociatedExcursions(vacation.getVacationId());

            if (associatedExcursions.isEmpty()) {
                new Thread(() -> {
                    repository.deleteVacation(vacation);
                    runOnUiThread(() -> {
                        Toast.makeText(this, vacation.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                        loadVacations();  // Refresh the list after deletion
                    });
                }).start();
            } else {
                // Prevent deletion due to existing associated excursions
                runOnUiThread(() -> Toast.makeText(this, "Cannot delete " + vacation.getTitle() + " because it has associated excursions.", Toast.LENGTH_LONG).show());
            }
        }
    private void navigateToAddVacationActivity() {
        Intent intent = new Intent(this, VacationDetails.class);
        startActivity(intent);
    }

    private void navigateToSeeExcursion() {
        Intent intent = new Intent(this, ExcursionDetails.class);
        startActivity(intent);
    }

    private void onVacationClicked(Vacation vacation) {
        // Additional method to handle navigation or actions when a vacation item is selected
        Intent intent = new Intent(this, VacationDetails.class);
        intent.putExtra("vacationId", vacation.getVacationId());
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_vacation_button:
                navigateToAddVacationActivity();
                return true;

            case R.id.see_excursions:
                navigateToSeeExcursion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadVacations();
    }
}

