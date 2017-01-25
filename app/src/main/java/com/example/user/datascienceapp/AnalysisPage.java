package com.example.user.datascienceapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class AnalysisPage extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analysis_page1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        private WebView webView;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int[] res;
        private ArrayList<Response> responses;
        private ProgressBar progressBar;
        private int m=0;
        private int f=0;
        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_analysis_page1, container, false);
            progressBar= (ProgressBar) rootView.findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);
            webView= (WebView) rootView.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            responses= (ArrayList<Response>) getActivity().getIntent().getSerializableExtra("Response");
            progressBar= (ProgressBar) rootView.findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);
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
            webView.addJavascriptInterface(new PlaceholderFragment.AppJavaProxy(getActivity()),"androidAppProxy");
            String url="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/bar.html?alt=media&token=3597669c-1040-444d-9c41-9ed60830cd1b";

            String url1="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/chart1.html?alt=media&token=a7fef7c2-6b71-4850-bb0b-9e94afb68db6";
            String url2="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/chart2.html?alt=media&token=778c4c70-3306-4d69-9d9e-710bf4173986";
            String url3="https://firebasestorage.googleapis.com/v0/b/datasciencekids-master.appspot.com/o/chart3.html?alt=media&token=5b56d0c3-c198-4761-8840-9a58a685d44f";

            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            int page=getArguments().getInt(ARG_SECTION_NUMBER);
            switch(page){
                case 1:
                    url=url1;
                    break;
                case 2:
                    url=url2;
                    break;
                case 3:
                    url=url3;
                    break;

            }
            webView.loadUrl(url);

            return rootView;
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
                sb.append("\"").append(30).append("\"");
                sb.append(",");
                sb.append("\"").append(26).append("\"");
                sb.append("]");
                Log.d("St",sb.toString());
                return sb.toString();
            }
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FRIEND";
                case 1:
                    return "GENDER";
                case 2:
                    return "PLAY";
            }
            return null;
        }
    }
}
