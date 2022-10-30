package com.web.mad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class HomeActivity extends AppCompatActivity {


    private CardView loginBtn,budgetcal,goallist,rewards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);


        loginBtn = findViewById(R.id.todolist);
        budgetcal = findViewById(R.id.budgetCal);
        goallist = findViewById(R.id.goallist);
        rewards = findViewById(R.id.rewards);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, com.web.mad.TodoActivity.class);
                startActivity(intent);
            }
        });

        budgetcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, com.web.mad.BudgetCal.class);
                startActivity(intent);
            }
        });


        goallist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, com.web.mad.GoalActivity.class);
                startActivity(intent);
            }
        });

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, com.web.mad.RewardsMainActivity.class);
                startActivity(intent);
            }
        });
    }
}