package com.puzzle.industries.inumbr.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.puzzle.industries.inumbr.iNumber;

public class ScriptLoader {

    private static final ScriptLoader instance = new ScriptLoader();

    public static ScriptLoader getInstance(){
        return instance;
    }

    public String getScript(String name) throws IOException {
        InputStream scriptStream = iNumber.getAppContext().getAssets().open(name);
        InputStreamReader scriptStreamReader = new InputStreamReader(scriptStream);
        BufferedReader reader = new BufferedReader(scriptStreamReader);

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null){
            stringBuilder.append(line).append('\n');
        }

        reader.close();
        scriptStreamReader.close();
        scriptStream.close();

        return stringBuilder.toString();
    }
}