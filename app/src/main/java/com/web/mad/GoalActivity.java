package com.web.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class GoalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    private ProgressDialog loader;

    private String key ="";
    private String goal;
    private String goalDiscription;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goal);

        toolbar = findViewById(R.id.HomeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Goals");

        recyclerView = findViewById(R.id.recycleGoal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth .getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Goals").child(onlineUserID);

        floatingActionButton = findViewById(R.id.addGoal);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGoal();
            }
        });
    }

    private void addGoal() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_goal,null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);


        final EditText Goal = myView.findViewById(R.id.GoalName);
        final EditText GoalDiscription = myView.findViewById(R.id.GoalDiscription);
        Button save = myView.findViewById(R.id.GoalAdd);
        Button cancel = myView.findViewById(R.id.GoalCancel);

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        save.setOnClickListener(view -> {
            String mGoal = Goal.getText().toString().trim();
            String mGoalDiscription = GoalDiscription.getText().toString().trim();
            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());

            if (TextUtils.isEmpty(mGoal)){
                Goal.setError("Goal Required");
                return;
            }if (TextUtils.isEmpty(mGoalDiscription)){
                GoalDiscription.setError("Description Required");
                return;
            }else {
                loader.setMessage("Adding your data");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                com.web.mad.Model4 model = new com.web.mad.Model4(id,date,mGoal,mGoalDiscription);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(GoalActivity.this, "Goal is added successful", Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }else {
                            String error = task.getException().toString();
                            Toast.makeText(GoalActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }

                    }
                });

            }

            dialog.dismiss();

        });

        dialog.show();


    }

    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model4> options = new FirebaseRecyclerOptions.Builder<com.web.mad.Model4>()
                .setQuery(reference, com.web.mad.Model4.class)
                .build();
        FirebaseRecyclerAdapter<Model4, GoalActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<com.web.mad.Model4, GoalActivity.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GoalActivity.MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull com.web.mad.Model4 model) {
                holder.setDate(model.getDate());
                holder.setGoalTitle(model.getGoalTitle());
                holder.setGoalDescription(model.getGoalDiscription());



                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = getRef(position).getKey();
                        goal =model.getGoalTitle();
                        goalDiscription = model.getGoalDiscription();
                        date = model.getDate();

                        updateGoal();
                    }
                });

            }

            @NonNull
            @Override
            public GoalActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrived_layout_goal,parent,false);
                return new GoalActivity.MyViewHolder(view);
//
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

        public void setGoalTitle(String goal){
            TextView taskTechView = mView.findViewById(R.id.goalTitleTv);
            taskTechView.setText(goal);
        }
        public void setGoalDescription(String goalDescription){
            TextView taskTechView = mView.findViewById(R.id.GoalDiscriptionTv);
            taskTechView.setText(goalDescription);
        }

        public void setDate(String date){
            TextView dateTextView = mView.findViewById(R.id.dateTv);
            dateTextView.setText(date);

        }

    }

    private void updateGoal() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_data_goal,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mText = view.findViewById(R.id.TitleEditGoal);
        EditText mDescription = view.findViewById(R.id.descriptionEditGoal);

        mText.setText(goal);
        mText.setSelection(goal.length());

        mDescription.setText(goalDiscription);
        mDescription.setSelection(goalDiscription.length());

        Button updateBtn = view.findViewById(R.id.btneditUpdateGoal);
        Button deleteBtn = view.findViewById(R.id.btneditdeleteGoal);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goal = mText.getText().toString().trim();
                goalDiscription = mDescription.getText().toString().trim();
                date =

                        date = DateFormat.getDateInstance().format(new Date());
                com.web.mad.Model4 model = new com.web.mad.Model4(key,date,goal,goalDiscription);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(GoalActivity.this, "Data Update Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(GoalActivity.this, "Update Failed"+err, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GoalActivity.this, "Goal Delete Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(GoalActivity.this, "Failed to delete Goal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });



        dialog.show();



    }


}