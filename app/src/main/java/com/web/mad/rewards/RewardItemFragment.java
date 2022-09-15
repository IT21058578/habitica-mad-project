package com.web.mad.rewards;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.web.mad.R;

import java.time.LocalDateTime;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RewardItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RewardItemFragment extends Fragment {
    private ConstraintLayout itemLayout;
    private ConstraintLayout detailsLayout;
    private TextView detailsDescriptionTxt;
    private TextView detailsLastBoughtTxt;
    private TextView detailsLastEditedTxt;
    private TextView detailsLastUsedTxt;
    private TextView detailsCreatedTxt;
    private TextView detailsBoughtTxt;
    private TextView detailsUsedTxt;
    private TextView boughtTxt;
    private TextView priceTxt;
    private TextView nameTxt;
    private TextView typeTxt;
    private Button editBtn;
    private Button buyBtn;
    private Button useBtn;

    //Used to link-together parent activity and fragment so fragment can cause
    //parent to fire certain actions when relevant events occur.
    private OnRewardItemBtnClickListener listener;

    public RewardItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reward_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Getting all passed arguments.
        String rewardDescription = getArguments().getString("rewardDescription", "...");
        String rewardLastBought = getArguments().getString("rewardLastBought", "...");
        String rewardLastEdited = getArguments().getString("rewardLastEdited", "...");
        String rewardLastUsed = getArguments().getString("rewardLastUsed", "...");
        String rewardCreated = getArguments().getString("rewardCreated", "...");
        String rewardBought = getArguments().getString("rewardBought", "0");
        String rewardPrice = getArguments().getString("rewardPrice", "0");
        String rewardUsed = getArguments().getString("rewardUsed", "0");
        String rewardType = getArguments().getString("rewardType", "...");
        String rewardName = getArguments().getString("rewardName", "...");

        //Grabbing all dynamic views in the reward item.
        detailsDescriptionTxt = view.findViewById(R.id.rewardItemDetailsDescription);
        detailsLastBoughtTxt = view.findViewById(R.id.rewardItemDetailsLastBought);
        detailsLastEditedTxt = view.findViewById(R.id.rewardItemDetailsLastEdited);
        detailsLastUsedTxt = view.findViewById(R.id.rewardItemDetailsLastUsed);
        detailsCreatedTxt = view.findViewById(R.id.rewardItemDetailsCreated);
        detailsBoughtTxt = view.findViewById(R.id.rewardItemDetailsBought);
        detailsUsedTxt = view.findViewById(R.id.rewardItemDetailsUsed);
        detailsLayout = view.findViewById(R.id.rewardItemDetails);
        itemLayout = view.findViewById(R.id.rewardItemLayout);
        boughtTxt = view.findViewById(R.id.rewardItemBought);
        priceTxt = view.findViewById(R.id.rewardItemPrice);
        nameTxt = view.findViewById(R.id.rewardItemName);
        typeTxt = view.findViewById(R.id.rewardItemType);
        editBtn = view.findViewById(R.id.rewardItemEdit);
        buyBtn = view.findViewById(R.id.rewardItemBuy);
        useBtn = view.findViewById(R.id.rewardItemUse);

        //Creating all view details with args.
        detailsDescriptionTxt.setText(rewardDescription);
        detailsLastBoughtTxt.setText(rewardLastBought);
        detailsLastEditedTxt.setText(rewardLastEdited);
        detailsLastUsedTxt.setText(rewardLastUsed);
        detailsCreatedTxt.setText(rewardCreated);
        detailsBoughtTxt.setText(rewardBought);
        detailsUsedTxt.setText(rewardUsed);
        boughtTxt.setText(rewardBought);
        priceTxt.setText(rewardPrice);
        typeTxt.setText(rewardType);
        nameTxt.setText(rewardName);

        detailsLayout.setVisibility(View.GONE);

        //Adding eventListeners and handling events.

        buyBtn.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardItemFragment's buy button clicked!");
            listener.onBuyBtnClick();
        });

        useBtn.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardItemFragment's use button clicked!");
            listener.onUseBtnClick();
        });

        editBtn.setOnClickListener(v -> {
            Log.d("DEBUG", "RewardItemFragment's edit button clicked!");
            listener.onEditBtnClick();
        });

        itemLayout.setOnClickListener(v -> {
            //Toggle showing extra details.
            Log.d("DEBUG", "RewardItemFragment's layout tapped!");
            if (detailsLayout.getVisibility() == View.GONE) {
                detailsLayout.setVisibility(View.VISIBLE);
            } else {
                detailsLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRewardItemBtnClickListener) {
            listener = (OnRewardItemBtnClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement RewardItemFragment.OnRewardItemBtnClickListener !");
        }
    }

    public static RewardItemFragment newInstance(Reward reward) {
        RewardItemFragment rewardItemFragment = new RewardItemFragment();

        Bundle args = new Bundle();
        args.putString("rewardName", reward.getName());
        args.putString("rewardType", reward.getType().getText());
        args.putString("rewardPrice", Integer.toString(reward.getPrice()));
        args.putString("rewardBought",Integer.toString(reward.getBought()));
        args.putString("rewardUsed", Integer.toString(reward.getUsed()));
        args.putString("rewardLastBought", formatDateTime(reward.getLastBoughtAt()));
        args.putString("rewardLastUsed", formatDateTime(reward.getLastUsedAt()) );
        args.putString("rewardLastEdited", formatDateTime(reward.getLastEditedAt()) );
        args.putString("rewardCreated", formatDateTime(reward.getCreatedAt()) );
        args.putString("rewardDescription", reward.getDescription());

        rewardItemFragment.setArguments(args);
        return rewardItemFragment;
    }

    private static String formatDateTime (LocalDateTime dateTime) {
        return String.format("%d / %d / %d", dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getYear());
    }

    public interface OnRewardItemBtnClickListener {
        void onBuyBtnClick();
        void onUseBtnClick();
        void onEditBtnClick();
    }
}