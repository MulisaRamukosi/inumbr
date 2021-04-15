package com.puzzle.industries.inumbr.bottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.puzzle.industries.inumbr.R;
import com.puzzle.industries.inumbr.adapters.BallsAdapter;
import com.puzzle.industries.inumbr.components.GridItemDecoration;
import com.puzzle.industries.inumbr.dataModels.Ball;
import com.puzzle.industries.inumbr.databinding.BottomSheetNumberInputBinding;
import com.puzzle.industries.inumbr.interfaces.BallClickListener;
import com.puzzle.industries.inumbr.interfaces.BallSelectionListener;

import java.util.List;

public class InputBottomSheet extends BottomSheetDialogFragment implements BallClickListener {

    private BallsAdapter mAdapter;
    private BottomSheetNumberInputBinding mBinding;
    private BallSelectionListener mSelectionListener;
    private final List<Ball> SELECTION_BALLS;

    public InputBottomSheet(List<Ball> selection_balls) {
        this.SELECTION_BALLS = selection_balls;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomSheetNumberInputBinding.inflate(inflater, container, false);
        androidx.core.view.ViewCompat.setNestedScrollingEnabled(mBinding.rvNumberList, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int margins = (int) getResources().getDimension(R.dimen.mp_4dp);

        mAdapter = new BallsAdapter(SELECTION_BALLS, this);

        mBinding.rvNumberList.addItemDecoration(new GridItemDecoration(margins));
        mBinding.rvNumberList.setAdapter(mAdapter);

    }

    @Override
    public void ballOnClick(int position) {
        Ball ball = SELECTION_BALLS.get(position);
        ball.setSelected(!ball.isSelected());
        mAdapter.notifyItemChanged(position);
        mSelectionListener.ballSelected(ball);
    }

    public void setBallSelectionListener(BallSelectionListener selectionListener) {
        mSelectionListener = selectionListener;
    }
}
