package com.allandroidprojects.payeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class registerUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        WebView mWebView = (WebView) findViewById(R.id.webView);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //loads the url
        mWebView.loadUrl("http://payeat.store/register.php");

        //cancels all hyperlinks
        mWebView.setWebViewClient(new WebViewClient() {
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          if (url.equals("http://payeat.store/index.html")) {
                                              //Do your thing
                                              startActivity(new Intent(registerUser.this, login.class));
                                              return true;
                                          } else {
                                              return false;
                                          }
                                      }
                                  }
        );
}

}
