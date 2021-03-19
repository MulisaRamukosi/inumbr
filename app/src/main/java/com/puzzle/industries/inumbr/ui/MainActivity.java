package com.puzzle.industries.inumbr.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.puzzle.industries.inumbr.R;
import com.puzzle.industries.inumbr.databinding.ActivityMainBinding;
import com.puzzle.industries.inumbr.viewModels.MainActivityViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private MainActivityViewModel mViewModel;
    private EditText[] mNumInputList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mNumInputList = new EditText[]{
                mBinding.edtCombIn1,
                mBinding.edtCombIn2,
                mBinding.edtCombIn3,
                mBinding.edtCombIn4,
                mBinding.edtCombIn5,
                mBinding.edtCombIn6
        };

        setInputFlow();

        mViewModel.getCombinations().observe(this, this::displayTheCombinations);

        mBinding.btnCombGen.setOnClickListener(v -> {
            List<Integer> selectedNumbers = getSelectedNumbers();
            Collections.sort(selectedNumbers);
            String sMaxLengthForComb = mBinding.edtCombLength.getText().toString();

            if (selectedNumbers.isEmpty()){
                Toast.makeText(this, "Insert numbers", Toast.LENGTH_SHORT).show();
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
                            break;
                        }
                    }

                    if (containsDuplicates){
                        Toast.makeText(this, "selected numbers contain duplicates!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        generateCombinations(selectedNumbers, maxLength);
                    }
                }
            }
        });
    }

    private void displayTheCombinations(List<String> listOfCombinations) {
        mBinding.llCombHolder.removeAllViews();

        for (String comb : listOfCombinations){
            TextView txtComb = new TextView(this);
            txtComb.setText(comb);
            txtComb.setTextSize(getResources().getDimension(R.dimen.txt_medium));
            txtComb.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);

            mBinding.llCombHolder.addView(txtComb, params);
        }
    }

    private void generateCombinations(List<Integer> selectedNumbers, int maxLength) {
        mViewModel.generateCombinations(selectedNumbers, maxLength);
    }

    private List<Integer> getSelectedNumbers() {
        List<Integer> selectedNumbers = new ArrayList<>();

        for (EditText edtCombIn : mNumInputList){
           String in = edtCombIn.getText().toString();
           if (!in.isEmpty()){
               selectedNumbers.add(Integer.parseInt(in));
           }
        }

        return selectedNumbers;
    }

    private void setInputFlow(){
        for (int i = 0; i < 5; i++){
            EditText edtComb = mNumInputList[i];

            int finalI = i;
            edtComb.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    mNumInputList[finalI + 1].requestFocus();
                }
                return false;
            });
        }
    }


}