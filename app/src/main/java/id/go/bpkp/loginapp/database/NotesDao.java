package id.go.bpkp.loginapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import id.go.bpkp.loginapp.model.Notes;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")
    public LiveData<List<Notes>> getAll();

    @Insert
    public Long insert(Notes notes);

    @Update
    void update(Notes notes);

    @Update
    void update(List<Notes> notes);

    @Delete
    public void delete(Notes notes);

    @Query("SELECT * FROM notes WHERE id=:id")
    public Notes getById(int id);

    @Query("SELECT * FROM notes WHERE sync=:sync")
    public LiveData<List<Notes>> getByNotSync(boolean sync);
}