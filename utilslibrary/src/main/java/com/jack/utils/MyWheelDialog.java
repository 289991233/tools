package com.jack.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.jack.utils.wheelview.adapter.ArrayWheelAdapter;
import com.jack.utils.wheelview.adapter.CommonUtil;
import com.jack.utils.wheelview.adapter.OnWheelChangedListener;
import com.jack.utils.wheelview.adapter.WheelView;
import com.jack.utils.wheelview.dialog.DialogStyle;
import com.jack.utils.wheelview.dialog.LoadStyle;
import com.jack.utils.wheelview.dialog.callback.OnWheelClickListener;
import com.jack.utils.wheelview.dialog.entity.Address;
import com.jack.utils.wheelview.dialog.entity.AddressSaveAllEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class MyWheelDialog extends Dialog implements OnWheelChangedListener, View.OnClickListener {
    private final Context mContext;
    private final OnWheelClickListener mWheelClickLitener;
    private View view;
    private final WheelView wArea;
    private final WheelView wArea_child;
    private final WheelView wArea_child2;
    private final TextView area_tv_ok;
    private final TextView area_tv_cancel;
    private String[] area;
    private String[] areacode;
    private HashMap<Integer, String[]> area_city = new HashMap<>();
    private HashMap<Integer, String[]> area_citycode = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, String[]>> area_country = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, String[]>> area_countrycode = new HashMap<>();
    public static final String TAG = "test";
    private DialogStyle mDialogStyle;
    private LoadStyle mLoadStyle;
    private AddressSaveAllEntity saveAllEntity = null;


    public MyWheelDialog(Context context, DialogStyle type, LoadStyle dat, OnWheelClickListener wheelClickLitener) {
        super(context, R.style.transparentFrameWindowStyle);
        mContext = context;
        this.mDialogStyle = type;
        this.mLoadStyle = dat;
        mWheelClickLitener = wheelClickLitener;
        view = View.inflate(context, R.layout.dialog_select_area, null);
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                CommonUtil.getScreenHeight(context) / 2 - 100));
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.Dialog_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wl.x = 0;
        wl.y = wm.getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
        wArea = (WheelView) view.findViewById(R.id.id_area);
        wArea_child = (WheelView) view.findViewById(R.id.id_area_child);
        wArea_child2 = (WheelView) view.findViewById(R.id.id_area_child2);
        area_tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        area_tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        /**
         * 省市区样式LoadStyle
         * ALL(省市区全部显示)，PROVINCE(只显示省)，PROVINCE_CITY（只显示省市）
         */
        if (mLoadStyle.getValue() == 2) {
            wArea_child.setVisibility(View.GONE);
            wArea_child2.setVisibility(View.GONE);
        } else if (mLoadStyle.getValue() == 3) {
            wArea_child2.setVisibility(View.GONE);
        }
        saveAllEntity = AddressSaveAllEntity.newInstance(mContext.getApplicationContext());
        fillData();

    }

    private void initView() {
        wArea.addChangingListener(this);
        wArea_child.addChangingListener(this);
        wArea.setVisibleItems(5);
        wArea.setViewAdapter(new ArrayWheelAdapter<>(
                mContext, area));
        wArea_child.setViewAdapter(new ArrayWheelAdapter<>(mContext, area_city.get(0)));
        wArea_child2.setViewAdapter(new ArrayWheelAdapter<>(mContext, area_country.get(0).get(0)));
        area_tv_ok.setOnClickListener(this);
        area_tv_cancel.setOnClickListener(this);
    }

    private void fillData() {
        DefaultThreadExecutor.getInstance().execute(new FillDataTask());
    }

    private boolean isInitData;
    LoadDialog mLoadDialog;
    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                initView();
                if (mLoadDialog != null && mLoadDialog.isShowing()) {
                    mLoadDialog.dismiss();
                    show();
                }
                return true;
            }
            return false;
        }
    });

    @Override
    public void show() {
        if (!isInitData) {
            mLoadDialog = new LoadDialog(mContext);
            mLoadDialog.show();
            return;
        }
        super.show();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wArea) {
            wArea_child.setViewAdapter(new ArrayWheelAdapter<String>(mContext, area_city.get(wArea.getCurrentItem())));
            wArea_child.setCurrentItem(0);
            wArea_child2.setViewAdapter(new ArrayWheelAdapter<String>(mContext, area_country.get(wArea.getCurrentItem()).get(wArea_child.getCurrentItem())));
            wArea_child2.setCurrentItem(0);
        }
        if (wheel == wArea_child) {
            wArea_child2.setViewAdapter(new ArrayWheelAdapter<String>(mContext, area_country.get(wArea.getCurrentItem()).get(wArea_child.getCurrentItem())));
            wArea_child2.setCurrentItem(0);
        }
    }

    private class FillDataTask implements Runnable {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            String json = readFromAsset(mContext);
            //DialogStyle是否显示全部
            if (mDialogStyle.getValue() == 1) {
                province(json);
            } else {
                province2(json);
            }
            isInitData = true;
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok) {
            String provinceName = area[wArea.getCurrentItem()];
            String provinceID = areacode[wArea.getCurrentItem()];
            String cityName = area_city.get(wArea.getCurrentItem())[wArea_child.getCurrentItem()];
            String cityID = area_citycode.get(wArea.getCurrentItem())[wArea_child.getCurrentItem()];
            String countryName = "";
            String countryID = "";

            if (area_country.get(wArea.getCurrentItem()).get(wArea_child.getCurrentItem()).length > 0) {
                countryName = area_country.get(wArea.getCurrentItem()).get(wArea_child.getCurrentItem())[wArea_child2.getCurrentItem()];
                countryID = area_countrycode.get(wArea.getCurrentItem()).get(wArea_child.getCurrentItem())[wArea_child2.getCurrentItem()];
            } else {
                countryName = "";
                countryID = "";
            }
            Address mAddress = new Address(provinceName, provinceID, cityName, cityID, countryName, countryID);
            mWheelClickLitener.onOkClick(mAddress);
            dismiss();
        } else if (v.getId() == R.id.tv_cancel) {
            mWheelClickLitener.onCancelClick();
            cancel();
        }

    }


    public String readFromAsset(Context context) {
        try {
            InputStream in = context.getClass().getClassLoader().getResourceAsStream("assets/city.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer input = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                input.append(line);
            }
            String result = input.toString();
            result = result.replaceAll("\r", "");
            result = result.replaceAll("	", "");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void province(String res) {
        if (res.length() != 0) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.optString("code").equals("40000")) {
                    JSONObject list = jsonObject.optJSONObject("list");
                    JSONArray regions = list.optJSONArray("regions");
                    area = new String[regions.length()];
                    areacode = new String[regions.length()];
                    saveAllEntity.setArea(area);
                    saveAllEntity.setAreacode(areacode);
                    for (int i = 0; i < regions.length(); i++) {
                        JSONObject p = regions.optJSONObject(i);
                        String pname = p.optString("title");
                        String pcode = p.optString("regions_id");
                        area[i] = pname;//省
                        areacode[i] = pcode;//省id
                        JSONArray citylist = p.optJSONArray("child");
                        String[] city = new String[citylist.length()];
                        String[] citycode = new String[citylist.length()];
                        HashMap<Integer, String[]> district = new HashMap<>();//城区
                        HashMap<Integer, String[]> districtcode = new HashMap<>();//城区id
                        for (int j = 0; j < citylist.length(); j++) {
                            JSONObject c = citylist.optJSONObject(j);
                            String cname = c.optString("title");
                            String ccode = c.optString("regions_id");
                            city[j] = cname;
                            citycode[j] = ccode;
                            JSONArray countrylist = c.optJSONArray("child");
                            String[] country = new String[countrylist.length()];
                            String[] countrycode = new String[countrylist.length()];
                            for (int k = 0; k < countrylist.length(); k++) {
                                JSONObject countryObj = countrylist.optJSONObject(k);
                                String countryname = countryObj.optString("title");
                                String countrycode1 = countryObj.optString("regions_id");
                                country[k] = countryname;
                                countrycode[k] = countrycode1;
                            }
                            district.put(j, country);
                            districtcode.put(j, countrycode);
                        }
                        area_country.put(i, district);//城区
                        area_countrycode.put(i, districtcode);//城区id
                        area_city.put(i, city);//city
                        area_citycode.put(i, citycode);//city id

                        saveAllEntity.setArea_country(area_country);
                        saveAllEntity.setArea_countrycode(area_countrycode);
                        saveAllEntity.setArea_city(area_city);
                        saveAllEntity.setArea_citycode(area_citycode);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void province2(String res) {
        if (res.length() != 0) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.optString("code").equals("40000")) {
                    JSONObject list = jsonObject.optJSONObject("list");
                    JSONArray regions = list.optJSONArray("regions");
                    area = new String[regions.length() + 1];
                    areacode = new String[regions.length() + 1];
                    HashMap<Integer, String[]> dname = new HashMap<>();//城区
                    HashMap<Integer, String[]> dcode = new HashMap<>();//城区id
                    for (int i = 0; i < regions.length() + 1; i++) {
                        if (i == 0) {
                            area[i] = "全部";//省
                            areacode[i] = "0";//省id
                            String[] city = new String[]{"全部"};
                            String[] citycode = new String[]{"0"};
                            area_city.put(i, city);//city
                            area_citycode.put(i, citycode);//city id
                            saveAllEntity.setArea(area);
                            saveAllEntity.setAreacode(areacode);
                            dname.put(i, city);
                            dcode.put(i, citycode);
                            area_country.put(i, dname);
                            area_countrycode.put(i, dcode);
                        } else {
                            JSONObject p = regions.optJSONObject(i - 1);
                            String pname = p.optString("title");
                            String pcode = p.optString("regions_id");
                            area[i] = pname;//省
                            areacode[i] = pcode;//省id
//                            Log.e("aa", " sheng " + area);
                            JSONArray citylist = p.optJSONArray("child");
                            String[] city = null;
                            String[] citycode = null;
                            city = new String[citylist.length() + 1];
                            citycode = new String[citylist.length() + 1];
                            HashMap<Integer, String[]> district = new HashMap<>();//城区
                            HashMap<Integer, String[]> districtcode = new HashMap<>();//城区id
                            for (int j = 0; j < citylist.length() + 1; j++) {
                                String[] country = null;
                                String[] countrycode = null;
                                if (j == 0) {
                                    city[j] = "全市";
                                    citycode[j] = "0";
                                    country = new String[]{"全市"};
                                    countrycode = new String[]{"0"};
                                    district.put(0, country);
                                    districtcode.put(0, countrycode);
                                    area_country.put(0, district);//城区
                                    area_countrycode.put(0, districtcode);//城区id
                                } else {
                                    JSONObject c = citylist.optJSONObject(j - 1);
                                    String cname = c.optString("title");
                                    String ccode = c.optString("regions_id");
                                    JSONArray countrylist = c.optJSONArray("child");
                                    country = new String[countrylist.length() + 1];
                                    countrycode = new String[countrylist.length() + 1];
                                    city[j] = cname;
                                    citycode[j] = ccode;
                                    for (int k = 0; k < countrylist.length() + 1; k++) {
                                        if (k == 0) {
                                            country[k] = "全区";
                                            countrycode[k] = "0";
                                        } else {
                                            JSONObject countryObj = countrylist.optJSONObject(k - 1);
                                            String countryname = countryObj.optString("title");
                                            String countrycode1 = countryObj.optString("regions_id");
                                            country[k] = countryname;
                                            countrycode[k] = countrycode1;
                                        }
                                    }
                                    district.put(j, country);
                                    districtcode.put(j, countrycode);
                                }

                            }
                            area_country.put(i, district);//城区
                            area_countrycode.put(i, districtcode);//城区id
                            area_city.put(i, city);//city
                            area_citycode.put(i, citycode);//city id

                            saveAllEntity.setArea_country(area_country);
                            saveAllEntity.setArea_countrycode(area_countrycode);
                            saveAllEntity.setArea_city(area_city);
                            saveAllEntity.setArea_citycode(area_citycode);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
