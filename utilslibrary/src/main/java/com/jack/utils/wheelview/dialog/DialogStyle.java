package com.jack.utils.wheelview.dialog;

/**
 * @desc：   NORMAL:正常显示默认省市区  STANDARD：默认显示全部
 * @author: yongzhi
 * @time: 2017/4/10 0010
 * @reviser_and_time:
 */

public enum DialogStyle {
    NORMAL(1), STANDARD(2);
    private final int value;

    DialogStyle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
