package com.jack.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by huisoucw on 2018/10/12.
 */

public class LoadDialog extends Dialog {
    private int mWidth, mHeight;

    public LoadDialog(@NonNull Context context) {
        this(context, 0);
    }

    public LoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId != 0 ? themeResId : R.style.dialog_load);
        setCanceledOnTouchOutside(false);
        View mLoadDialogView = View.inflate(context, R.layout.dialog_load, null);
        setContentView(mLoadDialogView);
        mWidth = CommUtils.getScreenWidth(context);
        mHeight = CommUtils.getScreenHeight(context);
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


}
