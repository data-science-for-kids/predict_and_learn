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
import android.util.StringBuilderPrinter;
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
    private ArrayList<Response> responses;
    private int[] res;
    private int m=0,f=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis_page);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        responses= (ArrayList<Response>) getIntent().getSerializableExtra("Response");

        webView= (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        res=new int[4];
        res[0]=0;
        res[1]=0;
        res[2]=0;
        res[3]=0;

        for(Response response: responses){
            String r=response.getResponse();
            if(r.equals("5.0")){
                res[3]++;
            }
            else if(r.equals("4.0")){
                res[2]++;
            }
            else if(r.equals("3.0")){
                res[1]++;
            }
            else{
                res[0]++;
            }

            String g=response.getGender();
            if(g.equals("MALE")){
                m++;
            }
            else if(g.equals("FEMALE")){
                f++;
            }

        }

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
        String url="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/bar.html?alt=media&token=3597669c-1040-444d-9c41-9ed60830cd1b";
        String local="file:///android_asset/bar.html";
        webView.getSettings().setBuiltInZoomControls(true);
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

            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for(int i=0; i<res.length; i++){
                sb.append("\"").append(res[i]).append("\"");
                sb.append(",");
                int percent=(res[i]*100)/56;
                sb.append("\"").append(percent).append("\"");
                //if(i+1 < res.length) {
                    sb.append(",");
                //}
            }
            sb.append("\"").append(m).append("\"");
            sb.append(",");
            sb.append("\"").append(f).append("\"");
            sb.append("]");
           Log.d("St",sb.toString());
            return sb.toString();
        }
    }
}
