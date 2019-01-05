package com.jack.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoad {
    /**
     * //原生 API
     *
     * @param context
     * @param url
     * @param iv
     */
    public static void glideLoader(Context context, ImageView iv, String url) {
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(iv);
    }

    /**
     * @param context
     * @param url
     * @param iv
     * @param tag     切圆角的度数
     */
    public static void glideLoader(Context context, ImageView iv, String url, int tag) {
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }
        RequestOptions options = new RequestOptions();
        if (tag >= 180) {
            //圆形
            options.transform(new CircleCrop());
        } else {
            options.transform(new RoundedCorners(tag));
        }
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(iv);
    }
}
