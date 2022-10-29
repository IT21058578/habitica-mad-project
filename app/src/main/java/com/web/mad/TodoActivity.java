package com.web.mad;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class TodoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    private ProgressDialog loader;

    private String key ="";
    private String task;
    private String description;
    private String date;

    TextView detailText;
    private RelativeLayout RelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_todo);

        detailText = findViewById(R.id.descTv);
        RelativeLayout = findViewById(R.id.expandLayout);

        toolbar = findViewById(R.id.HomeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("To Do List");

        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth .getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Task").child(onlineUserID);

        floatingActionButton = findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }


        });
    }

    private void addTask() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file,null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);


        final EditText task = myView.findViewById(R.id.task);
        final EditText discription = myView.findViewById(R.id.discription);
        Button save = myView.findViewById(R.id.btnsave);
        Button cancel = myView.findViewById(R.id.btncancel);

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        save.setOnClickListener(view -> {
            String mTask = task.getText().toString().trim();
            String mDiscription = discription.getText().toString().trim();
            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());

            if (TextUtils.isEmpty(mTask)){
                task.setError("Task Required");
                return;
            }if (TextUtils.isEmpty(mDiscription)){
                task.setError("Description Required");
                return;
            }else {
                loader.setMessage("Adding your data");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                com.web.mad.Model model = new com.web.mad.Model(mTask,mDiscription,id,date);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(TodoActivity.this, "Task is added successful", Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }else {
                            String error = task.getException().toString();
                            Toast.makeText(TodoActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }

                    }
                });

            }

            dialog.dismiss();

        });

        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
       FirebaseRecyclerOptions<com.web.mad.Model> options = new FirebaseRecyclerOptions.Builder<com.web.mad.Model>()
               .setQuery(reference, com.web.mad.Model.class)
               .build();
       FirebaseRecyclerAdapter<com.web.mad.Model, MyViewHolder> adapter = new FirebaseRecyclerAdapter<com.web.mad.Model, MyViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull com.web.mad.Model model) {
               holder.setDate(model.getDate());
               holder.setTask(model.getTask());
               holder.setDesc(model.getDescription());



               holder.mView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       key = getRef(position).getKey();
                       task =model.getTask();
                       description = model.getDescription();
                       date = model.getDate();

                       updateTask();
                   }
               });

           }

           @NonNull
           @Override
           public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent,false);
               return new MyViewHolder(view);
           }
       };
       recyclerView.setAdapter(adapter);
       adapter.startListening();


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTask(String task){
            TextView taskTechView = mView.findViewById(R.id.taskTv);
            taskTechView.setText(task);
        }
        public void setDesc(String desc){
            TextView taskTechView = mView.findViewById(R.id.descTv);
            taskTechView.setText(desc);
        }

        public void setDate(String date){
            TextView dateTextView = mView.findViewById(R.id.dateTv);
            dateTextView.setText(date);

        }

    }

    private void updateTask(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_data,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mText = view.findViewById(R.id.editTitle);
        EditText mDescription = view.findViewById(R.id.editDesc);

        mText.setText(task);
        mText.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        Button updateBtn = view.findViewById(R.id.btneditUpdate);
        Button deleteBtn = view.findViewById(R.id.btneditdelete);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = mText.getText().toString().trim();
                description = mDescription.getText().toString().trim();
                date =

                date = DateFormat.getDateInstance().format(new Date());
                com.web.mad.Model model = new com.web.mad.Model(task,description,date,key);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(TodoActivity.this, "Data Update Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(TodoActivity.this, "Update Failed"+err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(TodoActivity.this, "Task Delete Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(TodoActivity.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });



        dialog.show();

    }

    public void expand(View view){
        int v = (detailText.getVisibility()==View.GONE)?View.VISIBLE:View.GONE;
        TransitionManager.beginDelayedTransition(RelativeLayout, new AutoTransition());

        detailText.setVisibility(v);
    }
}