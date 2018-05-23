package com.wanban.wbbaner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fq on 2018/5/23.
 */

public class WBannerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ImageView> views;
    private ViewGroup.LayoutParams layoutParams;
    private WBImageLoader imageLoader;


    public <T> WBannerAdapter(Context context, List<T> urls, WBImageLoader imageLoader) {
        this.context = context;
        this.imageLoader = imageLoader;
        views = new ArrayList<>(urls.size());
        for(T t: urls){
            ImageView simpleDraweeView = getItemView(t);
            views.add(simpleDraweeView);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    private <T> ImageView getItemView(T data) {
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        ImageView imageView = imageLoader.createImageView(context);
        if (imageView == null){
            imageView = new ImageView(context);
        }
        imageView.setLayoutParams(layoutParams);
        imageLoader.loadImage(context, imageView, data);
        return imageView;
    }

}
