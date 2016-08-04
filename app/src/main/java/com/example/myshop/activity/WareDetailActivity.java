package com.example.myshop.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.bean.Wares;
import com.example.myshop.utils.CartProvider;
import com.example.myshop.utils.ToastUtils;
import com.example.myshop.widget.MyToolbar;

import java.io.Serializable;

public class WareDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private MyToolbar mToolbar;
    private WebView webView;

    private CartProvider mCartProvider;
    private WebInterface mAppInterface;
    private Wares ware;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waredetail);

        getIntentData();
        initView();
        initEvent();

    }

    /**
     * 获取Intent传递过来的值
     * */
    private void getIntentData() {
        Serializable data = getIntent().getSerializableExtra(Contants.WARE);
        if (data == null) {
            this.finish();
        } else {
            ware = (Wares) data;
        }
    }

    private void initView() {

        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webview);

    }

    private void initEvent() {

        mCartProvider = CartProvider.getInstance(this);

        mToolbar.setRightButtonOnClickListener(this);
        mToolbar.getmLeftButton().setOnClickListener(this);

        WebSettings webset = webView.getSettings();
        webset.setJavaScriptEnabled(true);
        webset.setBlockNetworkImage(false);
        webset.setAppCacheEnabled(true);

        webView.loadUrl(Contants.API.WARES_DETAIL);
        mAppInterface = new WebInterface(this);
        webView.addJavascriptInterface(mAppInterface, "appInterface");
        webView.setWebViewClient(new WebClient());
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.toolbar_leftButton:
                onBackPressed();
                break;
            case R.id.toolbar_rightButton:
                ToastUtils.show(this, "分享暂时未开发");
                break;
            default:
                break;
        }
    }

    /**
     * *******************定义内部类WebClient*********************
     * */
    private class WebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mAppInterface.showDetail();
        }
    }

    /**
     * *******************定义内部类WebInterface*********************
     * */
    private class WebInterface {

        private Context mContext;

        public WebInterface(Context mContext) {
            this.mContext = mContext;
        }

        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:showDetail("+ware.getId()+")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id) {


        }

        @JavascriptInterface
        public void addToCart(long id) {
            mCartProvider.put(ware);
            ToastUtils.show(mContext,"已添加到购物车");
        }
    }
}
