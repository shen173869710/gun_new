package com.auto.di.guan;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.adapter.GroupStatusAdapter;
import com.auto.di.guan.adapter.StatusAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.dialog.GroupOptionDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.ActivityEvent;
import com.auto.di.guan.event.AutoCountEvent;
import com.auto.di.guan.event.AutoTaskEvent;
import com.auto.di.guan.event.GroupStatusEvent;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.rtm.MessageEntiy;
import com.auto.di.guan.rtm.MessageSend;
import com.auto.di.guan.utils.DiffStatusCallback;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 *   轮灌设置
 */
public class GroupStatusActivity extends FragmentActivity  {
    private View view;
    private TextView textView;
    private TextView title_bar_status;
    private RecyclerView recyclerView;
    private List<GroupInfo> groupInfos = new ArrayList<>();
    private GroupStatusAdapter adapter;

    private View group_status_view;

//    private StatusAdapter openAdapter;
//    private RecyclerView openList;
//    private StatusAdapter closeAdapter;
//    private RecyclerView closeList;
//    private List<ControlInfo> openInfos = new ArrayList<>();
//    private List<ControlInfo> closeInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_status_layout);
        view = findViewById(R.id.title_bar);
        EventBus.getDefault().register(this);
        groupInfos = GroupInfoSql.queryGroupSettingList();

        textView = (TextView) view.findViewById(R.id.title_bar_title);
        textView.setText("自动轮灌状态");

        title_bar_status = (TextView) view.findViewById(R.id.title_bar_status);
        title_bar_status.setVisibility(View.VISIBLE);
        title_bar_status.setText("轮灌操作");
        title_bar_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                GroupOptionDialog.ShowDialog(GroupStatusActivity.this, "自动轮灌操作", new GroupOptionDialog.ItemClick() {
                    @Override
                    public void onItemClick(int index) {
                        groupOption(index);
                    }
                });
            }
        });
        view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupStatusActivity.this.finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.group_option_view);
        adapter = new GroupStatusAdapter(groupInfos);
        adapter.setDiffCallback(new DiffStatusCallback());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        group_status_view = findViewById(R.id.group_status_view);
        if (BaseApp.isWebLogin()) {
            group_status_view.setVisibility(View.VISIBLE);
            group_status_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else {
            group_status_view.setVisibility(View.GONE);
        }
    }

    /**
     *        index == 1 开启轮灌
     *        index == 2 关闭轮灌
     *        index == 3 自动查询
     * @param index
     */
    public void groupOption(int index) {
        if (index == 1) {
            if (groupInfos != null && groupInfos.size() > 0) {
                if (GroupInfoSql.queryOpenGroupList() != null) {
                    ToastUtils.showLongToast("自动轮灌正在运行当中, 无法再次开启自动轮灌");
                    return;
                }
                PollingUtils.stopPollingService(GroupStatusActivity.this);
                TaskFactory.createAutoGroupOpenTask(groupInfos.get(0));
                TaskManager.getInstance().startTask();
            }
        }else if (index == 2) {
            groupInfos = GroupInfoSql.queryGroupSettingList();
            for (int i = 0; i < groupInfos.size(); i++) {
                GroupInfo info = groupInfos.get(i);
                info.setGroupRunTime(0);
                info.setGroupTime(0);
                info.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
            }
            GroupInfoSql.updateGroupList(groupInfos);
            PollingUtils.stopPollingService(GroupStatusActivity.this);
            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_FINISH));

        }else if (index == 3) {
            if(!TaskManager.getInstance().hasTask()) {
                ToastUtils.showLongToast("轮灌操作执行当中,请稍后操作");
                PollingUtils.stopPollingService(GroupStatusActivity.this);
                return;
            }

            if(PollingUtils.isIsStart()) {
                PollingUtils.stopPollingService(GroupStatusActivity.this);
            }else {
                PollingUtils.startPollingService(GroupStatusActivity.this, Entiy.ALERM_TIME);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoTaskEvent(AutoTaskEvent event) {
        if (event.getType() == Entiy.RUN_DO_FINISH) {
            LogUtils.e("GroupStatusActivity", "自动轮灌结束");
            groupInfos = GroupInfoSql.queryGroupSettingList();
            adapter = new GroupStatusAdapter(groupInfos);
            recyclerView.setAdapter(adapter);
        }
        if (event.getGroupInfo() == null) {
            return;
        }
        if (event.getType() == Entiy.RUN_DO_STOP
        || event.getType() == Entiy.RUN_DO_START
                || event.getType() == Entiy.RUN_DO_NEXT
                || event.getType() == Entiy.RUN_DO_TIME
               ) {
            if (adapter != null) {
                onAutoCountEvent(new AutoCountEvent(event.getGroupInfo()));
            }
         }
    }



    /**
     *        自动轮灌时间同步
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoCountEvent(AutoCountEvent event) {
        if (adapter != null && event != null && event.getGroupInfo() != null) {
            GroupInfo groupInfo = event.getGroupInfo();
            int groupId = groupInfo.getGroupId();
            int size = groupInfos.size();
            int position = 0;
            for (int i = 0; i < size; i++) {
                if (groupId == groupInfos.get(i).getGroupId()) {
                    position = i;
                }
            }
            adapter.getData().set(position, groupInfo);
            adapter.notifyItemChanged(position, position);
//            if (openInfos != null && openInfos.size() == 0) {
//                GroupStatusEvent groupStatusEvent = new GroupStatusEvent(groupInfo);
//                onGroupStatusEvent(groupStatusEvent);
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityEvent(ActivityEvent event) {
        if (event.getIndex() == MessageEntiy.TYPE_ACTIVITY_STATUS_FINISH) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
