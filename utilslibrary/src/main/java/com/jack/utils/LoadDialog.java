package com.jack.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class LoadDialog extends Dialog {
    private int mWidth, mHeight;
    private TextView mTitle;
    private RelativeLayout viewById;

    public LoadDialog(@NonNull Context context, int show) {
        this(context, 0, show);
    }

    public LoadDialog(@NonNull Context context, int themeResId, int show) {
        super(context, themeResId != 0 ? themeResId : R.style.dialog_load);
        setCanceledOnTouchOutside(false);
        View mLoadDialogView = View.inflate(context, R.layout.dialog_load, null);
        viewById = mLoadDialogView.findViewById(R.id.rl);
        mTitle = mLoadDialogView.findViewById(R.id.tv_title);
        setContentView(mLoadDialogView);
        mWidth = CommUtils.getScreenWidth(context);
        mHeight = CommUtils.getScreenHeight(context);
        viewById.setVisibility(show == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = mWidth;
        params.height = mHeight;
        window.setAttributes(params);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }


    public void hidden() {
        viewById.setVisibility(View.GONE);
    }


}
