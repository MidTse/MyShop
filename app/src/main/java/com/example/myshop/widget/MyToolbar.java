package com.example.myshop.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myshop.R;

/**
 * Created by Thank on 2015/12/14.
 */
public class MyToolbar extends Toolbar{

    private View mView;
    private LayoutInflater mInflater;
    private EditText mSearchView;
    private TextView mTextTitle;
    private Button mRightButton;
    private Button mLeftButton;
    
    
    public MyToolbar(Context context) {
        this(context, null);
    }

    public MyToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10,10);

        if (attrs != null) {

            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MyToolbar, defStyleAttr, 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.MyToolbar_rightButtonIcon);
            if (rightIcon != null) {

                setRightButtonIcon(rightIcon);
            }

            final Drawable leftIcon = a.getDrawable(R.styleable.MyToolbar_leftButtonIcon);
            if (leftIcon != null) {

                setLeftButtonIcon(leftIcon);
            }



            final String rightText = a.getString(R.styleable.MyToolbar_rightButtonText);
            if (rightText != null) {

                setRightButtonText(rightText);
            }

            final String seachViewHint = a.getString(R.styleable.MyToolbar_searchViewhint);
            if (seachViewHint != null) {
                setSearchViewHint(seachViewHint);
            }

            final boolean isShowTitle = a.getBoolean(R.styleable.MyToolbar_isShowTitle, false);
            if(!isShowTitle) {
                showSearchView();
                hideTitle();

            }else {
                showTitle();
                hideSearchView();
            }
            a.recycle();

        }

    }

    private void setSearchViewHint(String seachViewHint) {
        if (mSearchView != null) {
            mSearchView.setHint(seachViewHint);
        }
    }


    private void initView() {

        if (mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar_content, null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            mLeftButton = (Button) mView.findViewById(R.id.toolbar_leftButton);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);


            addView(mView,lp);

        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void  setRightButtonIcon(Drawable icon){

        if(mRightButton !=null){

            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }

    public void setRightButtonIcon(int icon){
        setRightButtonIcon(getResources().getDrawable(icon));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void  setLeftButtonIcon(Drawable icon){

        if(mLeftButton !=null){

            mLeftButton.setBackground(icon);
            mLeftButton.setVisibility(VISIBLE);
        }

    }

    public void setLeftButtonIcon(int icon){
        setLeftButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightButtonText(String text) {

        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public  void setRightButtonOnClickListener(OnClickListener listener){

        mRightButton.setOnClickListener(listener);
    }


    @Override
    public void setTitle( int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        mTextTitle.setText(title);
        showTitle();
    }

    public void showTitle() {

        if (mTextTitle != null){

            mTextTitle.setVisibility(VISIBLE);
        }
    }

    public void hideTitle() {

        if (mTextTitle != null){

            mTextTitle.setVisibility(GONE);
        }

    }

    public void showSearchView() {

        if (mSearchView != null) {

            mSearchView.setVisibility(VISIBLE);
        }

    }

    public void hideSearchView() {

        if (mSearchView != null) {

            mSearchView.setVisibility(GONE);
        }

    }

    public Button getRightButton(){

        return this.mRightButton;
    }

    public Button getmLeftButton(){

        return this.mLeftButton;
    }

}
