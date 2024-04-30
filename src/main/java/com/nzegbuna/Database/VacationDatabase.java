package com.nzegbuna.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.nzegbuna.DAO.ExcursionDAO;
import com.nzegbuna.DAO.VacationDAO;
import com.nzegbuna.Entities.Excursion;
import com.nzegbuna.Entities.Vacation;



@Database(entities = {Vacation.class, Excursion.class}, version = 17)
public abstract class VacationDatabase extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    public static volatile VacationDatabase INSTANCE;

    public static VacationDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (VacationDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VacationDatabase.class,"VacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
