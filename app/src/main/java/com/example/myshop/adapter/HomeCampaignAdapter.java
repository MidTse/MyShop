package com.example.myshop.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.Campaign;
import com.example.myshop.bean.HomeCampaign;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thank on 2015/12/18.
 */
public class HomeCampaignAdapter extends RecyclerView.Adapter<HomeCampaignAdapter.ViewHoder> {

    private List<HomeCampaign> mDatas;
    private static final int VIEW_TYPE_L = 0;
    private static final int VIEW_TYPE_R = 1;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnCampaignClickListener mListener;

    public void setOnClickListener(OnCampaignClickListener listener) {
        this.mListener = listener;
    }

    public HomeCampaignAdapter(List<HomeCampaign> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_L) {
            view = mInflater.inflate(R.layout.template_home_cardview_left, parent,false);
        }else {
            view = mInflater.inflate(R.layout.template_home_cardview_right, parent, false);
        }
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {
        HomeCampaign campaign = mDatas.get(position);

        holder.textTitle.setText(campaign.getTitle());
        Picasso.with(mContext).load(campaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(campaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(campaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_L;
        }
        return VIEW_TYPE_R;
    }

    class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHoder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

           startAnim(v);

        }

        private void startAnim(final View view) {

            ObjectAnimator animator =  ObjectAnimator.ofFloat(view, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());
                    switch (view.getId()) {
                        case R.id.imgview_big:
                            mListener.onClick(view, homeCampaign.getCpOne());
                            break;

                        case R.id.imgview_small_top:
                            mListener.onClick(view, homeCampaign.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mListener.onClick(view, homeCampaign.getCpThree());
                            break;
                        default:
                            break;
                    }
                }
            });

            animator.start();
        }
    }



    public interface OnCampaignClickListener {
        void onClick(View view, Campaign campaign);
    }
}
