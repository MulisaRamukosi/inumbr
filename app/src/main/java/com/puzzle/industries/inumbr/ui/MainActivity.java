package com.puzzle.industries.inumbr.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.puzzle.industries.inumbr.R;
import com.puzzle.industries.inumbr.adapters.TableViewController;
import com.puzzle.industries.inumbr.databinding.ActivityMainBinding;
import com.puzzle.industries.inumbr.databinding.ItemCombinationsBinding;
import com.puzzle.industries.inumbr.viewModels.MainActivityViewModel;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private MainActivityViewModel mViewModel;
    private TableViewController tableViewController;
    private Resources res;

    private double ballWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        res = getResources();
        ballWidth = res.getDimension(R.dimen.col_width);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.getCombinations().observe(this, this::displayTheCombinations);

        tableViewController = new TableViewController(mBinding.tlNumberInputs, ballWidth);

        mBinding.ibtnAddNumber.setOnClickListener(v -> tableViewController.addNumberInput());

        mBinding.btnClear.setOnClickListener(v -> {
            mBinding.edtCombLength.setText("");
            mBinding.llCombHolder.removeAllViews();
            tableViewController.removeAllViews();
        });

        mBinding.btnCombGen.setOnClickListener(v -> {
            List<Integer> selectedNumbers = tableViewController.getSelectedNumbers();
            Collections.sort(selectedNumbers);
            String sMaxLengthForComb = mBinding.edtCombLength.getText().toString();

            if (selectedNumbers.isEmpty()){
                Toast.makeText(this, "Input numbers", Toast.LENGTH_SHORT).show();
            }
            else if (sMaxLengthForComb.isEmpty()){
                mBinding.edtCombLength.setError("Enter length");
            }
            else{
                int maxLength = Integer.parseInt(sMaxLengthForComb);
                if (maxLength > selectedNumbers.size()){
                    Toast.makeText(this, "combination length can't be higher than the provided numbers",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    boolean containsDuplicates = false;

                    for (int i = 1; i < selectedNumbers.size(); i++){
                        if (selectedNumbers.get(i).equals(selectedNumbers.get(i - 1))){
                            containsDuplicates = true;
                            Toast.makeText(this, "Error, there's a duplicate of " + selectedNumbers.get(i),
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (!containsDuplicates){
                        generateCombinations(selectedNumbers, maxLength);
                    }
                }
            }
        });
    }

    private void displayTheCombinations(List<int[]> listOfCombinations) {
        mBinding.llCombHolder.removeAllViews();
        int margin = (int) res.getDimension(R.dimen.mp_8dp);

        for (int[] comb : listOfCombinations){

            ItemCombinationsBinding combBinding = ItemCombinationsBinding.inflate(getLayoutInflater());
            TableViewController controller = new TableViewController(combBinding.tlResultItem, ballWidth);

            for (int i : comb) controller.addNumber(i);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, margin, 0, margin);

            mBinding.llCombHolder.addView(combBinding.getRoot(), params);
        }
    }

    private void generateCombinations(List<Integer> selectedNumbers, int maxLength) {
        mViewModel.generateCombinations(selectedNumbers, maxLength);
    }

}