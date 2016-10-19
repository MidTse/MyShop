package com.example.myshop.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.myshop.Contants;
import com.example.myshop.MyShopApplication;
import com.example.myshop.R;
import com.example.myshop.adapter.FavoriteAdapter;
import com.example.myshop.adapter.decortion.CardViewtemDecortion;
import com.example.myshop.bean.Favorites;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.TokenCallBack;
import com.example.myshop.widget.MyToolbar;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener{

    private MyToolbar mToolbar;
    private RecyclerView mRecyclerView;

    private FavoriteAdapter mApdater;
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private String token;
    private Long userId;
    private String TAG = "FavoriteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initData();
        requestData();
    }

    private void initData() {

        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mToolbar.getmLeftButton().setOnClickListener(this);

        token = MyShopApplication.getInstance().getToken();
        userId = MyShopApplication.getInstance().getUser().getId();
    }

    private void requestData() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", "" + userId);
        params.put("token", token);

        mHttpHelper.doPost(Contants.API.FAVORITE_LIST, params, new TokenCallBack<List<Favorites>>(this) {

            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                showDatas(favorites);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showDatas(List<Favorites> favorites) {

        mApdater = new FavoriteAdapter(this, favorites);
        mRecyclerView.setAdapter(mApdater);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
