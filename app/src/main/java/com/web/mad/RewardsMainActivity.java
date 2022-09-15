package com.web.mad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.web.mad.rewards.Reward;
import com.web.mad.rewards.RewardItemFragment;
import com.web.mad.rewards.RewardType;

import java.time.LocalDateTime;

public class RewardsMainActivity extends AppCompatActivity implements RewardItemFragment.OnRewardItemBtnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_main);

        Reward reward = new Reward("documentId", "userId", "Eat pizza", "You get to eat some pizza!",
                10, 5, 1, RewardType.ONETIME,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        //Code to add reward-item fragment to the layout.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.scrollViewLayout, RewardItemFragment.newInstance(reward)); //new with args.
        transaction.commit();
    }

    @Override
    public void onBuyBtnClick() {
        //TODO: Implement method
    }

    @Override
    public void onUseBtnClick() {
        //TODO: Implement method
    }

    @Override
    public void onEditBtnClick() {
        startActivity(new Intent(RewardsMainActivity.this, RewardsAddEditActivity.class));
    }
}