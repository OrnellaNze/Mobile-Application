package com.nzegbuna.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nzegbuna.Database.Repository;

import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;
import com.nzegbuna.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;


public class VacationDetails extends AppCompatActivity {
    EditText editTitle, editHotel;

    TextView startDate, endDate;
    Vacation currentVacation;
    int vacationID;
    private Repository repository;


    private RecyclerView recyclerViewExcursions;
    private ExcursionAdapter excursionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Initialize fields
    initViews();
    // Load data
    loadVacationDetails();
    loadAssociatedExcursions();
}

        private void initViews() {
            startDate = findViewById(R.id.editTextVacationStartDate);
            endDate = findViewById(R.id.editTextVacationEndDate);
            editTitle = findViewById(R.id.editTextVacationTitle);
            editHotel = findViewById(R.id.editTextVacationHotel);
            recyclerViewExcursions = findViewById(R.id.recyclerViewExcursions);

            vacationID = getIntent().getIntExtra("vacationId", -1); // Retrieve vacation ID
            repository = new Repository(getApplication());

            startDate.setOnClickListener(v -> showDatePickerDialog(true));
            endDate.setOnClickListener(v -> showDatePickerDialog(false));


            FloatingActionButton fabAddExcursion = findViewById(R.id.fabAddExcursion);
            fabAddExcursion.setOnClickListener(v -> {
                Intent intent = new Intent(this, ExcursionDetails.class);
                intent.putExtra("vacationId", currentVacation.getVacationId());
                // Do not pass an excursion ID to indicate a new excursion
                startActivity(intent);
            });
    }

    private void loadVacationDetails() {
        currentVacation = repository.getVacationById(vacationID);
        if (currentVacation != null) {
            editTitle.setText(currentVacation.getTitle());
            editHotel.setText(currentVacation.getHotel());
            startDate.setText(formatDate(currentVacation.getStartDate()));
            endDate.setText(formatDate(currentVacation.getEndDate()));
        }
    }

    private void loadAssociatedExcursions() {
        if (vacationID != -1) {
            List<Excursion> excursions = repository.getAssociatedExcursions(vacationID);
            excursionAdapter = new ExcursionAdapter(this, excursions, repository, this::editExcursion, this::deleteExcursion);
            recyclerViewExcursions.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewExcursions.setAdapter(excursionAdapter);
        }
    }

    private void editExcursion(Excursion excursion) {
        Intent intent = new Intent(this, ExcursionDetails.class);
        intent.putExtra("EXCURSION_ID", excursion.getExcursionId());
        intent.putExtra("vacationId", vacationID);
        startActivity(intent);
    }

    private void deleteExcursion(Excursion excursion) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Excursion")
                .setMessage("Are you sure you want to delete this excursion?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteExcursion(excursion);
                    loadAssociatedExcursions(); // Refresh the list after deletion
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    public void viewVacations() {
        Intent intent = new Intent(VacationDetails.this, VacationList.class);
        startActivity(intent);
    }

    public void enterExcursionDetail() {
        Intent intent = new Intent(this, ExcursionDetails.class);
        intent.putExtra("vacationId", vacationID);
        startActivity(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacation_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.saveVacation:
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        Date startParsed = sdf.parse(startDate.getText().toString());
                        Date endParsed = sdf.parse(endDate.getText().toString());

                        // Check if end date is before start date
                        if (endParsed.before(startParsed)) {
                            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
                            return true; // Stop further processing since validation failed
                        }

                        long startMillis = startParsed.getTime();
                        long endMillis = endParsed.getTime();

                        if (vacationID == -1) {
                            if (repository.getAllVacations().size() == 0) vacationID = 1;
                            else
                                vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationId() + 1;
                            currentVacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startMillis, endMillis);
                            repository.insert(currentVacation);
                        } else {
                            currentVacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startMillis, endMillis);
                            repository.update(currentVacation);
                        }
                        Toast.makeText(this, "Vacation saved", Toast.LENGTH_SHORT).show();
                        enterExcursionDetail();

                        return true; // Indicate that the event was handled
                    } catch (ParseException e) {
                        Toast.makeText(this, "Invalid date format. Use MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
                        return true; // Indicate that the event was handled due to an exception
                    }

                case R.id.action_see_vacation:
                    viewVacations();

            case R.id.action_share:
                shareVacationDetails();
                return true;


                case R.id.action_set_alert:
                    setAlertsForVacation();            }
        return super.onOptionsItemSelected(item);
    }
    private void setAlertsForVacation() {
        if (currentVacation != null) {
            setAlert(currentVacation.getStartDate(), "Vacation Start Alert for " + currentVacation.getTitle(), "start", currentVacation.getVacationId());
            setAlert(currentVacation.getEndDate(), "Vacation End Alert for " + currentVacation.getTitle(), "end", currentVacation.getVacationId());
        } else {
            Toast.makeText(this, "Vacation details not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAlert(long timeInMillis, String alertTitle, String type, int vacationId) {
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        alertIntent.putExtra("type", type); // "start" or "end"
        alertIntent.putExtra("title", currentVacation.getTitle());
        alertIntent.putExtra("date", timeInMillis); // Pass the date as well
        alertIntent.putExtra("message", type.equals("start") ? "Your vacation starts today!" : "Your vacation ends today!");

        // Unique request code to differentiate start and end alerts
        int requestCode = (type + vacationId).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, alertIntent,  PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        // Feedback to user
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formattedDate = sdf.format(new Date(timeInMillis));
        Toast.makeText(this, "Alert set for " + alertTitle + " on " + formattedDate, Toast.LENGTH_SHORT).show();
    }



    private void showDatePickerDialog(boolean isStartDate) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(Calendar.YEAR, year);
                    selectedCal.set(Calendar.MONTH, month);
                    selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    String selectedDate = format.format(selectedCal.getTime());
                    if (isStartDate) {
                        startDate.setText(selectedDate);
                    } else {
                        endDate.setText(selectedDate);
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void shareVacationDetails() {
        if (currentVacation == null) {
            Toast.makeText(this, "Vacation details not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build the sharing text for vacation details
        StringBuilder shareTextBuilder = new StringBuilder();
        shareTextBuilder.append("Vacation Details:");
        shareTextBuilder.append("\nTitle: ").append(currentVacation.getTitle());
        shareTextBuilder.append("\nHotel: ").append(currentVacation.getHotel());
        shareTextBuilder.append("\nStart Date: ").append(formatDate(currentVacation.getStartDate()));
        shareTextBuilder.append("\nEnd Date: ").append(formatDate(currentVacation.getEndDate()));

        // Fetch and append excursion details
        List<Excursion> excursions = repository.getAssociatedExcursions(currentVacation.getVacationId());
        if (!excursions.isEmpty()) {
            shareTextBuilder.append("\n\nExcursions:");
            for (Excursion excursion : excursions) {
                shareTextBuilder.append("\n- ").append(excursion.getTitle()).append(" on ").append(formatDate(excursion.getDate()));
            }
        }

        // Create the share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareTextBuilder.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

    private String formatDate(long timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date(timestamp));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssociatedExcursions();
        loadVacationDetails();
    }

}
