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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.web.mad.rewards.Reward;
import com.web.mad.rewards.RewardDeleteDialogFragment;

import java.util.Map;
import java.util.Objects;

public class RewardsAddEditActivity extends AppCompatActivity implements RewardDeleteDialogFragment.OnDeleteDialogBtnClickListener {
    private Button btnDelete;
    private EditText etName;
    private EditText etPrice;
    private EditText etDesc;
    private Button btnSave;
    private Button btnCancel;
    private Spinner spnType;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

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
        String userId = getIntent().getStringExtra("userId");
        String documentId = getIntent().getStringExtra("documentId");
        DocumentReference docRef = db.collection("users").document(userId)
                .collection("rewards").document(documentId);
        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("DEBUG","Successfully retrieved document from firestore.");
                            Reward reward = document.toObject(Reward.class);
                            assert reward != null;
                            //TODO: Logic to change reward type.
                            reward.setName(getIntent().getStringExtra("rewardName"));
                            reward.setPrice(Integer.parseInt(getIntent().getStringExtra("rewardPrice")));
                            reward.setDescription(getIntent().getStringExtra("rewardDescription"));
                            docRef.update(mapper.convertValue(reward, Map.class));
                            finish(); //TODO: Logic to go back with some confirmation.
                        } else {
                            Log.e("ERROR", "Document does not exist");
                        }
                    } else {
                        Log.e("ERROR", "Could not get data from firestore.");
                    }
                });
    }

    @Override
    public void onDeleteBtnClick() {
        String userId = getIntent().getStringExtra("userId");
        String documentId = getIntent().getStringExtra("documentId");
        DocumentReference docRef = db.collection("users").document(userId)
                .collection("rewards").document(documentId);
        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("DEBUG","Successfully retrieved document from firestore.");
                            docRef.delete();
                        } else {
                            Log.e("ERROR", "Document does not exist");
                        }
                    } else {
                        Log.e("ERROR", "Could not get data from firestore.");
                    }
                });
    }
}