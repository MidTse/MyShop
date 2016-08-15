package com.example.myshop.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myshop.Contants;
import com.example.myshop.R;

public class PayResultActivity extends AppCompatActivity {

    private ImageView imgPayStatus;
    private TextView txtPayStatus;
    private Button btnReturn;

    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);

        status = getIntent().getIntExtra(Contants.ORDER_STATUS, -1);
        initView();
        initStatus();

    }

    private void initView() {

        imgPayStatus = (ImageView) findViewById(R.id.img_pay_result);
        txtPayStatus = (TextView) findViewById(R.id.txt_pay_result);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initStatus() {

        switch (status) {
            case Contants.NUM_SUCCESS:
                setStatus(getResources().getString(R.string.order_success), R.drawable.icon_success_128);
                break;

            case Contants.NUM_FAIL:
                setStatus(getResources().getString(R.string.order_failed), R.drawable.icon_failed);
                break;

            case Contants.NUM_CANCEL:
                setStatus(getResources().getString(R.string.order_cancel), R.drawable.icon_cancel_128);
                break;

            default:

                break;
        }
    }

    private void setStatus(String text, int imgId) {

        imgPayStatus.setImageResource(imgId);
        txtPayStatus.setText(text);
    }
}
