package com.puzzle.industries.inumbr.tasks;

import com.puzzle.industries.inumbr.dataModels.CombinationsUtil;
import com.puzzle.industries.inumbr.dataModels.Result;
import com.puzzle.industries.inumbr.iNumber;
import com.puzzle.industries.inumbr.utils.Callback;
import com.puzzle.industries.inumbr.utils.ThreadTask;

import java.util.ArrayList;
import java.util.List;

public class GenerateComboTask extends ThreadTask {

    private final GenerateComboRunnable generateComboRunnable;

    public GenerateComboTask(List<Integer> selectedNumbers, int n, int r, Callback<List<int[]>> callback) {
        this.generateComboRunnable = new GenerateComboRunnable(selectedNumbers, n, r, callback);
    }

    @Override
    public void execute() {
        super.execute();
        execute(generateComboRunnable);
    }

    private static class GenerateComboRunnable implements Runnable {

        private final List<Integer> selectedNumbers;
        private final int n;
        private final int r;
        private final Callback<List<int[]>> callback;

        public GenerateComboRunnable(List<Integer> selectedNumbers, int n, int r, Callback<List<int[]>> callback) {
            this.selectedNumbers = selectedNumbers;
            this.n = n;
            this.r = r;
            this.callback = callback;
        }

        @Override
        public void run() {
            Result<List<int[]>> result = generateCombinations(selectedNumbers, n, r);
            Result.Success<List<int[]>> successResult = (Result.Success<List<int[]>>) result;
            List<int[]> possibleCombinations = successResult.getResultValue();
            iNumber.runOnUiThread(() -> callback.onCallback(possibleCombinations));
        }

        private Result<List<int[]>> generateCombinations(List<Integer> selectedNumbers, int n, int r) {
            CombinationsUtil model = new CombinationsUtil();

            List<int[]> combinations = model.generate(n, r);
            List<int[]> generatedCombinations = new ArrayList<>();

            for (int[] comb : combinations) {
                int[] selectedNumbersComb = new int[comb.length];

                for (int i = 0; i < comb.length; i++) {
                    selectedNumbersComb[i] = selectedNumbers.get(comb[i]);
                }

                generatedCombinations.add(selectedNumbersComb);
            }

            return new Result.Success<>(generatedCombinations);
        }
    }
}