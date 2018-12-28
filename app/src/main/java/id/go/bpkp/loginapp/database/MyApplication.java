package id.go.bpkp.loginapp.database;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, MyDatabase.NAME)
                .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
                .build();
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}
