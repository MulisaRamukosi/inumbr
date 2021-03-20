package com.puzzle.industries.inumbr.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.puzzle.industries.inumbr.repository.CombinationsRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final CombinationsRepository COMBINATIONS_REPO = CombinationsRepository.getInstance();
    private MutableLiveData<List<int[]>> combinations;

    public LiveData<List<int[]>> getCombinations() {
        if (combinations == null) combinations = new MutableLiveData<>();

        return combinations;
    }

    public void generateCombinations(List<Integer> selectedNumbers, int maxLength){
        List<int[]> possibleCombinations = COMBINATIONS_REPO.generateCombinations(selectedNumbers.size(),
                maxLength);

        List<int[]> generatedCombinations = new ArrayList<>();

        for (int[] comb : possibleCombinations){
            int[] selectedNumbersComb = new int[comb.length];

            for (int i = 0; i < comb.length; i++){
                selectedNumbersComb[i] = selectedNumbers.get(comb[i]);
            }

            generatedCombinations.add(selectedNumbersComb);
        }

        combinations.postValue(generatedCombinations);
    }
}
