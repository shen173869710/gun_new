package com.auto.di.guan.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.FloatStatusAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.floatWindow.FloatWindow;
import com.auto.di.guan.floatWindow.MoveType;
import com.auto.di.guan.floatWindow.Screen;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.List;

/**
 *  显示当前正在运行的阀门
 */

public class FloatStatusUtil {
    private static FloatStatusUtil instance = new FloatStatusUtil();
    private RecyclerView mListView;
    private FloatStatusAdapter adapter;
    private ArrayList<ControlInfo> controlInfos = new ArrayList<>();
    private View view;
    private TextView textView;
    private DonutProgress donutProgress;
    private GroupInfo groupInfo;

    LinearLayout linearLayout;

    public static synchronized FloatStatusUtil getInstance() {
        return instance;
    }

    private final String TAG = "FloatStatusUtil";

    public void initFloatWindow(Context mContext) {
        view = View.inflate(BaseApp.getInstance(), R.layout.dialog_run_listview, null);
        view.setFocusableInTouchMode(true);
        mListView = (RecyclerView) view.findViewById(R.id.list);
        donutProgress = view.findViewById(R.id.progress);

        linearLayout = view.findViewById(R.id.layout_list);
        textView = view.findViewById(R.id.text);

        if (groupInfo != null && groupInfo.getGroupStatus() == Entiy.GROUP_STATUS_OPEN) {
            List<ControlInfo> list = ControlInfoSql.queryControlList(groupInfo.getGroupId());
            controlInfos.clear();
            controlInfos.addAll(list);

        }
        initProgess(groupInfo);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupInfo == null || groupInfo.getGroupStatus() == 0) {
                    initProgess(null);
                    ToastUtils.showLongToast("当前没有运行的设备");
                    return;
                }
                onGroupStatusEvent(groupInfo);
                if (mListView.getVisibility() == View.VISIBLE) {
                    mListView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    LogUtils.e(TAG, "gon");
                } else {
                    mListView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    LogUtils.e(TAG, "VISIBLE");
                }
            }
        });
        adapter = new FloatStatusAdapter(controlInfos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(adapter);
    }

    public void cleanList() {
        if (adapter != null) {
            controlInfos.clear();
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isShow() {
        if (FloatWindow.get(TAG) == null) {
            return false;
        }
        return FloatWindow.get(TAG).isShowing();
    }

    public void show() {
        if (FloatWindow.get(TAG) == null) {
            int size = Math.round(BaseApp.getContext().getResources().getDimension(R.dimen.float_status_size));
            FloatWindow.with(BaseApp.getInstance())
                    .setView(view)
//                    .setWidth(size*8)
                    .setHeight(size)
                    .setX(Screen.width, 0.9f)
                    .setY(Screen.height, 0.5f)
                    .setFilter(true, MainActivity.class)
                    .setMoveType(MoveType.active)
                    .setTag(TAG)
                    .build();
            FloatWindow.get(TAG).show();
        } else {
            if (!FloatWindow.get(TAG).isShowing()) {
                FloatWindow.get(TAG).show();
            }
        }
    }

    public void distory() {
        controlInfos.clear();
        adapter.notifyDataSetChanged();
        FloatWindow.destroy(TAG);
    }

    /**
     *       单个阀门的开启
     * @param event
     */
    public void onStatsuEvent(CmdStatus event) {
        List<GroupInfo> groupInfos = GroupInfoSql.queryOpenGroupList();
        if (groupInfos != null && groupInfos.size() > 0) {
            onGroupStatusEvent(groupInfos.get(0));
        }
    }

    public void onGroupStatusEvent(GroupInfo info) {
        if (info != null && info.getGroupStatus() == Entiy.GROUP_STATUS_OPEN) {
            List<ControlInfo> controlInfoList = ControlInfoSql.queryControlList(info.getGroupId());
            int size = controlInfoList.size();
            if (size > 0) {
                controlInfos.clear();
                controlInfos.addAll(controlInfoList);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
        initProgess(info);
    }
    /**
     *        计算当前运行的时间
     * @param info
     */
    public void onAutoCountEvent(GroupInfo info) {
        initProgess(info);
    }

    /**
     *         初始化进度条
     * @param info
     */
    public void initProgess(GroupInfo info) {
        if (info != null && info.getGroupStatus() == Entiy.GROUP_STATUS_OPEN) {
            groupInfo = info;

            if (groupInfo.getGroupTime() == 0 && groupInfo.getGroupRunTime() ==0) {
                controlInfos.clear();
                if (donutProgress != null) {
                    textView.setText("轮灌完成\n设备未关闭");
                    donutProgress.setVisibility(View.INVISIBLE);
                }
            }else {
                if (donutProgress != null) {
                    donutProgress.setVisibility(View.VISIBLE);
                    donutProgress.setMax(groupInfo.getGroupTime());
                    donutProgress.setProgress(groupInfo.getGroupRunTime());
//                    textView.setText("时长:" + groupInfo.getGroupTime() + "\n运行:" + info.getGroupRunTime());
                    textView.setText("时长:" + secToTime(groupInfo.getGroupTime()) + "\n运行:" + secToTime(info.getGroupRunTime()));
                }
            }

        }else {
            controlInfos.clear();
            if (donutProgress != null) {
                textView.setText("无设备运行");
                donutProgress.setVisibility(View.INVISIBLE);
            }
        }
    }



    public  String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public  String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }
}
