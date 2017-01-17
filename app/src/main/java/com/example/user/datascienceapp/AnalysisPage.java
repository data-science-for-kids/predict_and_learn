package com.example.user.datascienceapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

public class AnalysisPage extends AppCompatActivity {
private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis_page);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Downloading");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        webView= (WebView) findViewById(R.id.webview);
       webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            boolean loadingFinished = true;
            boolean redirect = false;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loadingFinished = false;
                Log.d("here","there");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                if(!redirect){
                    loadingFinished = true;
                }
                if(loadingFinished && !redirect){

                } else{
                    redirect = false;
                }
                super.onPageFinished(view, url);
            }
        });
        webView.addJavascriptInterface(new AppJavaProxy(this),"androidAppProxy");
        //webView.loadUrl("http://www.google.com");
        String url="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/chart3.html?alt=media&token=5fa8e6c8-f965-461a-a8fe-0154fa3eb61a";
        String local="file:///android_asset/chart.html";
        webView.loadUrl(url);
    }
    public class AppJavaProxy{
        private Activity activity=null;
        public AppJavaProxy(Activity activity){
            this.activity=activity;
        }
        @JavascriptInterface
        public void showMessage(String message){
            Toast.makeText(this.activity.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public String sendNo(){
                // for testing only

            ArrayList<Integer> arr=new ArrayList<Integer>();
            arr.add(1);
            arr.add(2);
            arr.add(3);
            arr.add(4);
            arr.add(5);
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for(int i=0; i<arr.size(); i++){
                sb.append("\"").append(arr.get(i)).append("\"");
                if(i+1 < arr.size()){
                    sb.append(",");
                }
            }
            sb.append("]");
           // Log.d("St",sb.toString());
            return sb.toString();

        }

    }

}
