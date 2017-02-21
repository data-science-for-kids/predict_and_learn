package com.example.user.datascienceapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class PredictActivity extends AppCompatActivity {
    private WebView webView;
    private int[] res;
    private int m,f;
    private ArrayList<Response> responses;
    private ProgressBar progressBar;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_predict);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);
        webView= (WebView) findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        responses= (ArrayList<Response>) getIntent().getSerializableExtra("Response");
        progressBar= (ProgressBar) findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);
        res=new int[5];
        res[0]=0;
        res[1]=0;
        res[2]=0;
        res[3]=0;
        m=0;
        f=0;

       // String url="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/chart.html?alt=media&token=12afb374-34d4-43bf-97d2-b5269e8c341b";
        String url="file:///android_asset/chart.html";
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        final Context context = this;

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Float","Pressed");
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_infrence);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                Button predict = (Button) dialog.findViewById(R.id.predictDialogButton);
                predict.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getBaseContext(),"Build Your Predictor",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        for(Response response: responses){
            String r = response.getResponse();
            if(r.equals("5.0")){
                res[4]++;
                String gender=response.getGender();
                if(gender.equals("MALE")){
                    m++;
                }
                else{
                    f++;
                }
            }
            else if(r.equals("4.0")){
                res[3]++;
                String gender=response.getGender();
                if(gender.equals("MALE")){
                    m++;
                }
                else{
                    f++;
                }
            }
            else if(r.equals("3.0")){
                res[2]++;
            }
            else if(r.equals("2.0")){
                res[1]++;
            }
            else{
                res[0]++;
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
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(int i=0; i<res.length; i++){
                sb.append("\"").append(res[i]).append("\"");
                sb.append(",");

            }
            sb.append("\"").append(30).append("\"");
            sb.append("]");
            Log.d("St",sb.toString());
            return sb.toString();
        }
        @JavascriptInterface
        public String sendGender(){
            StringBuilder sb = new StringBuilder();
            sb.append("[");
                sb.append("\"").append(m).append("\"");
                sb.append(",");
                sb.append("\"").append(f).append("\"");
            sb.append("]");
            Log.d("St-gender",sb.toString());
            return sb.toString();
        }
        @JavascriptInterface
        public void makeToast(String message){
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
        @JavascriptInterface
        public void floatVisible(boolean flag){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            });
        }

    }


}
