package com.puzzle.industries.inumbr.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.puzzle.industries.inumbr.repository.CombinationsRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final CombinationsRepository COMBINATIONS_REPO = CombinationsRepository.getInstance();
    private MutableLiveData<List<String>> combinations;

    public LiveData<List<String>> getCombinations() {
        if (combinations == null) combinations = new MutableLiveData<>();

        return combinations;
    }

    public void generateCombinations(List<Integer> selectedNumbers, int maxLength){
        List<int[]> possibleCombinations = COMBINATIONS_REPO.generateCombinations(selectedNumbers.size(),
                maxLength);

        List<String> generatedCombinations = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (int[] comb : possibleCombinations){
            for (int i = 0; i < comb.length; i++){
                if (i != 0) sb.append(' ');
                sb.append(selectedNumbers.get(comb[i]));
            }

            generatedCombinations.add(sb.toString());
            sb = new StringBuilder();
        }

        combinations.postValue(generatedCombinations);
    }
}
