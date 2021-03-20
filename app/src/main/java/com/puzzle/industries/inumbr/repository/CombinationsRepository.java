package com.puzzle.industries.inumbr.repository;

import com.puzzle.industries.inumbr.dataModels.CombinationsModel;

import java.util.List;

public class CombinationsRepository {

    private final CombinationsModel COMBINATIONS_MODEL;

    private static CombinationsRepository instance;

    public static CombinationsRepository getInstance(){
        if (instance == null) instance = new CombinationsRepository();

        return instance;
    }

    private CombinationsRepository(){
        COMBINATIONS_MODEL = new CombinationsModel();
    }

    public List<int[]> generateCombinations(int n, int r){
        return COMBINATIONS_MODEL.generate(n, r);
    }
}
