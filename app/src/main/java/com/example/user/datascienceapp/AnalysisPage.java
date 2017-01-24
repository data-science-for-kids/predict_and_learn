package com.example.user.datascienceapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class AnalysisPage extends AppCompatActivity {
    private WebView webView;
    private ArrayList<Response> responses;
    private int[] res;
    private int m=0,f=0;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis_page);
        progressBar= (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        responses= (ArrayList<Response>) getIntent().getSerializableExtra("Response");
        webView= (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        res=new int[4];
        res[0]=0;
        res[1]=0;
        res[2]=0;
        res[3]=0;
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
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
                    sb.append(",");
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
