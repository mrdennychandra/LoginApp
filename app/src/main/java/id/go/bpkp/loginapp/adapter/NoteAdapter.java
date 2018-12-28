package id.go.bpkp.loginapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.go.bpkp.loginapp.R;
import id.go.bpkp.loginapp.database.MyApplication;
import id.go.bpkp.loginapp.database.NotesDao;
import id.go.bpkp.loginapp.model.Notes;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private List<Notes> data;
    private Context context;
    private NotesDao notesDao;
    private ExecutorService executorService;

    public NoteAdapter(List<Notes> data, Context context) {
        this.data = data;
        this.context = context;
        notesDao = ((MyApplication) context.getApplicationContext()).getMyDatabase().notesDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_note, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Notes note = data.get(i);
        if(note != null){
            viewHolder.txtNote.setText(note.note);
            viewHolder.txtDate.setText(new SimpleDateFormat("dd-mm-yyyy").format(note.created));
            if(note.sync){
                viewHolder.txtStatus.setText("terkirim");
                viewHolder.btnEdit.setVisibility(View.GONE);
                viewHolder.btnDelete.setVisibility(View.GONE);
            }else{
                viewHolder.txtStatus.setText("belum terkirim");
            }

        }
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(note);
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Hapus data?");
                builder.setPositiveButton("Hapus",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        notesDao.delete(note);
                                    }
                                });
                            }
                        });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNote,txtDate,txtStatus;
        Button btnEdit,btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNote = (TextView) itemView.findViewById(R.id.txt_note);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
            btnEdit = (Button) itemView.findViewById(R.id.btn_edit);
            btnDelete = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }

    public void setData(List<Notes> newData) {
        if (data != null) {
            NoteDiffCallback postDiffCallback = new NoteDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            // first initialization
            data = newData;
        }
    }

    class NoteDiffCallback extends DiffUtil.Callback {

        private final List<Notes> oldPosts, newPosts;

        public NoteDiffCallback(List<Notes> oldPosts, List<Notes> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).id == newPosts.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }

    private void showEditDialog(final Notes notes){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_input_notes, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText txtNote = (EditText) promptsView
                .findViewById(R.id.txt_note);
        txtNote.setText(notes.note);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Simpan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                notes.note = txtNote.getText().toString();
                                notes.created = new Date();
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        notesDao.update(notes);
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
