package com.example.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.adapter.CategoryAdapter;
import com.example.myshop.adapter.MyBaseAdapter;
import com.example.myshop.adapter.WaresAdapter;
import com.example.myshop.adapter.decortion.DividerGridItemDecoration;
import com.example.myshop.adapter.decortion.DividerItemDecoration;
import com.example.myshop.bean.Banner;
import com.example.myshop.bean.Category;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.example.myshop.utils.Pager;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryFragment extends BaseFragment implements Pager.OnPageListener<Wares>{

    private RecyclerView mRecyclerview;
    private RecyclerView mRecyclerviewWares;
    private SliderLayout mSliderlayout;
    private MaterialRefreshLayout mRefreshlayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdatper;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private Pager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerviewWares = (RecyclerView) view.findViewById(R.id.recycler_wares);
        mSliderlayout = (SliderLayout) view.findViewById(R.id.slider);
        mRefreshlayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        requestCategoryData();
        requestBannerData();
        return view;
    }

    private void requestCategoryData() {

        mHttpHelper.doGet(Contants.API.CATEGORY_LIST_URL, new SpotsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                if (categories != null && categories.size()>0 ) {
                    requestWares(categories.get(0).getId());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void requestBannerData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");

        mHttpHelper.doPost(Contants.API.BANNER_URL, map, new SpotsCallBack<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                initSliderView(banners);

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    private void requestWares(long categoryId) {
        
        if (pager == null) {
            pager = Pager.newBuilder()
                    .setPageListener(this)
                    .setRefreshLayout(mRefreshlayout)
                    .setUrl(Contants.API.WARES_LIST)
                    .setParam("categoryId", categoryId)
                    .build(getContext(), new TypeToken<Page<Wares>>() {}.getType());
            pager.request();
        } else {
            pager.putParam("categoryId", categoryId);
            pager.request();
        }
    }

    private void initSliderView(List<Banner> banners) {
        if (banners != null) {
            for (Banner banner : banners) {
                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView
                        .image(banner.getImgUrl())
                        .description(banner.getName());
                mSliderlayout.addSlider(textSliderView);
            }
        }

        mSliderlayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderlayout.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mSliderlayout.setCustomAnimation(new DescriptionAnimation());
        mSliderlayout.setDuration(3000);

    }

    private void showCategoryData(List<Category> categories) {


        mCategoryAdapter = new CategoryAdapter(getActivity(), categories);
        mRecyclerview.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnItemClickListener(new MyBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Category category = mCategoryAdapter.getItem(position);
                requestWares(category.getId());


            }
        });

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));



    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        if (mWaresAdatper == null) {
            mWaresAdatper = new WaresAdapter(getContext(), datas);
            mRecyclerviewWares.setAdapter(mWaresAdatper);

            mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
            mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
        } else {
            mWaresAdatper.clear();
            mWaresAdatper.addData(datas);
        }
        mWaresAdatper.notifyDataSetChanged();
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdatper.clear();
        mWaresAdatper.addData(datas);

        mRecyclerviewWares.scrollToPosition(0);
        mWaresAdatper.notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdatper.addData(mWaresAdatper.getDatas().size(),datas);
        mRecyclerviewWares.scrollToPosition(mWaresAdatper.getDatas().size());
        mWaresAdatper.notifyDataSetChanged();
    }
}



