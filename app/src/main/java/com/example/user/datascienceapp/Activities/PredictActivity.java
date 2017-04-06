package com.example.user.datascienceapp.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.datascienceapp.R;
import com.example.user.datascienceapp.Wrappers.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PredictActivity extends AppCompatActivity {
    private WebView webView;
    private int[] res;
    private int m,f,new_name,old_name,indoor,outdoor,page;
    private ArrayList<Response> responses;
    private ProgressBar progressBar;
    private ImageButton imageButton;
    private String dialog_text="";
    private  Button predict;
    private  Dialog dialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.loadUrl("javascript:getSession()");
    }

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
        imageButton = (ImageButton) findViewById(R.id.fab);
        imageButton.setVisibility(View.GONE);
        webView= (WebView) findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        responses= (ArrayList<Response>) getIntent().getSerializableExtra("Response");
        progressBar= (ProgressBar) findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);
        res=new int[5];
        res[0] = 0;
        res[1] = 0;
        res[2] = 0;
        res[3] = 0;
        m = 0;
        f = 0;
        page = getIntent().getIntExtra("Page",1);
        indoor = 0;
        outdoor = 0;
        new_name = 0;
        old_name = 0;

        //String url="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/datasciene.html?alt=media&token=a2205287-88b0-4625-8463-e2206694ad6b";
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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Float","Pressed");

                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_inference);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);

                TextView textView= (TextView) dialog.findViewById(R.id.dialog_text);
                textView.setText(dialog_text);
                predict = (Button) dialog.findViewById(R.id.predictDialogButton);
                // predict.setVisibility(View.INVISIBLE);
                predict.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPredictor();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        inference();
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

    public void openPredictor(){
        Toast.makeText(getBaseContext(),"Build Your Predictor",Toast.LENGTH_LONG).show();

        Intent intent= new Intent(PredictActivity.this,BuildPredictorActivity.class);
        intent.putExtra("Response",responses);
        startActivity(intent);
        finish();
    }
    public void inference(){
        int c=0;
        for(Response response: responses){
            c++;
            if(c==43)
                break;
            String r = response.getResponse();
            if(r.equals("5.0")){
                res[4]++;
                String gender=response.getGender();
                if(gender.equals("MALE"))
                    m++;
                else
                    f++;
                int age=response.getNameAge();
                if(age == 0)
                    old_name++;
                else
                    new_name++;
                int act=response.getActivity();
                if(act == 0)
                    indoor++;
                else
                    outdoor++;
            }
            else if(r.equals("4.0")){
                res[3]++;
                String gender=response.getGender();
                if(gender.equals("MALE"))
                    m++;
                else
                    f++;
                int age=response.getNameAge();
                if(age == 0)
                    old_name++;
                else
                    new_name++;
                int act=response.getActivity();
                if(act == 0)
                    indoor++;
                else
                    outdoor++;
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
    }

    public class AppJavaProxy{
        private Activity activity=null;
        private int i;
        public AppJavaProxy(Activity activity){
            this.activity=activity;
        }
        @JavascriptInterface
        public void showMessage(String message){
            Toast.makeText(this.activity.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void getPage(int i){

            Log.d("Value of i",i+"");

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String uid="";
            if (mAuth.getCurrentUser() != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                uid = user.getUid();
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference response = database.getReference("session").child(uid).child("page");
            response.setValue(i);
        }
        @JavascriptInterface
        public void dialogText(String message){
            dialog_text=message;
        }
        @JavascriptInterface
        public int setPage(){
            Log.d("Page is ",page+"");
            return page;
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
        public String sendNameAge(){
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("\"").append(old_name).append("\"");
            sb.append(",");
            sb.append("\"").append(new_name).append("\"");
            sb.append("]");
            Log.d("St-nameage",sb.toString());
            return sb.toString();
        }
        @JavascriptInterface
        public String sendActivity(){
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("\"").append(indoor).append("\"");
            sb.append(",");
            sb.append("\"").append(outdoor).append("\"");
            sb.append("]");
            Log.d("St-activity",sb.toString());
            return sb.toString();
        }
        @JavascriptInterface
        public void makeToast(String message){
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void predictOpen(){
            Log.d("Open","Activity");
            openPredictor();
        }
        @JavascriptInterface
        public void floatVisible(boolean flag) {
            if (flag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setVisibility(View.VISIBLE);
                    }
                });
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setVisibility(View.GONE);
                    }
                });
            }
            
        }

    }


}
