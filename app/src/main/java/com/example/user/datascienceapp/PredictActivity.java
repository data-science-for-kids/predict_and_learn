package com.example.user.datascienceapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class PredictActivity extends AppCompatActivity {
    private WebView webView;
    private int[] res;
    private ArrayList<Response> responses;
    private ProgressBar progressBar;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView= (WebView) findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        responses= (ArrayList<Response>) getIntent().getSerializableExtra("Response");
        progressBar= (ProgressBar) findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);
        res=new int[4];
        res[0]=0;
        res[1]=0;
        res[2]=0;
        res[3]=0;
      String url="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/predict.html?alt=media&token=0a6236ef-8f07-4bc1-8431-fbb31582a674";
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

        for(Response response: responses){
            String r = response.getResponse();
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
//                int percent=(res[i]*100)/56;
//                sb.append("\"").append(percent).append("\"");
                //sb.append(",");
            }
            sb.append("\"").append(30).append("\"");
            sb.append("]");
            Log.d("St",sb.toString());
            return sb.toString();
        }
    }


}
