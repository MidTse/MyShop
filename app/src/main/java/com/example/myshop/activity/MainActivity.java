package com.example.myshop.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.Tab;
import com.example.myshop.fragment.CartFragment;
import com.example.myshop.fragment.CategoryFragment;
import com.example.myshop.fragment.HomeFragment;
import com.example.myshop.fragment.HotFragment;
import com.example.myshop.fragment.MineFragment;
import com.example.myshop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabhost;
//    private MyToolbar mToolbar;

    private CartFragment mCart;

    private LayoutInflater mInflater;

    private List<Tab> mTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInflater = LayoutInflater.from(this);

//        initToolbar();
        initTabs();

    }

    private void initToolbar() {
//        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
    }

    private void initTabs() {

        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        //为fragmenttabhost设置盛装容器
        mTabhost.setup(this,getSupportFragmentManager(), R.id.realtabcontent);

        mTabs = new ArrayList<>(5);

        Tab tabHome = new Tab(R.string.tab_home,R.drawable.selector_icon_home, HomeFragment.class);
        Tab tabHot = new Tab(R.string.tab_hot,R.drawable.selector_icon_hot, HotFragment.class);
        Tab tabGategory = new Tab(R.string.tab_catagory,R.drawable.selector_icon_gategory, CategoryFragment.class);
        Tab tabCart = new Tab(R.string.tab_cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tabMine = new Tab(R.string.tab_mine,R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(tabHome);
        mTabs.add(tabHot);
        mTabs.add(tabGategory);
        mTabs.add(tabCart);
        mTabs.add(tabMine);

        for (Tab tab : mTabs) {

            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(),null);
        }

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == getString(R.string.tab_cart)) {
                    refData();
                }
            }
        });

        //设置是否显示分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);



    }

    private void refData() {

        if (mCart == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab_cart));
            if (fragment != null) {
                mCart = (CartFragment) fragment;
                mCart.refData();
            }
        } else {
            mCart.refData();
        }
    }

    private View buildIndicator(Tab tab) {

        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.img_tab);
        TextView txt = (TextView) view.findViewById(R.id.txt_tab);

        img.setBackgroundResource(tab.getIcon());
        txt.setText(tab.getTitle());

        return view;
    }
}
