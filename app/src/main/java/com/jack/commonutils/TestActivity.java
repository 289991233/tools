package com.jack.commonutils;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import com.blankj.utilcode.util.ToastUtils;
import com.jack.utils.CommUtils;
import com.jack.utils.ImageLoad;
import com.jack.utils.LoadDialog;

public class TestActivity extends AppCompatActivity {
    JzvdStd videoplayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        videoplayer = findViewById(R.id.videoplayer);
        videoplayer.setUp("http://zhuanfafa.oss-cn-zhangjiakou.aliyuncs.com/1545793879DSy.mp4", "", Jzvd.SCREEN_WINDOW_NORMAL);
//                        mBinding.videoplayer.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        Jzvd.SAVE_PROGRESS = false;
        ImageLoad.glideLoader(this, videoplayer.thumbImageView, "http://dev.xinmem.com/profit/uploadfiles/large/20181226/1545793904664808_450x300.jpg");
        CommUtils.Toast(this, "ceshi ");
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
