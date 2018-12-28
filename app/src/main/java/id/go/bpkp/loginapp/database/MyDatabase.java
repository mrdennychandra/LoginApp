package id.go.bpkp.loginapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import id.go.bpkp.loginapp.model.Notes;

// bump version number if your schema changes
@Database(entities={Notes.class}, version=1)
@TypeConverters({DateConverter.class})
public abstract class MyDatabase extends RoomDatabase {

    // Declare your data access objects as abstract
    public abstract NotesDao notesDao();

    // Database name to be used
    public static final String NAME = "SampeDatabase";
}
