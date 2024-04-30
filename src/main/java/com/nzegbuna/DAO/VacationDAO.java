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

@Dao
public interface VacationDAO {
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(Vacation vacation);

   @Update
   void update(Vacation vacation);

   @Delete
   void delete(Vacation vacation);
   @Query("SELECT * FROM vacations WHERE vacationId = :vacationId")
   Vacation getVacationById(int vacationId);

   @Query("SELECT COUNT(*) FROM vacations WHERE vacationId = :vacationId")
   int countVacationById(int vacationId);


   @Query("SELECT * FROM vacations ORDER BY vacationId ASC")
   List<Vacation> getAllVacations();

}