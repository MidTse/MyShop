package com.example.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.adapter.HomeCampaignAdapter;
import com.example.myshop.adapter.decortion.CardViewtemDecortion;
import com.example.myshop.bean.Banner;
import com.example.myshop.bean.Campaign;
import com.example.myshop.bean.HomeCampaign;
import com.example.myshop.http.BaseCallback;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    private SliderLayout mSliderLayout;

    private RecyclerView mRecyclerView;

    private List<Banner> mBanners;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_home);

        requestBanners();

        requestCampaigns();

        return view;

    }

    //通过网络获取轮播图数据
    private void requestBanners() {


        Map<String, String> map = new HashMap<>();
        map.put("type", "1");

        mHttpHelper.doPost(Contants.API.BANNER_URL, map, new SpotsCallBack<List<Banner>>(this.getActivity()) {
            @Override
            public void onSuccess(Response response, List<Banner> bannerBeen) {
                mBanners = bannerBeen;
                initSliderView();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });




    }

    //初始化轮播图
    private void initSliderView() {


        if (mBanners != null) {
            for (Banner banner : mBanners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView
                        .image(banner.getImgUrl())
                        .description(banner.getName());
                mSliderLayout.addSlider(textSliderView);

            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setDuration(3000);


    }

    //通过网络获取活动列表数据
    private void requestCampaigns() {

        mHttpHelper.doGet(Contants.API.CAMPAIGN_HOME_URL, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaignBeen) {
                initRecyclerView(homeCampaignBeen);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });



    }

    //初始化活动列表
    private void initRecyclerView(List<HomeCampaign> datas) {

        HomeCampaignAdapter mAdapter = new HomeCampaignAdapter(datas, this.getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mAdapter.setOnClickListener(new HomeCampaignAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getActivity(), ""+ campaign.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }
}
