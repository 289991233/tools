package com.jack.utils.wheelview.dialog;

/**
 * @desc：ALL(省市区全部显示)，PROVINCE(只显示省)，PROVINCE_CITY（只显示省市）
 * @author: yongzhi
 * @time: 2017/4/20 0020
 * @reviser_and_time:
 */

public enum LoadStyle {

    ALL(1), PROVINCE(2), PROVINCE_CITY(3);
    private final int value;

    LoadStyle(int var) {
        this.value = var;
    }

    public int getValue() {
        return value;
    }
}
