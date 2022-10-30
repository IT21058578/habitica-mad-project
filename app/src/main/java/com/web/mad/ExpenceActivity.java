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

public class ExpenceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButtonIncome;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    private ProgressDialog loader;

    private String key ="";
    private String nameExpence;
    private String descriptionExpence;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expence);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        toolbar = findViewById(R.id.HomeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expence");

        recyclerView = findViewById(R.id.recycleExpence);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth .getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Expence").child(onlineUserID);

        floatingActionButtonIncome = findViewById(R.id.addExpence);
        floatingActionButtonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpence();
            }


        });
    }

    private void addExpence() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_expence,null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText nameExpence = myView.findViewById(R.id.Expenceename);
        final EditText amountExpence = myView.findViewById(R.id.Expenceamount);
        Button save = myView.findViewById(R.id.btnsaveExpence);
        Button cancel = myView.findViewById(R.id.btncancelExpence);

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        save.setOnClickListener(view -> {
            String mTask = nameExpence.getText().toString().trim();
            String mDiscription = amountExpence.getText().toString().trim();
            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());

            if (TextUtils.isEmpty(mTask)){
                nameExpence.setError("Title Required");
                return;
            }if (TextUtils.isEmpty(mDiscription)){
                amountExpence.setError("Amount Required");
                return;
            }else {
                loader.setMessage("Adding your data");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                Model3 model = new Model3(id,date,mTask,mDiscription);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(ExpenceActivity.this, "Income is added successful", Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }else {
                            String error = task.getException().toString();
                            Toast.makeText(ExpenceActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
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
        FirebaseRecyclerOptions<Model3> options = new FirebaseRecyclerOptions.Builder<com.web.mad.Model3>()
                .setQuery(reference, com.web.mad.Model3.class)
                .build();
        FirebaseRecyclerAdapter<Model3, ExpenceActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<com.web.mad.Model3, ExpenceActivity.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ExpenceActivity.MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull com.web.mad.Model3 model) {
                holder.setDate(model.getDate());
                holder.setExpenceTitle(model.getExpenceTitle());
                holder.setExpenceAmount(model.getExpenceAmount());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = getRef(position).getKey();
                        nameExpence =model.getExpenceTitle();
                        descriptionExpence = model.getExpenceAmount();
                        date = model.getDate();

                        updateTask();
                    }
                });

            }

            @NonNull
            @Override
            public ExpenceActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrived_layout_expence,parent,false);
                return  new ExpenceActivity.MyViewHolder(view);
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

        public void setExpenceTitle(String task){
            TextView taskTechView = mView.findViewById(R.id.ExpenceenameTv);
            taskTechView.setText(task);
        }
        public void setExpenceAmount(String desc){
            TextView taskTechView = mView.findViewById(R.id.ExpenceAmountTv);
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
        View view = inflater.inflate(R.layout.update_data_expence,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mText = view.findViewById(R.id.titleExpenceedit);
        EditText mDescription = view.findViewById(R.id.amountExpenceedit);

        mText.setText(nameExpence);
        mText.setSelection(nameExpence.length());

        mDescription.setText(descriptionExpence);
        mDescription.setSelection(descriptionExpence.length());

        Button updateBtn = view.findViewById(R.id.btneditUpdateExpence);
        Button deleteBtn = view.findViewById(R.id.btneditdeleteExpence);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameExpence = mText.getText().toString().trim();
                descriptionExpence = mDescription.getText().toString().trim();
                date =

                        date = DateFormat.getDateInstance().format(new Date());
                com.web.mad.Model3 model = new com.web.mad.Model3(key,date,nameExpence,descriptionExpence);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ExpenceActivity.this, "Data Update Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(ExpenceActivity.this, "Update Failed"+err, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ExpenceActivity.this, "Expence Delete Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(ExpenceActivity.this, "Failed to delete Expence", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });



        dialog.show();

    }


}