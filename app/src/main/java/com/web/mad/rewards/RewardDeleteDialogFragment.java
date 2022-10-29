package com.web.mad.rewards;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.web.mad.R;

public class RewardDeleteDialogFragment extends DialogFragment {
    private Button deleteBtn;
    private Button cancelBtn;

    private OnDeleteDialogBtnClickListener listener;

    public RewardDeleteDialogFragment() {
        // Required empty public constructor
    }

    public static RewardDeleteDialogFragment newInstance() {
        RewardDeleteDialogFragment fragment = new RewardDeleteDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDeleteDialogBtnClickListener) {
            listener = (OnDeleteDialogBtnClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement RewardItemFragment.OnRewardItemBtnClickListener !");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reward_delete_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelBtn = view.findViewById(R.id.rewardDeleteDialogCancelBtn);
        cancelBtn.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardDeleteDialogFragment's cancel button clicked!");
            dismiss();
        });

        deleteBtn = view.findViewById(R.id.rewardDeleteDialogDeleteBtn);
        deleteBtn.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardDeleteDialogFragment's delete button clicked!");
            listener.onDeleteBtnClick();
        });
    }

    public interface OnDeleteDialogBtnClickListener {
        void onDeleteBtnClick();
    }
}