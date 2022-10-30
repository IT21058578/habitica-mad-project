package com.web.mad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;
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
import java.util.UUID;

public class RewardsMainActivity extends AppCompatActivity implements RewardItemFragment.OnRewardItemBtnClickListener {

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String uId = user.getUid();

    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference userReference = db.child("users").child(uId);
    private final DatabaseReference rewardsReference = userReference.child("rewards");
    private int currency = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_main);

        //TODO: ValueEventListener for currency.
        ValueEventListener currencyListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                currency = user.getCurrency();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("WARN", "No longer keeping up to date with user currency", error.toException());
            }
        };
        userReference.addValueEventListener(currencyListener);

        ValueEventListener rewardsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (Fragment fragment: getSupportFragmentManager().getFragments()) {
                    transaction.remove(fragment);
                }

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Reward reward = dataSnapshot.getValue(Reward.class);
                    String tag = UUID.randomUUID().toString();
                    transaction.add(R.id.scrollViewLayout, RewardItemFragment.newInstance(reward), tag);
                }
                transaction.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("WARN", "No longer keeping up to date with user rewards", error.toException());
            }
        };
        rewardsReference.addValueEventListener(rewardsListener);
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
        DatabaseReference rewardReference = rewardsReference.child(documentId);
        OnCompleteListener<DataSnapshot> listener = task -> {
            if (!task.isSuccessful()) {
                Log.w("WARN","Could not access desired reward");
                Toast.makeText(this, "Couldn't buy reward...", Toast.LENGTH_SHORT).show();
                return;
            }

            Reward reward = task.getResult().getValue(Reward.class);
            if (currency < reward.getPrice()) {
                Log.w("WARN", "User does not have enough currency to buy reward");
                Toast.makeText(this, "Not enough currency...", Toast.LENGTH_SHORT).show();
                return;
            }

            currency = currency - reward.getPrice();
            reward.buyOne();
            rewardReference.setValue(reward);
            userReference.child("currency").setValue(currency);
        };
    }

    @Override
    public void onUseBtnClick(String userId, String documentId, String tag) {
        DatabaseReference rewardReference = rewardsReference.child(documentId);
        OnCompleteListener<DataSnapshot> listener = task -> {
            if (!task.isSuccessful()) {
                Log.w("WARN","Could not access desired reward");
                Toast.makeText(this, "Couldn't use reward...", Toast.LENGTH_SHORT).show();
                return;
            }

            Reward reward = task.getResult().getValue(Reward.class);
            if (reward.getAvailable() < 1) {
                Log.w("WARN", "User needs more of the reward");
                Toast.makeText(this, "Not enough rewards in stock. Buy more!", Toast.LENGTH_SHORT).show();
                return;
            }

            reward.useOne();
            rewardReference.setValue(reward);
        };
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
        intent.putExtra("rewardDocumentId", documentId);
        startActivity(intent);
    }
}