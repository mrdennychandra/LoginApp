package id.go.bpkp.loginapp.scheduling;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import id.go.bpkp.loginapp.database.MyApplication;
import id.go.bpkp.loginapp.database.NotesDao;
import id.go.bpkp.loginapp.model.Notes;

public class SyncWorker extends Worker {

    private NotesDao notesDao;
    private ExecutorService executorService;

    public SyncWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        notesDao = ((MyApplication) context.getApplicationContext()).getMyDatabase().notesDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public Result doWork() {
        new Thread() {
            public void run() {
                notesDao.getByNotSync(false).observeForever(new Observer<List<Notes>>() {
                    @Override
                    public void onChanged(@Nullable List<Notes> notes) {
                        //call API
                        //update status
                        for(Notes note : notes){
                            note.sync = true;
                        }
                        notesDao.update(notes);
                        Log.d("UploadWorker",notes.size()+"");
                    }
                });
            }
        }.start();
        return Result.success();
    }
}