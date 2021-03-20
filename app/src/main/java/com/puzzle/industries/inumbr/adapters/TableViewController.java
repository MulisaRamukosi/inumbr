package com.puzzle.industries.inumbr.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;
import com.puzzle.industries.inumbr.R;

import java.util.ArrayList;
import java.util.List;

public class TableViewController {

    private final Context c;
    private final TableLayout tableLayout;
    private final Resources res;

    private final int numOfColumns;
    private final double columnWidth;

    private final int[] colorList = new int[]{R.color.red, R.color.yellow, R.color.green, R.color.color_primary_dark};

    private final List<EditText> NUMBER_INPUTS = new ArrayList<>();
    private final List<TableRow> TABLE_ROW = new ArrayList<>();

    private final InputFilter[] FILTER = new InputFilter[]{new InputFilter.LengthFilter(3)};

    public TableViewController(TableLayout tableLayout, double columnWidth){
        this.tableLayout = tableLayout;
        this.columnWidth = columnWidth;
        this.c = tableLayout.getContext();
        this.res = c.getResources();
        this.numOfColumns = calcNumOfColumns();
    }

    private int calcNumOfColumns() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return (int) (screenWidth/columnWidth);
    }

    public void addNumberInput(){
        TableRow latestRow = getLatestTableRow();
        latestRow.addView(createNumberInputEditText());
        setInputFlow();
    }

    public void addNumber(int number){
        TableRow latestRow = getLatestTableRow();
        EditText editText = createNumberView(number);

        int colorToUse;

        if (number <= 10) colorToUse = colorList[0];
        else if (number <= 20) colorToUse = colorList[1];
        else if (number <= 30) colorToUse = colorList[2];
        else colorToUse = colorList[3];

        Drawable background = ContextCompat.getDrawable(c, R.drawable.bg_circle);
        background.setColorFilter(new PorterDuffColorFilter(getColor(colorToUse), PorterDuff.Mode.SRC_ATOP));

        editText.setBackground(background);


        latestRow.addView(editText);
    }

    private int getColor(int colorId){
        int actualColor = ContextCompat.getColor(c, colorId);
        int r = Color.red(actualColor);
        int g = Color.green(actualColor);
        int b = Color.blue(actualColor);

        return Color.rgb(r, g, b);
    }

    private EditText createNumberView(int number) {
        EditText editText = createBaseEditText();
        editText.setText(String.valueOf(number));
        editText.setFocusable(false);
        return editText;
    }

    private TableRow getLatestTableRow() {
        TableRow row;
        if (TABLE_ROW.isEmpty()) row = createEmptyRow();

        else{
            row = TABLE_ROW.get(TABLE_ROW.size() - 1);

            if (row.getChildCount() == numOfColumns){
                //row is full, create a new one
                row = createEmptyRow();
            }
        }

        return row;
    }

    private TableRow createEmptyRow(){
        int margin = (int) res.getDimension(R.dimen.mp_4dp);

        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, margin, 0, margin);

        TableRow row = new TableRow(c);
        TABLE_ROW.add(row);
        tableLayout.addView(row, params);

        return row;
    }

    private EditText createNumberInputEditText() {
        EditText editText = createBaseEditText();

        initInputBehaviour(editText);
        NUMBER_INPUTS.add(editText);

        return editText;
    }

    private EditText createBaseEditText(){
        EditText editText = new EditText(c);

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setBackground(ContextCompat.getDrawable(c, R.drawable.bg_circle));
        editText.setGravity(Gravity.CENTER);
        editText.setTextAppearance(c, R.style.TextAppearance_Regular);
        editText.setTextColor(ContextCompat.getColor(c, R.color.white));
        editText.setFilters(FILTER);
        editText.setHintTextColor(ContextCompat.getColor(c, android.R.color.darker_gray));
        editText.setHint(String.valueOf(NUMBER_INPUTS.size() + 1));
        editText.setTextSize(res.getDimension(R.dimen.txt_size_medium));

        setDeleteKeyListenerAsEnabled(true, editText);

        int editTextSize = (int) res.getDimension(R.dimen.bg_circle_size);
        int margins = (int) res.getDimension(R.dimen.mp_4dp);

        TableRow.LayoutParams params = new TableRow.LayoutParams(editTextSize, editTextSize);
        params.setMargins(margins, margins, margins, margins);
        editText.setLayoutParams(params);

        return editText;
    }

    private void setInputFlow(){
        if (NUMBER_INPUTS.size() > 1){
            int beforeLastItem = NUMBER_INPUTS.size() - 1;

            for (int i = 0; i < beforeLastItem; i++){
                EditText edtComb = NUMBER_INPUTS.get(i);
                edtComb.setHint(String.valueOf(i + 1));

                if (i + 1 == beforeLastItem) NUMBER_INPUTS.get(i + 1).setHint(String.valueOf(i + 2));

                int finalI = i;
                edtComb.setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_NEXT){
                        giveEditTextFocus(finalI + 1);
                    }
                    return false;
                });
            }
        }
        else if (NUMBER_INPUTS.size() == 1){
            EditText edtComb = NUMBER_INPUTS.get(0);
            edtComb.setHint(String.valueOf(1));
        }
    }

    private void giveEditTextFocus(int pos){
        NUMBER_INPUTS.get(pos).requestFocus();
    }

    public List<Integer> getSelectedNumbers() {
        List<Integer> selectedNumbers = new ArrayList<>();

        for (EditText edtCombIn : NUMBER_INPUTS){
            String in = edtCombIn.getText().toString();
            if (!in.isEmpty()){
                selectedNumbers.add(Integer.parseInt(in));
            }
        }

        return selectedNumbers;
    }

    /*
     * inits editText text watcher
     * to monitor the input
     * */
    private void initInputBehaviour(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*
                 * The moment the editText is empty, the delete key listener is enabled
                 * in case the users presses delete again
                 * we first make a delay
                 * */
                boolean isNowEmpty = s.toString().trim().isEmpty();

                if (isNowEmpty){
                    new Handler().postDelayed(() ->
                            setDeleteKeyListenerAsEnabled(s.toString().trim().isEmpty(), editText),
                            100);
                }
                else{
                    setDeleteKeyListenerAsEnabled(false, editText);
                }

            }
        });
    }

    /*
     * enables the listener for delete key
     * when delete key is pressed (when the listener is enabled), the editText is removed
     * and focus is given to the previous item
     * */
    private void setDeleteKeyListenerAsEnabled(boolean enabled, EditText editText){
        if (enabled){
            editText.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    removeEditTextFromTable(editText);
                }
                return false;
            });
        }
        else editText.setOnKeyListener(null);
    }

    private void removeEditTextFromTable(EditText editText){
        editText.clearFocus();
        int itemPos = NUMBER_INPUTS.indexOf(editText);
        NUMBER_INPUTS.remove(editText);

        removeEditTextFromRow(editText);

        setInputFlow();

        if (itemPos - 1 >= 0) giveEditTextFocus(itemPos - 1);
    }

    private void removeEditTextFromRow(EditText editText) {
        boolean targetFound = false;

        for (int i = 0; i < TABLE_ROW.size(); i++){
            TableRow row = TABLE_ROW.get(i);
            int numOfChildrenInRow = row.getChildCount();

            for (int j = 0; j < numOfChildrenInRow; j++){
                EditText editTextInRow = (EditText) row.getChildAt(j);

                if (editTextInRow.equals(editText)){ //found the editText to be deleted
                    targetFound = true;
                    row.removeView(editTextInRow);
                    executeRowShifting(row, i);
                    break;
                }
            }

            if (targetFound){
                break;
            }
        }
    }

    private void executeRowShifting(TableRow row, int rowPosition) {
        if (row.getChildCount() == 0){
            TABLE_ROW.remove(row);
        }
        else if (rowPosition + 1 < TABLE_ROW.size()){
            TableRow nextRow = TABLE_ROW.get(rowPosition + 1);

            if (nextRow.getChildCount() >= 1){
                EditText editTextInNextRow = (EditText) nextRow.getChildAt(0);
                nextRow.removeView(editTextInNextRow);
                row.addView(editTextInNextRow);
            }

            executeRowShifting(nextRow, rowPosition + 1);
        }
    }

    public void removeAllViews() {
        tableLayout.removeAllViews();
        TABLE_ROW.clear();
        NUMBER_INPUTS.clear();
    }
}
