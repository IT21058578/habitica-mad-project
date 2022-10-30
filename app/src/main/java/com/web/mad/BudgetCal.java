package com.web.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BudgetCal extends AppCompatActivity {

    private Button incomeBtn,expenceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_cal);

        incomeBtn = findViewById(R.id.incomebtn);
        expenceBtn = findViewById(R.id.expenceBtn);

        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BudgetCal.this, com.web.mad.IncomeActivity.class);
                startActivity(intent);
            }
        });

        expenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BudgetCal.this, com.web.mad.ExpenceActivity.class);
                startActivity(intent);
            }
        });
    }
}