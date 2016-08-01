package com.example.myshop.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myshop.R;

/**
 * Created by Thank on 2016/2/22.
 */
public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    public static final String TAG="NumberAddSubView";
    public static final int DEFUALT_MAX=1000;

    private LayoutInflater mInflater;

    private Button mBtnAdd;
    private Button mBtnSub;
    private TextView mTxtNum;

    private int value;
    private int minValue;
    private int maxValue = DEFUALT_MAX;

    private OnButtonClickListener mListener;
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }

    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0 );
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        initView();

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.NumberAddSubView, defStyleAttr, 0);
            int val =  a.getInt(R.styleable.NumberAddSubView_value,0);
            setValue(val);

            int maxVal = a.getInt(R.styleable.NumberAddSubView_maxValue,0);
            if(maxVal != 0)
                setMaxValue(maxVal);

            int minVal = a.getInt(R.styleable.NumberAddSubView_minValue, 0);
            if (minVal >= 0)
                setMinValue(minVal);

            Drawable txtBackground = a.getDrawable(R.styleable.NumberAddSubView_txtBackground);
            if(txtBackground!=null)
                setTextBackground(txtBackground);

            Drawable buttonAddBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonAddBackgroud);
            if(buttonAddBackground!=null)
                setButtonAddBackgroud(buttonAddBackground);

            Drawable buttonSubBackground = a.getDrawable(R.styleable.NumberAddSubView_buttonSubBackgroud);
            if(buttonSubBackground!=null)
                setButtonSubBackgroud(buttonSubBackground);

            a.recycle();
        }
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.num_add_sub_content, this, true);

        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnSub = (Button) view.findViewById(R.id.btn_sub);
        mTxtNum = (TextView) view.findViewById(R.id.txt_num);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);

    }

    private void numAdd() {
        getValue();
        if (this.value < maxValue)
            this.value = this.value + 1;

        mTxtNum.setText(""+value);
    }

    private void numSub() {
        getValue();
        if (this.value > minValue)
            this.value = this.value - 1;
        mTxtNum.setText(""+value);
    }

    public int getValue() {
        String value = mTxtNum.getText().toString();
        if (value!=null && !"".equals(value))
            this.value = Integer.parseInt(value);
        return this.value;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setValue(int value) {
        mTxtNum.setText(""+value);
        this.value = value;
    }

    public void setTextBackground(Drawable drawable){

        mTxtNum.setBackgroundDrawable(drawable);

    }

    public void setTextBackground(int drawableId){

       setTextBackground(getResources().getDrawable(drawableId));

    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud){
        this.mBtnAdd.setBackground(backgroud);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud){
        this.mBtnSub.setBackground(backgroud);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {

            numAdd();
            if (mListener != null) {
                mListener.onButtonAddClick(v, this.value);
            }

        } else if (v.getId() == R.id.btn_sub) {

            numSub();
            if (mListener != null) {
                mListener.onButtonSubClick(v, this.value);
            }

        }

    }

    public interface OnButtonClickListener{
        void onButtonAddClick(View view, int value);
        void onButtonSubClick(View view, int value);
    }
}
