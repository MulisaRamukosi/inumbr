package com.puzzle.industries.inumbr.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.puzzle.industries.inumbr.R;
import com.puzzle.industries.inumbr.adapters.CombinationsAdapter;
import com.puzzle.industries.inumbr.adapters.TableViewController;
import com.puzzle.industries.inumbr.databinding.ActivityMainBinding;
import com.puzzle.industries.inumbr.tasks.GenerateComboTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    //private MainActivityViewModel mViewModel;
    private TableViewController tableViewController;

    private final List<int[]> combinationsList = new ArrayList<>();

    private CombinationsAdapter combinationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        configLinearProgress(mBinding.lpiCalculation);

        double ballWidth = getResources().getDimension(R.dimen.col_width);
        tableViewController = new TableViewController(mBinding.tlNumberInputs, ballWidth);

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