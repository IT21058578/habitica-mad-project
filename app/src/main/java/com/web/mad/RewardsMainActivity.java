package com.web.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.web.mad.rewards.Reward;
import com.web.mad.rewards.RewardItemFragment;
import com.web.mad.rewards.RewardType;
import com.web.mad.user.User;

import org.w3c.dom.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RewardsMainActivity extends AppCompatActivity implements RewardItemFragment.OnRewardItemBtnClickListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_main);

    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getSupportActionBar().setTitle(R.string.title_rewards);
        getSupportActionBar().setElevation(0);
        getMenuInflater().inflate(R.menu.menu_rewards,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionAddRewardBtn) {
            startActivity(new Intent(this, RewardsAddEditActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBuyBtnClick(String userId, String documentId, String tag) {

    }

    @Override
    public void onUseBtnClick(String userId, String documentId, String tag) {

    }

    @Override
    public void onEditBtnClick(String userId, String documentId, String tag) {
        Fragment rewardItemFragment = getSupportFragmentManager().findFragmentByTag(tag);
        String name = rewardItemFragment.getArguments().getString("rewardName", "");
        String description = rewardItemFragment.getArguments().getString("rewardDescription", "");
        int price = Integer.parseInt(rewardItemFragment.getArguments().getString("rewardPrice", "0"));

        Intent intent = new Intent(this, RewardsAddEditActivity.class);
        intent.putExtra("rewardName", name);
        intent.putExtra("rewardPrice", price);
        intent.putExtra("rewardDescription", description);
        startActivity(intent);
    }
}