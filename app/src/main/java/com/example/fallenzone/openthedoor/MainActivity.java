package com.example.fallenzone.openthedoor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends /*ActionBarActivity*/ Activity {

    private WebView webView ;
    private TextView textView , txtUrl , txtProgress;
    private String username = "M5B01";
    private String pwd = "c@dla3";
    //private String url = "http://163.25.117.185/OGWeb/LoginForm.aspx";
    private boolean ctrlLock = false ;
    private  boolean stopOrAuto ;

    private ProgressBar progressBar , progressBarCircle;
    private Switch switchWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Button btnAutoLock , btnStopAutoLock , btnOpen;

        setContentView(R.layout.activity_main);
        switchWebView = (Switch) findViewById(R.id.switchWebView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textView = (TextView) findViewById(R.id.textView);
        webView = (WebView) findViewById(R.id.webView);
        txtUrl = (TextView) findViewById(R.id.txtUrl);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        btnAutoLock = (Button) findViewById(R.id.btnAutoLock);
        btnStopAutoLock = (Button) findViewById(R.id.btnStopAutoLock);
        btnOpen = (Button) findViewById(R.id.btnOpen);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);


        //set component invisiable on start
        webView.setVisibility(View.INVISIBLE);
        txtUrl.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);


        final WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearCache(true);


        final Activity activity = this;
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl("http://163.25.117.185/OGWeb/OGWebGuard/OGDOutActionPage.aspx");
        textView.setText("never uesd");
        txtUrl.setText(webView.getUrl());



        webView.clearHistory();
        final WebView webview = new WebView(this);
        WebSettings ws = webview.getSettings();
        ws.setSaveFormData(false);

        webView.setWebViewClient(new WebViewClient());
        final String urlOpen = "http://163.25.117.185/OGWeb/OGWebGuard/OGDOutActionPage.aspx" ;
        final String urlDefault = "http://163.25.117.185/OGWeb/Default.aspx" ;
        final String urlCtrl = "http://163.25.117.185/OGWeb/OGWebGuard/OGDoorLatchActionPage.aspx";
        final String btnOpenID = "OGDOutActionCtrl_DeviceList_ctl00_DeviceBtn_Button";
        final String btnAutoLockOff = "OGDoorLatchActionCtrl_DeviceLatchOffList_ctl00_DeviceOffBtn_Button";
        final String btnAutoLockOn =  "OGDoorLatchActionCtrl_DeviceLatchOnList_ctl00_DeviceOnBtn_Button";
        //Auto lock click event
        final String urlCtrlLock = "http://163.25.117.185/OGWeb/OGWebGuard/OGDoorLatchActionPage.aspx" ;

        switchWebView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
           // webView.setVisibility((switchWebView.isChecked()) ? View.VISIBLE : View.INVISIBLE);
            if (switchWebView.isChecked()){
                webView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                txtUrl.setVisibility(View.VISIBLE);
                switchWebView.setText("");
                progressBar.setVisibility(View.INVISIBLE);
            }else{
                webView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                txtUrl.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                switchWebView.setText("Show Information");
            }
        }
    });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
         if (!clickDirectly(webView,urlOpen,btnOpenID))
             onCtrlClick(urlOpen, false , false);
            }
        }); // end btnStopAutoLock
        btnAutoLock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
           //     if (!clickDirectly(webview, urlCtrl , btnAutoLockOn))
                 onCtrlClick(urlCtrlLock , true , false);
            }
        });// end btnAutoLock

        btnStopAutoLock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //if (!clickDirectly(webview, urlCtrl , btnAutoLockOff))
                    onCtrlClick(urlCtrlLock , true , true);
            }
        }); // end btnStopAutoLock


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                //Log.v("Progress", Integer.toString(progress));
                progressBar.setProgress(progress);
                txtProgress.setText(Integer.toString( progress ));
                if (progress == 100) {
                    txtUrl.setText(webView.getUrl());
                    if (webView.getUrl().equals("http://163.25.117.185/OGWeb/LoginForm.aspx?ReturnUrl=%2fOGWeb%2fOGWebGuard%2fOGDOutActionPage.aspx")) {
                        // Fill acount , password on web view
                        fillAccountAndPassword(view, username, pwd);
                        //click button on the web view
                        clickLogin(view);
                        textView.setText("Logging");
                    }else if (webView.getUrl().equals(urlDefault)) {
                        txtUrl.setText(webView.getUrl());
                        textView.setText("Redirecting");
                        webView.loadUrl( ctrlLock ? urlCtrl : urlOpen);
                    }
                    if (ctrlLock) {
                        if (webView.getUrl().equals(urlCtrl)) {
                            txtUrl.setText(webView.getUrl());
                            textView.setText("in Auto lock Desicion");
                            clickConfirm(view , stopOrAuto ? btnAutoLockOff : btnAutoLockOn);
                            ctrlLock = false;
                            textView.setText("Stop Auto Lock  : " +stopOrAuto );
                        }
                    } else {
                        if (webView.getUrl().equals(urlOpen)) {
                            txtUrl.setText(webView.getUrl());
                            clickConfirm(view , btnOpenID);
                            textView.setText("Clicked Open");
                        }
                    } // end else
                } // end progress = 100
            } // end onProgressedChanged
            @Override
            public boolean onJsAlert(WebView view, final String url, String message,
                                     JsResult result) {

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setMessage(message)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        }).show();
                result.confirm();
                return true;
            }

        });
    }
    public  void onCtrlClick(String url , boolean ctrlLcok ,boolean stopOrAuto ){
        //set 2 progressbar to VISIBLE
        txtProgress.setVisibility(View.VISIBLE);
        progressBarCircle.setVisibility(View.VISIBLE);
        //load to Contrl Auto Lock Web Page
        webView.loadUrl(url);
        // assign click boolean value
        this.ctrlLock = ctrlLcok;
        this.stopOrAuto = stopOrAuto;
    }
    public boolean clickDirectly(WebView view , String clickUrl , String buttonID){
        if (view.getUrl().equals(clickUrl)) {
            clickConfirm(view, buttonID);
            return true;
        }else{
            return false;
        }
    }
    public void fillAccountAndPassword(WebView view , String account , String password){
        view.loadUrl("javascript:var x = document.getElementById('UserAccount').value = '" +
                account + "';");
        view.loadUrl("javascript:var y = document.getElementById('UserPassword').value = '" +
                password + "';");
    }
    public void clickLogin(WebView view){
        view.loadUrl("javascript:(function(){" +
                "l=document.getElementById('LoginBtn');" +
                "e=document.createEvent('HTMLEvents');" +
                "e.initEvent('click',true,true);" +
                "l.dispatchEvent(e);" +
                "})()");
    }
    public void clickConfirm(WebView view, String buttonID){
        txtProgress.setVisibility(View.INVISIBLE);
        progressBarCircle.setVisibility(View.INVISIBLE);
        view.loadUrl("javascript:(function(){" +
                "l=document.getElementById('"+buttonID+"');" +
                "e=document.createEvent('HTMLEvents');" +
                "e.initEvent('click',true,true);" +
                "l.dispatchEvent(e);" +
                "})()");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
