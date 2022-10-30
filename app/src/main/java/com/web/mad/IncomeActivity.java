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
import android.widget.RelativeLayout;
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

public class IncomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButtonIncome;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    private ProgressDialog loader;

    private String key ="";
    private String nameIncome;
    private String descriptionIncome;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_income);

        toolbar = findViewById(R.id.HomeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Incomes");

        recyclerView = findViewById(R.id.recycleIncome);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth .getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Income").child(onlineUserID);

        floatingActionButtonIncome = findViewById(R.id.addIncome);
        floatingActionButtonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }


        });
    }

    private void addTask() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_income,null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText nameIncome = myView.findViewById(R.id.Incomename);
        final EditText amountIncome = myView.findViewById(R.id.Incomeamount);
        Button save = myView.findViewById(R.id.btnsaveIncome);
        Button cancel = myView.findViewById(R.id.btncancelIncome);

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        save.setOnClickListener(view -> {
            String mTask = nameIncome.getText().toString().trim();
            String mDiscription = amountIncome.getText().toString().trim();
            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());

            if (TextUtils.isEmpty(mTask)){
                nameIncome.setError("Title Required");
                return;
            }if (TextUtils.isEmpty(mDiscription)){
                amountIncome.setError("Amount Required");
                return;
            }else {
                loader.setMessage("Adding your data");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                com.web.mad.Model2 model = new com.web.mad.Model2(id,date,mDiscription,mTask);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(IncomeActivity.this, "Income is added successful", Toast.LENGTH_SHORT).show();
                            loader.dismiss();
                        }else {
                            String error = task.getException().toString();
                            Toast.makeText(IncomeActivity.this, "Failed"+error, Toast.LENGTH_SHORT).show();
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
        FirebaseRecyclerOptions<Model2> options = new FirebaseRecyclerOptions.Builder<com.web.mad.Model2>()
                .setQuery(reference, com.web.mad.Model2.class)
                .build();
        FirebaseRecyclerAdapter<Model2, IncomeActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<com.web.mad.Model2, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IncomeActivity.MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull com.web.mad.Model2 model) {
                holder.setDate(model.getDate());
                holder.setIncomeTitle(model.getIncomeTitle());
                holder.setIncomeAmount(model.getIncomeAmount());



                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = getRef(position).getKey();
                        nameIncome =model.getIncomeTitle();
                        descriptionIncome = model.getIncomeAmount();
                        date = model.getDate();

                        updateTask();
                    }
                });

            }

            @NonNull
            @Override
            public IncomeActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrived_layout_income,parent,false);
                return new IncomeActivity.MyViewHolder(view);
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

        public void setIncomeTitle(String task){
            TextView taskTechView = mView.findViewById(R.id.IncomenameTv);
            taskTechView.setText(task);
        }
        public void setIncomeAmount(String desc){
            TextView taskTechView = mView.findViewById(R.id.IncomeAmountTv);
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
        View view = inflater.inflate(R.layout.update_data_income,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mText = view.findViewById(R.id.titleIncomeedit);
        EditText mDescription = view.findViewById(R.id.amountIncomedit);

        mText.setText(nameIncome);
        mText.setSelection(nameIncome.length());

        mDescription.setText(descriptionIncome);
        mDescription.setSelection(descriptionIncome.length());

        Button updateBtn = view.findViewById(R.id.btneditUpdateIncome);
        Button deleteBtn = view.findViewById(R.id.btneditdeleteIncom);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameIncome = mText.getText().toString().trim();
                descriptionIncome = mDescription.getText().toString().trim();
                date =

                        date = DateFormat.getDateInstance().format(new Date());
                com.web.mad.Model2 model = new com.web.mad.Model2(key,date,descriptionIncome,nameIncome);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(IncomeActivity.this, "Data Update Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(IncomeActivity.this, "Update Failed"+err, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(IncomeActivity.this, "Income Delete Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(IncomeActivity.this, "Failed to delete Income", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });



        dialog.show();

    }
}