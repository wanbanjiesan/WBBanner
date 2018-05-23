package com.wanban.wbbaner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fq on 2018/5/23.
 */

public class WBBanner extends RelativeLayout implements ViewPager.OnPageChangeListener{

    private List dataList;
    private WBImageLoader imageLoader;
    private LoopHandler loopHandler;
    private long duration = 3000;
    private int currentPosition;
    private ViewPager viewPager;


    public WBBanner(Context context) {
        this(context, null);
    }

    public WBBanner(Context context, AttributeSet attrs) {
        super(context, attrs);





    }



    public <T> WBBanner setImageUrls(List<T> datas){
        if (datas == null){
            throw new NullPointerException("data 不允许null");
        }
        dataList = new ArrayList<T>();
        dataList.addAll(datas);
        int size = datas.size();
        dataList.add(0, datas.get(size - 1));
        dataList.add(dataList.size(), datas.get(0));
        return this;
    }

    public WBBanner setImageLoader(WBImageLoader imageLoader){
        if (imageLoader != null){
            this.imageLoader = imageLoader;
        }
        return this;
    }

    public WBBanner setPageTransformer(ViewPager.PageTransformer transformer){
        viewPager.setPageTransformer(true, transformer);
        return this;
    }

    public WBBanner setDuration(long duration){
        this.duration = duration;
        return this;
    }


    CircleIndicator circleIndicator;
    public void start(){
        if (imageLoader == null){
            throw new NullPointerException("imageLoader 必须实现");
        }
        // 初始化viewPager
        initViewPager(getContext());
        // 需要获取高度
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                // 初始化指示器
                initIndicator();
            }
        });
    }

    private void initIndicator() {
        circleIndicator = new CircleIndicator(getContext());
        circleIndicator.initData(dataList.size() - 2, 0);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, viewPager.getId());
        params.bottomMargin = viewPager.getMeasuredHeight() / 10;
        circleIndicator.setLayoutParams(params);
        addView(circleIndicator);
    }

    private void initViewPager(Context context) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(params);
        viewPager.addOnPageChangeListener(this);
        int id = 0xff1;
        viewPager.setId(id);
        addView(viewPager);
        WBannerAdapter adapter = new WBannerAdapter(getContext(), dataList, imageLoader);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1, false);
    }

    public void startAutoPlay(){
        if (dataList.size() <= 1){
            return;
        }
        if (loopHandler == null){
            loopHandler = new LoopHandler(duration, viewPager);
        }
        loopHandler.removeMessages(0);
        loopHandler.sendEmptyMessageDelayed(0, duration);
    }


    public void stopAutoPlay(){
        if (loopHandler != null){
            loopHandler.removeMessages(0);
            loopHandler = null;
        }
    }

    private static class LoopHandler extends Handler {

        private long duration;
        private ViewPager viewPager;

        LoopHandler(long duration, ViewPager viewPager) {
            this.viewPager = viewPager;
            this.duration = duration;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                int currentPos = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(currentPos);
                sendEmptyMessageDelayed(0, duration);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        if (circleIndicator != null){
            circleIndicator.setCurrentPage(position - 1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 若viewpager滑动未停止，直接返回
        if (state != ViewPager.SCROLL_STATE_IDLE || dataList == null) return;
        // 若当前为第一张，设置页面为倒数第二张
        int size = dataList.size();
        if (currentPosition == 0) {
            viewPager.setCurrentItem(size - 2, false);
        } else if (currentPosition == size - 1) {
            // 若当前为倒数第一张，设置页面为第二张
            viewPager.setCurrentItem(1, false);
        }
    }
}
