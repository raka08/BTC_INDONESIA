package net.sindonesia.xp.exchangepayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    WebView myWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getBoolean("Login", false) == true) {
            Intent intent = new Intent(Login.this, Utama.class);
            startActivity(intent);
        }

            myWebView = (WebView) findViewById(R.id.webview);
            myWebView.setWebViewClient(new myWebClient());
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setVerticalScrollBarEnabled(true);
            myWebView.setHorizontalScrollBarEnabled(true);
            myWebView.addJavascriptInterface(new raka(), "okedata");
            myWebView.loadUrl("https://vip.bitcoin.co.id");


        }

        public class myWebClient extends WebViewClient {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (Objects.equals(url, "https://vip.bitcoin.co.id/dashboard")) {
                    myWebView.loadUrl("https://vip.bitcoin.co.id/trade_api");
                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (Objects.equals(url, "https://vip.bitcoin.co.id/trade_api")) {
                    view.loadUrl("javascript:window.okedata.getthisdata('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                }
                Login.this.setTitle(view.getTitle());

                Toolbar title = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(title);
                getSupportActionBar().setTitle(view.getTitle());


            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                view.loadUrl(url);
                return true;

            }
        }

        class raka {
            @JavascriptInterface
            @SuppressWarnings("unused")

            public void getthisdata(String data) {

                Pattern pattern = Pattern.compile("API Key: (.*)<br>");
                Pattern patternini = Pattern.compile("Secret Key: (.*)</div>");
                Pattern nama = Pattern.compile("<h4 class=\"media-heading mt0\">(.*)</h4>");
                Matcher matcher = pattern.matcher(data);
                Matcher matcherini = patternini.matcher(data);
                Matcher nama1 = nama.matcher(data);


                if (matcherini.find() && matcher.find() && nama1.find()) {
                    Log.d("test", nama1.group(1));

                    Context context = getApplicationContext();
                    CharSequence text = "API Key: " + matcher.group(1) + "Secret: " + matcherini.group(1);
                    int duration = Toast.LENGTH_SHORT;


                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Key", matcher.group(1));
                    editor.putString("Secret", matcherini.group(1));
                    editor.putString("Nama", nama1.group(1));
                    editor.putBoolean("Login", true);
                    editor.commit();
                    Intent intent = new Intent(Login.this, Utama.class);
                    startActivity(intent);

                    //DO the stuff
                }
            }


        }

    }


