package com.puzzle.industries.inumbr.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.Toast;

import com.puzzle.industries.inumbr.repositores.CredentialsRepository;
import com.puzzle.industries.inumbr.utils.Constants;
import com.puzzle.industries.inumbr.utils.ScriptLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BetPlacerActivity extends Activity {

    private final AtomicInteger STATE_TRACK = new AtomicInteger(0);
    private final ScriptLoader SCRIPT_LOADER = ScriptLoader.getInstance();
    private final CredentialsRepository CRED_REPO = CredentialsRepository.getInstance();
    private WebView mWeb;


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(WebView webView){
        this.mWeb = webView;
        //in case if the app unexpectedly closed
        clearWebCache();

        this.mWeb.getSettings().setJavaScriptEnabled(true);
        this.mWeb.getSettings().setBlockNetworkLoads(false);
        this.mWeb.getSettings().setBlockNetworkImage(false);
        this.mWeb.getSettings().setDomStorageEnabled(true);
        this.mWeb.setWebViewClient(new WebViewClient());
        this.mWeb.setWebChromeClient(new WebChromeClient());
    }

    public void startBetting(WebView webView){
        initWebView(webView);
        this.mWeb.loadUrl(Constants.BETWAY_URL);
    }
    
    private void clearWebCache() {
        WebStorage.getInstance().deleteAllData();

        // Clear all the cookies
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        }

        this.mWeb.clearCache(true);
        this.mWeb.clearFormData();
        this.mWeb.clearSslPreferences();
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

    @Override
    public void onBackPressed() {
        if (!mWeb.getUrl().equals(Constants.BETWAY_LUCKY_NUMBERS_URL)){
            mWeb.loadUrl(Constants.BETWAY_LUCKY_NUMBERS_URL);
        }
        else{
            super.onBackPressed();
        }
    }

    public String getStringifiedList(List<int[]> results){
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < results.size(); i++){
            if (i != 0) sb.append(',');
            sb.append(Arrays.toString(results.get(i)));
        }
        sb.append(']');
        return sb.toString();
    }

    public void evaluateScript(String script){
        this.mWeb.evaluateJavascript(script, value -> {});
    }

    public String loadScript(String scriptName) throws IOException {
        return SCRIPT_LOADER.getScript(scriptName);
    }

    public abstract void placeBets();

    private class WebViewClient extends android.webkit.WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            final int state = STATE_TRACK.intValue();

            switch (state){
                case 0: loginUser(); break;
                case 1: selectLuckyNumbers(); break;
                case 2: switchFrames(); break;
                case 3: placeBets(); break;
            }

            STATE_TRACK.incrementAndGet();

        }
    }
}
