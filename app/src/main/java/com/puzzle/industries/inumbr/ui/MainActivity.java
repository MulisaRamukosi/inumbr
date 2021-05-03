package com.puzzle.industries.inumbr.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.puzzle.industries.inumbr.R;
import com.puzzle.industries.inumbr.adapters.LuckyNumberAdapter;
import com.puzzle.industries.inumbr.bottomSheet.CredentialsBottomSheet;
import com.puzzle.industries.inumbr.components.GridItemDecoration;
import com.puzzle.industries.inumbr.dataModels.LuckyNumberGame;
import com.puzzle.industries.inumbr.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity  {

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

        int margins = (int) getResources().getDimension(R.dimen.mp_4dp);

        LuckyNumberGame russianGosLotto749 = new LuckyNumberGame("GOSLOTO RUSSIA 7/49", 49, 6, R.drawable.ic_russia, "RussiaGosLotto749BetPlacer.js");
        LuckyNumberGame southAfricaDailyLotto536 = new LuckyNumberGame("SOUTH AFRICA DAILY LOTTO 5/36", 36, 4, R.drawable.ic_south_africa, "SouthAfricaDailyLotto536BetPlacer.js");

        List<LuckyNumberGame> luckyNumberGames = new ArrayList<>(Arrays.asList(russianGosLotto749, southAfricaDailyLotto536));
        LuckyNumberAdapter luckyNumberAdapter = new LuckyNumberAdapter(luckyNumberGames);
        mBinding.rvLuckyNumberGames.setAdapter(luckyNumberAdapter);
        mBinding.rvLuckyNumberGames.addItemDecoration(new GridItemDecoration(margins));
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