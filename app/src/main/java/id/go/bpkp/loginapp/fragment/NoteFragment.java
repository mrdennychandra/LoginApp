package id.go.bpkp.loginapp.fragment;


import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import id.go.bpkp.loginapp.R;
import id.go.bpkp.loginapp.adapter.NoteAdapter;
import id.go.bpkp.loginapp.database.MyApplication;
import id.go.bpkp.loginapp.database.NotesDao;
import id.go.bpkp.loginapp.model.Notes;
import id.go.bpkp.loginapp.scheduling.SyncWorker;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {

    private RecyclerView list;
    private List<Notes> notes;
    private NoteAdapter adapter;
    private NotesDao notesDao;
    private ExecutorService executorService;

    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_note, container, false);
        notesDao = ((MyApplication) getActivity().getApplicationContext()).getMyDatabase().notesDao();
        executorService = Executors.newSingleThreadExecutor();

        adapter = new NoteAdapter(notes,getActivity());
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setAdapter(adapter);
        notesDao.getAll().observeForever(new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {
                adapter.setData(notes);
            }
        });
        notesDao.getAll().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {
                adapter.setData(notes);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        //Periodic work has a minimum interval of 15 minutes and it cannot have an initial delay.
        //https://developer.android.com/reference/androidx/work/PeriodicWorkRequest#MIN_PERIODIC_INTERVAL_MILLIS
        PeriodicWorkRequest.Builder uploadWorker =
                new PeriodicWorkRequest
                        .Builder(SyncWorker.class, 15,
                        TimeUnit.MINUTES).setConstraints(constraints);
        PeriodicWorkRequest photoCheckWork = uploadWorker.build();
        WorkManager.getInstance().enqueue(photoCheckWork);
        return view;
    }

    private void showDialog(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.dialog_input_notes, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        final EditText txtNote = (EditText) promptsView
                .findViewById(R.id.txt_note);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Simpan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                final Notes notes = new Notes();
                                notes.note = txtNote.getText().toString();
                                notes.created = new Date();
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        notesDao.insert(notes);
                                    }
                                });
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
