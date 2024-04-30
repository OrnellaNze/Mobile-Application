package com.nzegbuna.Entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "excursions")

public class Excursion {
    @PrimaryKey(autoGenerate = true)

    private int excursionId;

    private String title;
    private long date;
    private int vacationId;


    public Excursion() {

    }

    public Excursion(int excursionId, String title, long date, int vacationId) {
        this.excursionId = excursionId;
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
    }

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}