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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mFragmentManager = getSupportFragmentManager();

        setContentView(mBinding.getRoot());

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
}