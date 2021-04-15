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

        mBinding.btnSetCredentials.setOnClickListener(v -> {
            CredentialsBottomSheet credentialsBottomSheet = new CredentialsBottomSheet();
            credentialsBottomSheet.show(mFragmentManager, credentialsBottomSheet.getTag());
        });

        mBinding.btnSelectNum.setOnClickListener(v -> {
            if (mInputBottomSheet == null){
                mInputBottomSheet = new InputBottomSheet(SELECTION_BALLS);
                mInputBottomSheet.setBallSelectionListener(this);
            }
            InputBottomSheet inputBottomSheet = new InputBottomSheet(SELECTION_BALLS);
            mInputBottomSheet.show(mFragmentManager, inputBottomSheet.getTag());
        });

        mBinding.btnClearSelection.setOnClickListener(v -> {
            SELECTED_BALLS.clear();
            for (Ball ball : SELECTION_BALLS) ball.setSelected(false);
            SELECTED_BALLS_ADAPTER.notifyDataSetChanged();
            v.setVisibility(View.GONE);
        });

        mBinding.btnGenComb.setOnClickListener(v -> {
            String sNumber = mBinding.edtCombLength.getText().toString().trim();

            if (sNumber.isEmpty()){
                Toast.makeText(v.getContext(), "Enter combination length", Toast.LENGTH_SHORT).show();
            }
            else{
                int combLength = Integer.parseInt(sNumber);
                int numOfSelectedNums = SELECTED_BALLS.size();

                if (combLength > numOfSelectedNums){
                    Toast.makeText(this, "combination length can't be higher than the provided numbers", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "generating combinations", Toast.LENGTH_SHORT).show();
                    generateCombinations(combLength);
                }
            }
        });
    }

    private void displayResults(List<int[]> listOfCombinations) {
        ResultsActivity.sResults = listOfCombinations;
        startActivity(new Intent(MainActivity.this, ResultsActivity.class));
    }

    private void generateCombinations(int maxLength) {
        List<Integer> selectedBalls = new ArrayList<>();
        for (Ball b : SELECTED_BALLS){
            selectedBalls.add(b.getNum());
        }
        new GenerateComboTask(selectedBalls, selectedBalls.size(), maxLength, this::displayResults).execute();
    }

    @Override
    public void ballSelected(Ball ball) {
        int ballPosition = SELECTED_BALLS.indexOf(ball);

        if (ballPosition == -1){
            SELECTED_BALLS.add(ball);
            SELECTED_BALLS_ADAPTER.notifyItemInserted(SELECTED_BALLS.size() - 1);
        }
        else{
            SELECTED_BALLS.remove(ballPosition);
            SELECTED_BALLS_ADAPTER.notifyItemRemoved(ballPosition);
        }

        mBinding.btnClearSelection.setVisibility(SELECTED_BALLS.isEmpty() ? View.GONE : View.VISIBLE);
    }
}