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
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
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
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private RecyclerView mRecyclerviewWares;
    private SliderLayout mSliderlayout;
    private MaterialRefreshLayout mRefreshlayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdatper;


    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private long category_id=0;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

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

        initRefreshLayout();

        return view;
    }

    private void initRefreshLayout() {
        mRefreshlayout.setLoadMore(true);
        mRefreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if(currPage <=totalPage)
                    loadMoreData();
                else{
                    Toast.makeText(getContext(), "已经加载完毕，没有更多了哦！！",Toast.LENGTH_SHORT).show();
                    mRefreshlayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private  void refreshData(){

        currPage =1;
        state=STATE_REFREH;
        requestWares(category_id);

    }

    private void loadMoreData(){

        currPage = ++currPage;
        state = STATE_MORE;
        requestWares(category_id);

    }


    private void requestCategoryData() {

        mHttpHelper.doGet(Contants.API.CATEGORY_LIST_URL, new SpotsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                if (categories != null && categories.size()>0 ) {
                    category_id = categories.get(0).getId();
                    requestWares(category_id);
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
        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currPage+"&pageSize="+pageSize;
        mHttpHelper.doGet(url, new SpotsCallBack<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

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
                category_id = category.getId();
                currPage = 1;
                state = STATE_NORMAL;

                requestWares(category_id);


            }
        });

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));



    }

    private void showWaresData(List<Wares> list) {

        switch (state) {
            case STATE_NORMAL:
                if (mWaresAdatper == null) {
                    mWaresAdatper = new WaresAdapter(getContext(), list);
                    mRecyclerviewWares.setAdapter(mWaresAdatper);

                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                } else {
                    mWaresAdatper.clear();
                    mWaresAdatper.addData(list);
                }

                break;

            case STATE_REFREH:
                mWaresAdatper.clear();
                mWaresAdatper.addData(list);

                mRecyclerviewWares.scrollToPosition(0);
                mRefreshlayout.finishRefresh();
                break;

            case STATE_MORE:
                mWaresAdatper.addData(mWaresAdatper.getDatas().size(),list);
                mRecyclerviewWares.scrollToPosition(mWaresAdatper.getDatas().size());
                mRefreshlayout.finishRefreshLoadMore();
                break;

        }

    }
    





}



