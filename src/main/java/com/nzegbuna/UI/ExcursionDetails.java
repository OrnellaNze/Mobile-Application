package com.nzegbuna.UI;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.nzegbuna.Database.Repository;

import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;
import com.nzegbuna.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {


    EditText editTitle, editDate;
    int excursionID, vacID;
    private Repository repository;
    private Excursion currentExcursion;
    private Vacation currentVacation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repository = new Repository(getApplication());
        editTitle = findViewById(R.id.editTextExcursionTitle);
        editDate = findViewById(R.id.editTextExcursionDate);

        excursionID = getIntent().getIntExtra("id", -1);
        vacID = getIntent().getIntExtra("vacationId", -1);

        if (vacID == -1) {
            Toast.makeText(this, "Invalid Vacation ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load the currentVacation based on vacID
        currentVacation = repository.getVacationById(vacID);
        if (currentVacation == null) {
            Toast.makeText(this, "Vacation details not available.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set date if passed through the intent
        long dateInMillis = getIntent().getLongExtra("Date", 0);
        if (dateInMillis != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            editDate.setText(sdf.format(new Date(dateInMillis)));
        }


        editDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    cal.set(year, month, dayOfMonth);
                    String myFormat = "MM/dd/yyyy"; // Better to use consistent date format
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    editDate.setText(sdf.format(cal.getTime()));
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void navigateToVacationList() {
        Intent intent = new Intent(this, VacationList.class);
        startActivity(intent);
        finish();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.excursion_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_save_excursion:
                saveExcursion();
                return true;
            case R.id.action_see_vacation:
                viewVacations();
                return true;
            case R.id.action_view_all_excursions:
                viewAllExcursions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveExcursion() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date excursionDate = sdf.parse(editDate.getText().toString());
            if (excursionDate == null) {
                Toast.makeText(this, "Invalid date format. Use MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate the excursion date against the current vacation dates
            if (currentVacation != null) {
                Date vacationStartDate = new Date(currentVacation.getStartDate());
                Date vacationEndDate = new Date(currentVacation.getEndDate());

                if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                    Toast.makeText(this, "Excursion date must be within the vacation dates.", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Vacation details not available.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentExcursion == null) { // If it's a new excursion
                currentExcursion = new Excursion();
                currentExcursion.setVacationId(currentVacation.getVacationId());
            }

            // Set or update details
            currentExcursion.setTitle(editTitle.getText().toString());
            currentExcursion.setDate(excursionDate.getTime());

            // Decide to insert or update based on excursionID
            if (excursionID == -1) {
                repository.insertExcursion(currentExcursion);
                Toast.makeText(this, "Excursion added", Toast.LENGTH_SHORT).show();
            } else {
                currentExcursion.getExcursionId();  // Ensure the ID is set before updating
                repository.update(currentExcursion);
                Toast.makeText(this, "Excursion updated", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewVacations() {
        Intent intent = new Intent(ExcursionDetails.this, VacationList.class);
        startActivity(intent);
    }

    private void viewAllExcursions() {
        Intent intent = new Intent(ExcursionDetails.this, ExcursionList.class);
        startActivity(intent);
    }

    }



