package com.wanban.wbbaner;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by fq on 2018/5/23.
 */

public abstract class WBImageLoader {

    public abstract <T> void loadImage(Context context, ImageView view, T t);

    public ImageView createImageView(Context context){
        return null;
    }
}
