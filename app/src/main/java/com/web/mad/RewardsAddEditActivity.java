package com.web.mad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Button;

import com.web.mad.R;
import com.web.mad.rewards.RewardDeleteDialogFragment;

public class RewardsAddEditActivity extends AppCompatActivity {
    private Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_add_edit);

        editBtn = findViewById(R.id.rewardAddEditDeleteBtn);
        editBtn.setOnClickListener(v -> {
            showDeleteDialog();
        });
    }

    private void showDeleteDialog() {
        FragmentManager manager = getSupportFragmentManager();
        RewardDeleteDialogFragment dialog = RewardDeleteDialogFragment.newInstance();
        dialog.show(manager, "fragment_reward_delete_dialog");
    }
}