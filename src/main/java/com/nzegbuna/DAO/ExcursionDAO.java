package com.nzegbuna.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertExcursion(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions ORDER BY excursionId ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM excursions WHERE vacationId =:vacationId ORDER BY excursionId ASC")
    List<Excursion> getAssociatedExcursions (int vacationId);

    @Query("SELECT * FROM excursions WHERE excursionId = :excursionId")
    Excursion getExcursionById(int excursionId);

    @Query("DELETE FROM excursions WHERE vacationId = :vacationId")
    void deleteExcursionsForVacation(int vacationId);


    }










