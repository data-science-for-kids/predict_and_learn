package com.example.user.datascienceapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.datascienceapp.R;
import com.example.user.datascienceapp.Wrappers.DataBean;
import com.example.user.datascienceapp.Wrappers.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

// This activity is used to handle

public class BuildPredictorActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private int[] res;
    private int m,f,new_name,old_name,indoor,outdoor,page;
    private ArrayList<Response> responses;
    private ArrayList<DataBean> cards;
    int pp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        setContentView(R.layout.activity_build_predictor2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);

        res=new int[5];
        res[0] = 0;
        res[1] = 0;
        res[2] = 0;
        res[3] = 0;
        m = 0;
        f = 0;
        indoor = 0;
        outdoor = 0;
        new_name = 0;
        old_name = 0;

        //deserialize the shared preferences to receive the card list in arrayList
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("cardList", "");
        Type type = new TypeToken<ArrayList<DataBean>>(){}.getType();
        cards= gson.fromJson(json, type);

        Log.d("Cards Received",cards.size()+" ");

        webView.getSettings().setJavaScriptEnabled(true);
        responses= (ArrayList<Response>) getIntent().getSerializableExtra("Response");
        String url="file:///android_asset/predict.html";

        inference();

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
    public void inference(){
        int c=0;
        for(Response response: responses){
            c++;
            if(c<=8)
                continue;
            if(c==41)
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
        public AppJavaProxy(Activity activity){
            this.activity=activity;
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
        public String sendInference(){
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("\"").append(m).append("\"");
            sb.append(",");
            sb.append("\"").append(f).append("\"");
            sb.append(",");
            sb.append("\"").append(old_name).append("\"");
            sb.append(",");
            sb.append("\"").append(new_name).append("\"");
            sb.append(",");
            sb.append("\"").append(indoor).append("\"");
            sb.append(",");
            sb.append("\"").append(outdoor).append("\"");

            sb.append("]");
            Log.d("St-infer",sb.toString());
            return sb.toString();
        }
        @JavascriptInterface
        public String getCard(String gender,int nameAge,int activity){
            pp++;
            StringBuilder sb = new StringBuilder();
            for(DataBean db:cards){

                if((nameAge==-1||nameAge==db.getNameAge())&&(activity==-1||activity==db.getActivity())&&(gender.equals("NONE")||gender.equals(db.getGender()))){


                    sb.append("[");
                    sb.append("\"").append(db.getName()).append("\"");
                    sb.append(",");
                    sb.append("\"").append(db.getGender()).append("\"");
                    sb.append(",");
                    sb.append("\"").append(db.getId()).append("\"");
                    sb.append(",");
                    sb.append("\"").append(db.getActivity()).append("\"");
                    sb.append(",");
                    sb.append("\"").append(db.getGame()).append("\"");
                    sb.append(",");
                    sb.append("\"").append(db.getNameAge()).append("\"");

                    sb.append("]");

                    break;
                }
            }
            return sb.toString();
        }

        @JavascriptInterface
        public void makeToast(String message){
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
