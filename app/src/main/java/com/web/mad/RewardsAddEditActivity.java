package com.web.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.web.mad.rewards.Reward;
import com.web.mad.rewards.RewardDeleteDialogFragment;
import com.web.mad.rewards.RewardType;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RewardsAddEditActivity extends AppCompatActivity implements RewardDeleteDialogFragment.OnDeleteDialogBtnClickListener {
    private Button btnDelete;
    private EditText etName;
    private EditText etPrice;
    private EditText etDesc;
    private Button btnSave;
    private Button btnCancel;
    private Spinner spnType;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String uId = user.getUid();

    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference userReference = db.child("users").child(uId);
    private final DatabaseReference rewardsReference = userReference.child("rewards");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_add_edit);

        //Grabbing relevant views.
        btnDelete = findViewById(R.id.rewardAddEditDeleteBtn);
        btnSave = findViewById(R.id.rewardAddEditSaveBtn);
        btnCancel = findViewById(R.id.rewardAddEditCancelBtn);
        etName = findViewById(R.id.rewardAddEditEditTextName);
        etPrice = findViewById(R.id.rewardAddEditEditTextPrice);
        etDesc = findViewById(R.id.rewardAddEditEditTextDescription);
        spnType = findViewById(R.id.rewardAddEditSpinnerType);

        //Populating spinner and decorating spinner.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reward_types,
                R.layout.spinner_reward_item);
        adapter.setDropDownViewResource(R.layout.spinner_reward_item);
        spnType.setPopupBackgroundDrawable(getDrawable(R.drawable.res_rounded_border));
        spnType.setAdapter(adapter);

        //Individualized display logic.
        btnDelete.setVisibility(View.GONE);
        if (getIntent().getStringExtra("rewardName") != null) {
            etName.setText(getIntent().getStringExtra("rewardName"));
            etPrice.setText(getIntent().getStringExtra("rewardPrice"));
            etDesc.setText(getIntent().getStringExtra("rewardDescription"));
            btnDelete.setVisibility(View.VISIBLE);
        }

        //Attaching OnCLickListeners.
        btnDelete.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardAddEditActivity's delete button clicked!");
            showDeleteDialog();
        });

        btnCancel.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardAddEditActivity's cancel button clicked!");
            finish();
        });

        btnSave.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardAddEditActivity's save button clicked!");
            this.onSaveBtnClick();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        String titleText;
        if (getIntent().getStringExtra("rewardName") != null) {
            titleText = getString(R.string.title_rewards_edit_existing);
        } else {
            titleText = getString(R.string.title_rewards_create_new);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(titleText);
        getSupportActionBar().setElevation(0);

        return super.onCreateOptionsMenu(menu);
    }

    private void showDeleteDialog() {
        FragmentManager manager = getSupportFragmentManager();
        RewardDeleteDialogFragment dialog = RewardDeleteDialogFragment.newInstance();
        dialog.show(manager, "fragment_reward_delete_dialog");
    }

    public void onSaveBtnClick() {
        long currentTime = Instant.now().toEpochMilli();
        String documentId = getIntent().getStringExtra("rewardDocumentId");

        OnCompleteListener<Void> rewardSaveListener = task -> {
            if (task.isSuccessful()) {
                Log.d("DEBUG", "Submitted reward successfully");
                Toast.makeText(this, "Reward successfully submitted!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.w("WARN", "Failed to submit reward", task.getException());
                Toast.makeText(this, "Could not submit reward...", Toast.LENGTH_SHORT).show();
            }
        };

        if (documentId.isEmpty()) {
            //Reward doesnt exist
            Reward reward = new Reward(
                    user.getUid(), UUID.randomUUID().toString(),
                    etName.getText().toString(),
                    etDesc.getText().toString(),
                    Integer.parseInt(etPrice.getText().toString()),
                    0, 0, false, false, RewardType.REPEATABLE,
                    currentTime, currentTime, currentTime, currentTime
            );

            rewardsReference.child(reward.getDocumentId())
                    .setValue(reward).addOnCompleteListener(rewardSaveListener);
        } else {
            //Reward exists already
            OnCompleteListener<DataSnapshot> getRewardListener = task -> {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Reward acquired from DB successfully");

                    Reward reward = task.getResult().getValue(Reward.class);
                    reward.setName(etName.getText().toString());
                    reward.setDescription(etDesc.getText().toString());
                    reward.setPrice(Integer.parseInt(etPrice.getText().toString()));

                    rewardsReference.child(reward.getDocumentId())
                            .setValue(reward).addOnCompleteListener(rewardSaveListener);
                }
            };
            rewardsReference.child(documentId)
                    .get().addOnCompleteListener(getRewardListener);
        }
    }

    @Override
    public void onDeleteBtnClick() {
        /* Delete button only works in the case of existing rewards */
        String documentId = getIntent().getStringExtra("rewardDocumentId");
        OnCompleteListener<Void> rewardDeleteListener = task -> {
            if (task.isSuccessful()) {
                Log.d("DEBUG", "Deleted reward successfully");
                Toast.makeText(this, "Reward successfully deleted!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.w("WARN", "Failed to deleted reward", task.getException());
                Toast.makeText(this, "Could not delete reward...", Toast.LENGTH_SHORT).show();
            }
        };

        if (documentId.isEmpty()) {
            //Reward doesn't exist
            Log.e("ERROR", "User tried to delete a reward that does not exist");
        } else {
            //Reward already exists
            rewardsReference.child(documentId)
                    .removeValue().addOnCompleteListener(rewardDeleteListener);
        }
    }
}