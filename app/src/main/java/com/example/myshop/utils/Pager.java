package com.example.myshop.utils;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.myshop.bean.Page;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thank on 2016/8/1.
 */
public class Pager {

    private static Builder mBuilder;
    private static Pager mPager;
    private OkHttpHelper mHttpHelper;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int mState = STATE_NORMAL;

    private Pager() {
        mHttpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder() {
        mBuilder = new Builder();
        return mBuilder;
    }

    /**
     * 请求网络
     **/
    public void request() {
        requestData();
    }

    /**
     * 初始化刷新控件事件监听
     * **/
    private void initRefreshLayout() {
        mBuilder.mRefreshlayout.setLoadMore(true);
        mBuilder.mRefreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (mBuilder.pageIndex <= mBuilder.totalPage) {
                    loadmoreData();
                } else {
                    Toast.makeText(mBuilder.mContext, "已经加载完毕，没有更多了哦！！",Toast.LENGTH_SHORT).show();
                    mBuilder.mRefreshlayout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 下拉刷新数据
     * **/
    private void refreshData() {
        mBuilder.pageIndex = 1;
        mState = STATE_REFRESH;
        requestData();
    }

    /**
     * 上拉加载数据
     * **/
    private void loadmoreData() {
        mBuilder.pageIndex = ++mBuilder.pageIndex;
        mState = STATE_MORE;
        requestData();
    }

    /**
     * 请求数据
     * **/
    private void requestData() {
        String url = buildUrl();
        mHttpHelper.doGet(url, new RequestCallBack(mBuilder.mContext));
    }

    /**
     * 显示数据
     * **/
    private <T> void showData(List<T> datas, int totalPage, int totalCount) {

        switch (mState) {
            case STATE_NORMAL:

                if (mBuilder.mPageListener != null) {
                    mBuilder.mPageListener.load(datas, totalPage, totalCount);
                }
                break;

            case STATE_REFRESH:

                if (mBuilder.mPageListener != null) {
                    mBuilder.mPageListener.refresh(datas, totalPage, totalCount);
                    mState = STATE_NORMAL;
                }
                mBuilder.mRefreshlayout.finishRefresh();
                break;

            case STATE_MORE:

                if (mBuilder.mPageListener != null) {
                    mBuilder.mPageListener.loadMore(datas, totalPage, totalCount);
                    mState = STATE_NORMAL;
                }
                mBuilder.mRefreshlayout.finishRefreshLoadMore();
                break;
        }

    }

    /**
     * 通过传入的参数构建URL地址
     * **/
    private String buildUrl(){

        return mBuilder.mUrl +"?"+buildUrlParams();
    }
    private String buildUrlParams() {

        HashMap<String, Object> map = mBuilder.params;
        map.put("curPage",mBuilder.pageIndex);
        map.put("pageSize",mBuilder.pageSize);
        map.put("categoryId", mBuilder.categoryId);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }

        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0,s.length()-1);
        }
        return s;
    }



    /**
     * ****************** 内部类Builder **********************
     * **/
    public static class Builder {

        private String mUrl;
        private HashMap<String, Object> params = new HashMap<>(5);
        private MaterialRefreshLayout mRefreshlayout;
        private boolean canLoadMore;
        private OnPageListener mPageListener;
        private Context mContext;
        private Type mType;

        private int pageIndex=1;
        private int totalPage=1;
        private int pageSize=10;
        private long categoryId = 0;

        /**
         * Builder设置参数的方法
         * **/
        public Builder setUrl(String url) {
            this.mUrl = url;
            return mBuilder;
        }

        public Builder setParam(String key, Object value) {
            this.params.put(key, value);
            return mBuilder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.mRefreshlayout = refreshLayout;
            return mBuilder;
        }

        public Builder setCanLoadMore(boolean canLoadMore) {
            this.canLoadMore = canLoadMore;
            return mBuilder;
        }

        public Builder setPageListener(OnPageListener listener) {
            this.mPageListener = listener;
            return mBuilder;
        }

        public Builder setPageSize(int size) {
            this.pageSize = size;
            return mBuilder;
        }

        public Builder setPageIndex(int index) {
            this.pageIndex = index;
            return mBuilder;
        }

        public Builder setTotalPage(int total) {
            this.totalPage = total;
            return mBuilder;
        }

        public Builder setCategoryId(long id) {
            this.categoryId = id;
            return mBuilder;
        }

        public Pager build(Context context, Type type) {
            this.mContext = context;
            this.mType = type;
            chkValid();
            mPager = new Pager();
            return mPager;

        }

        /**
         * 重新建立builer
         * **/
        public Pager reBulid() {
            chkValid();
            if (mPager != null) {
                return mPager;
            } else {
                mPager = new Pager();
                return mPager;
            }
        }

        /**
         * 检查重要参数是否不为空，否则抛出异常
         * **/
        private void chkValid(){

            if(this.mContext==null)
                throw  new RuntimeException("content can't be null");

            if(this.mUrl==null || "".equals(this.mUrl))
                throw  new RuntimeException("url can't be  null");

            if(this.mRefreshlayout==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }
    }

    /**
     * **********************内部类RequestCallBack，关于请求数据并回调 ******************************
     * **/
    private class RequestCallBack<T> extends SpotsCallBack<Page<T>> {

        public RequestCallBack(Context mContext) {
            super(mContext);
            super.mType = mBuilder.mType;
        }

        @Override
        public void onSuccess(Response response, Page<T> page) {
            mBuilder.pageIndex = page.getCurrentPage();
            mBuilder.pageSize = page.getPageSize();
            mBuilder.totalPage = page.getTotalPage();

            showData(page.getList(),page.getTotalPage(),page.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            Toast.makeText(mBuilder.mContext,"加载数据失败",Toast.LENGTH_LONG).show();

            if(STATE_REFRESH == mState)   {
                mBuilder.mRefreshlayout.finishRefresh();
            }
            else  if(STATE_MORE == mState){

                mBuilder.mRefreshlayout.finishRefreshLoadMore();
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            Toast.makeText(mBuilder.mContext,"请求出错："+e.getMessage(),Toast.LENGTH_LONG).show();

            if(STATE_REFRESH == mState)   {
                mBuilder.mRefreshlayout.finishRefresh();
            }
            else  if(STATE_MORE == mState){

                mBuilder.mRefreshlayout.finishRefreshLoadMore();
            }
        }
    }

    /**
     * **************** 声明刷新控件的接口 *****************
     * **/
    public interface  OnPageListener<T>{

        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas,int totalPage,int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);
    }

}
