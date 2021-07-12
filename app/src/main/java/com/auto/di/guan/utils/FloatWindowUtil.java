package com.auto.di.guan.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auto.di.guan.BaseApp;
import com.auto.di.guan.GroupStatusActivity;
import com.auto.di.guan.MainActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.DialogListViewAdapter;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.floatWindow.FloatWindow;
import com.auto.di.guan.floatWindow.MoveType;
import com.auto.di.guan.floatWindow.Screen;
import com.auto.di.guan.rtm.MessageSend;

import java.util.ArrayList;
/**
 * Created by Administrator on 2018/7/25.
 *   悬浮窗显示状态
 */

public class FloatWindowUtil {
    private static FloatWindowUtil instance = new FloatWindowUtil();
    private RecyclerView mListView;
    private DialogListViewAdapter adapter;
    private ArrayList<CmdStatus> alist = new ArrayList<>();
    private View view;
    public static synchronized FloatWindowUtil getInstance() {
        return instance;
    }

    private final String TAG = "FloatWindowUtil";
    public void initFloatWindow(Context mContext) {
        view = View.inflate(BaseApp.getInstance(), R.layout.dialog_listview, null);
        view.setFocusableInTouchMode(true);
        mListView = (RecyclerView) view.findViewById(R.id.listview);
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.close).setOnClickListener(new DoubleClickListener (){

            @Override
            public void onMultiClick(View v) {
                alist.clear();
                FloatWindow.destroy(TAG);
            }
        });

        adapter = new DialogListViewAdapter(alist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(adapter);
    }

    public void cleanList() {
        if (adapter != null) {
            alist.clear();
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isShow() {
        if (FloatWindow.get(TAG) == null) {
            return false;
        }
       return FloatWindow.get(TAG).isShowing();
    }

    public void show (){
        if (FloatWindow.get(TAG) == null) {
            FloatWindow.with(BaseApp.getInstance())
                    .setView(view)
                    .setWidth(Screen.width,0.4f)
                    .setHeight(Screen.height,0.3f)
                    .setX(Screen.width,0.5f)
                    .setY(Screen.height,0.5f)
                    .setDesktopShow(true)
                    .setFilter(true, MainActivity.class, GroupStatusActivity.class)
                    .setMoveType(MoveType.active)
                    .setTag(TAG)
                    .build();
            FloatWindow.get(TAG).show();
        }else {
            if (!FloatWindow.get(TAG).isShowing()) {
                FloatWindow.get(TAG).show();
            }
        }
    }

    public void distory() {
        alist.clear();
        adapter.notifyDataSetChanged();
        FloatWindow.destroy(TAG);
    }

    public void onStatsuEvent(CmdStatus event) {
        if (event != null) {
            MessageSend.syncOptionInfo(event);
            int size = alist.size();
            boolean isHas = false;
            int postion = 0;
            for (int i = 0; i < size; i++) {
                CmdStatus status = alist.get(i);
                if (status.getControl_id() == event.getControl_id()) {
                    if (!TextUtils.isEmpty(event.getCmd_start())) {
                        status.setCmd_start(event.getCmd_start());
                        status.setCmd_end("");
                        status.setCmd_read_start("");
                        status.setCmd_read_middle("");
                        status.setCmd_read_end("");
                    }
                    if (!TextUtils.isEmpty(event.getCmd_end())) {
                        status.setCmd_end(event.getCmd_end());
                    }

                    if(!TextUtils.isEmpty(event.getCmd_read_start())) {
                        status.setCmd_read_start(event.getCmd_read_start());
                    }

                    if (!TextUtils.isEmpty(event.getCmd_read_middle())) {
                        status.setCmd_read_middle(event.getCmd_read_middle());
                    }

                    if (!TextUtils.isEmpty(event.getCmd_read_end())) {
                        status.setCmd_read_end(event.getCmd_read_end());
                    }
                    isHas = true;
                    postion = i;
                }
            }
            if (!isHas) {
                alist.add(event);
            }

            if (adapter != null && mListView != null) {
                adapter.notifyDataSetChanged();
                mListView.scrollToPosition(postion);
            }

        }
    }
}
