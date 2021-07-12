package com.auto.di.guan.api;

/**
 * Created by chenzhaolin on 2019/7/9.
 */

public class ApiEntiy {
    // 图片
    public static final int ITEM_TYPE_0 = 0;
    // 气象title
    public static final int ITEM_TYPE_1 = 1;
    // 气象数据
    public static final int ITEM_TYPE_2 = 2;
    // 殇情title
    public static final int ITEM_TYPE_3 = 3;
    // 殇情数据
    public static final int ITEM_TYPE_4 = 4;

    public static String[] DEPTH_0 = {"地表", "depth0"};
    public static String[] DEPTH_10 = {"10cm", "depth10"};
    public static String[] DEPTH_20 = {"20cm", "depth20"};
    public static String[] DEPTH_30 = {"30cm", "depth30"};
    public static String[] DEPTH_40 = {"40cm", "depth40"};
    public static String[] DEPTH_50 = {"50cm", "depth50"};
    public static String[] DEPTH_60 = {"60cm", "depth60"};
    public static String[] DEPTH_70 = {"60cm", "depth70"};


    public static String getDepthName(String name) {
        String res = "-";
        if (DEPTH_0[0].equals(name)) {
            res = DEPTH_0[1];
        }else if (DEPTH_10[0].equals(name)) {
            res = DEPTH_10[1];
        }else if (DEPTH_20[0].equals(name)) {
            res = DEPTH_20[1];
        }else if (DEPTH_30[0].equals(name)) {
            res = DEPTH_30[1];
        }else if (DEPTH_40[0].equals(name)) {
            res = DEPTH_40[1];
        }else if (DEPTH_50[0].equals(name)) {
            res = DEPTH_50[1];
        }else if (DEPTH_60[0].equals(name)) {
            res = DEPTH_60[1];
        }else if (DEPTH_70[0].equals(name)) {
            res = DEPTH_70[1];
        }
        return res;
    }
    /**
     *   地表墒情
     */
    public static String [] POI_TITLE = {
            "采集时间",
            "深度",
            "土壤温度(℃)",
            "电池电量(v)",
            "外部输入电压(v)",
            "水分含量(%)"
    };

    public static final String DEVICE_TYPE = "气象监测";
}
