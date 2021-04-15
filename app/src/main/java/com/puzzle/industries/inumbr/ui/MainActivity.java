package com.puzzle.industries.inumbr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.puzzle.industries.inumbr.R;
import com.puzzle.industries.inumbr.adapters.BallsAdapter;
import com.puzzle.industries.inumbr.bottomSheet.CredentialsBottomSheet;
import com.puzzle.industries.inumbr.bottomSheet.InputBottomSheet;
import com.puzzle.industries.inumbr.components.GridItemDecoration;
import com.puzzle.industries.inumbr.dataModels.Ball;
import com.puzzle.industries.inumbr.databinding.ActivityMainBinding;
import com.puzzle.industries.inumbr.interfaces.BallSelectionListener;
import com.puzzle.industries.inumbr.tasks.GenerateComboTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements BallSelectionListener {

    private FragmentManager mFragmentManager;
    private ActivityMainBinding mBinding;
    private InputBottomSheet mInputBottomSheet;

    private final List<Ball> SELECTED_BALLS = new ArrayList<>();
    private final List<Ball> SELECTION_BALLS = new ArrayList<>();

    private final BallsAdapter SELECTED_BALLS_ADAPTER = new BallsAdapter(SELECTED_BALLS, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mFragmentManager = getSupportFragmentManager();

        //generate selection balls
        for (int i = 1; i <= 49; i++){
            SELECTION_BALLS.add(new Ball(i));
        }

        int margins = (int) getResources().getDimension(R.dimen.mp_4dp);

        mBinding.rvSelectedNums.addItemDecoration(new GridItemDecoration(margins));
        mBinding.rvSelectedNums.setAdapter(SELECTED_BALLS_ADAPTER);

        //mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        //mViewModel.getCombinations().observe(this, this::displayTheCombinations);

        mBinding.rvCombHolder.setAdapter(combinationsAdapter);
        mBinding.rvCombHolder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mBinding.ibtnAddNumber.setOnClickListener(v -> tableViewController.addNumberInput());

        mBinding.btnClear.setOnClickListener(v -> {
            mBinding.edtCombLength.setText("");
            tableViewController.removeAllViews();
            mBinding.rvCombHolder.removeAllViews();
        });

        mBinding.btnCombGen.setOnClickListener(v -> {
            List<Integer> selectedNumbers = tableViewController.getSelectedNumbers();
            Collections.sort(selectedNumbers);
            String sMaxLengthForComb = mBinding.edtCombLength.getText().toString();

            if (selectedNumbers.isEmpty()) {
                Toast.makeText(this, "Input numbers", Toast.LENGTH_SHORT).show();
            } else if (sMaxLengthForComb.isEmpty()) {
                mBinding.edtCombLength.setError("Enter length");
            } else {
                int maxLength = Integer.parseInt(sMaxLengthForComb);
                if (maxLength > selectedNumbers.size()) {
                    Toast.makeText(this, "combination length can't be higher than the provided numbers", Toast.LENGTH_LONG).show();
                } else {
                    boolean containsDuplicates = false;
                    for (int i = 1; i < selectedNumbers.size(); i++) {
                        if (selectedNumbers.get(i).equals(selectedNumbers.get(i - 1))) {
                            containsDuplicates = true;
                            Toast.makeText(this, "Error, there's a duplicate of " + selectedNumbers.get(i), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if (!containsDuplicates) {
                        generateCombinations(selectedNumbers, maxLength);
                    }
                }
            }
        });
    }

    private void populateListView(List<int[]> listOfCombinations) {
        mBinding.tvCombinations.setText(String.format(Locale.US, "Total Combinations (%d)", listOfCombinations.size()));
        setScreenAsEnabled(true);
        combinationsList.clear();

        combinationsList.addAll(listOfCombinations);
        if (combinationsAdapter == null) {
            combinationsAdapter = new CombinationsAdapter(combinationsList, getResources().getDimension(R.dimen.col_width));
            mBinding.rvCombHolder.setAdapter(combinationsAdapter);
        } else {
            combinationsAdapter.notifyDataSetChanged();
        }
    }



    private void generateCombinations(List<Integer> selectedNumbers, int maxLength) {
        setScreenAsEnabled(false);
        new GenerateComboTask(selectedNumbers, selectedNumbers.size(), maxLength, this::populateListView).execute();
        //mViewModel.generateCombinations(selectedNumbers, maxLength);
    }

    private void setScreenAsEnabled(boolean enabled) {
        if (enabled) {
            mBinding.lpiCalculation.hide();
            /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
        } else {
            mBinding.lpiCalculation.show();
            /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
        }
    }

    public void configLinearProgress(LinearProgressIndicator progressIndicator) {
        progressIndicator.setIndicatorColor(getColorById(R.color.brown),
                getColorById(R.color.light_brown), getColorById(R.color.black));

        progressIndicator.setIndeterminateAnimationType(LinearProgressIndicator
                .INDETERMINATE_ANIMATION_TYPE_CONTIGUOUS);

        progressIndicator.hide();
    }

    private int getColorById(int id) {
        return ContextCompat.getColor(this, id);
    }
}