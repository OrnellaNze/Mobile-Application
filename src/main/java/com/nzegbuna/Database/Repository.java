package com.nzegbuna.Database;


import android.app.Application;
import androidx.lifecycle.LiveData;
import com.nzegbuna.DAO.ExcursionDAO;
import com.nzegbuna.DAO.VacationDAO;
import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Future;
import java.util.function.Consumer;
public class Repository {
    private static final int NUMBER_OF_THREADS = 4;
    private final ExcursionDAO mExcursionDAO;
    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;
    private final VacationDAO mVacationDAO;
    private final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public Repository(Application application) {
        VacationDatabase db = VacationDatabase.getDatabase(application);
        this.mVacationDAO = db.vacationDAO();
        this.mExcursionDAO = db.excursionDAO();
    }

    public List<Vacation> getAllVacations() {
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllVacations;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.insert(vacation);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void deleteVacation(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public List<Excursion> getAllExcursions() {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcursions;
    }

    public List<Excursion> getAssociatedExcursions(int vacationId) {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcursions;
    }

    public void insertExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.insertExcursion(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void deleteExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Vacation getVacationById(int vacationId) {
        // Use a Callable within the submit() to properly handle the DAO's synchronous call
        Future<Vacation> future = databaseExecutor.submit(() -> mVacationDAO.getVacationById(vacationId));
        try {
            return future.get();  // This will block the thread until the result is available
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasExcursions(int vacationId) {
        List<Excursion> excursions = getAssociatedExcursions(vacationId);
        return !excursions.isEmpty();
    }


    public Excursion getExcursionById(int excursionId) {
        Future<Excursion> future = databaseExecutor.submit(() -> mExcursionDAO.getExcursionById(excursionId));
        try {
            return future.get();  // This will block the thread until the result is available, make sure this is not the main thread
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}