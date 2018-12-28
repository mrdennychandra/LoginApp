package id.go.bpkp.loginapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "notes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String note;
    public Date created;
    public Boolean sync = Boolean.FALSE;
}