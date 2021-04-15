package com.puzzle.industries.inumbr.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.puzzle.industries.inumbr.databinding.ActivityPlaceBetsBinding;
import com.puzzle.industries.inumbr.repositores.CredentialsRepository;
import com.puzzle.industries.inumbr.utils.Constants;
import com.puzzle.industries.inumbr.utils.ScriptLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlaceBetsActivity extends Activity {

    public static List<int[]> sResults;
    private final String TAG = "PLACE_BETS_ACTIVITY";

    private final ScriptLoader SCRIPT_LOADER = ScriptLoader.getInstance();
    private final CredentialsRepository CRED_REPO = CredentialsRepository.getInstance();

    private ActivityPlaceBetsBinding mBinding;
    private WebView mWeb;

    private final AtomicInteger STATE_TRACK = new AtomicInteger(0);

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityPlaceBetsBinding.inflate(getLayoutInflater());
        mWeb = mBinding.wvSite;

        setContentView(mBinding.getRoot());

        //in case if the app unexpectedly closed
        clearWebCache();

        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setBlockNetworkLoads(false);
        mWeb.getSettings().setBlockNetworkImage(false);
        mWeb.getSettings().setDomStorageEnabled(true);
        mWeb.setWebViewClient(new WebViewClient());
        mWeb.setWebChromeClient(new WebChromeClient());

        mWeb.loadUrl(Constants.BETWAY_URL);
    }

    @Override
    public void onBackPressed() {
        if (!mWeb.getUrl().equals(Constants.BETWAY_LUCKY_NUMBERS_URL)){
            mWeb.loadUrl(Constants.BETWAY_LUCKY_NUMBERS_URL);
        }
        else{
            super.onBackPressed();
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            final int state = STATE_TRACK.intValue();

            switch (state){
                case 0: loginUser(); break;
                case 1: selectLuckyNumbers(); break;
                case 2: switchFrames(); break;
                case 3: placeAllBets(); break;
            }

            STATE_TRACK.incrementAndGet();
        }
    }

    private String getStringifyList(){
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < sResults.size(); i++){
            if (i != 0) sb.append(',');
            sb.append(Arrays.toString(sResults.get(i)));
        }
        sb.append(']');
        return sb.toString();
    }

    private void clearWebCache() {
        WebStorage.getInstance().deleteAllData();

        // Clear all the cookies
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        }

        mWeb.clearCache(true);
        mWeb.clearFormData();
        mWeb.clearSslPreferences();
    }

    private void loginUser(){
        try {
            String loginScript = SCRIPT_LOADER.getScript("login.js");
            String phone = CRED_REPO.getPhone();
            String password = CRED_REPO.getPassword();

            loginScript = String.format(loginScript, phone, password);

            mWeb.evaluateJavascript(loginScript, value -> Toast.makeText(mWeb.getContext(), "Attempting to login, Please wait", Toast.LENGTH_LONG).show());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mWeb.getContext(), "Error occurred with the login script", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectLuckyNumbers() {
        Toast.makeText(mWeb.getContext(), "Selecting Lucky numbers", Toast.LENGTH_SHORT).show();
        try{
            String selectLuckyNumbersScript = SCRIPT_LOADER.getScript("selectLuckyNumbers.js");
            mWeb.evaluateJavascript(selectLuckyNumbersScript, value -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * create reference to iFrame, then switch to the iFrame by loading the iFrame src
    * */
    private void switchFrames() {
        try{
            String switchFrameScript = SCRIPT_LOADER.getScript("frameSwitcher.js");
            mWeb.evaluateJavascript(switchFrameScript, frameSrc -> {
                frameSrc = frameSrc.replace("\"", "");
                mWeb.loadUrl(frameSrc);
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void placeAllBets() {
        try{
            String betNumsSet = getStringifyList();
            String betPlaceScript = SCRIPT_LOADER.getScript("betPlacer.js");
            betPlaceScript = String.format(betPlaceScript, betNumsSet);

            Log.d(TAG, betPlaceScript);
            mWeb.evaluateJavascript(betPlaceScript, value -> {});
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
