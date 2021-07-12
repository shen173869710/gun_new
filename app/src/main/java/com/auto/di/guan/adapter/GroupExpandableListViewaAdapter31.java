package com.auto.di.guan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.fragment.FragmentTab31;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupList;
import com.auto.di.guan.fragment.FragmentTab5;
import com.auto.di.guan.utils.GlideUtil;
import com.auto.di.guan.utils.NoFastClickUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */

public class GroupExpandableListViewaAdapter31 extends BaseExpandableListAdapter {
    private Context context;
    private List<GroupList> groupLists;//组列表
    private FragmentTab5 fragmentTab5;
    public GroupExpandableListViewaAdapter31(Context a, List<GroupList> groupArray, FragmentTab5 fragmentTab5) {
        this.groupLists = groupArray;
        this.context = a;
        this.fragmentTab5 = fragmentTab5;
    }

    public void setData(List<GroupList> groupLists) {
        this.groupLists = groupLists;
        notifyDataSetChanged();
    }
    /*-----------------Child */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return groupLists.get(groupPosition).controlInfos;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expand_group_child_item, null);
            holder.group_item_name = (TextView) convertView.
                    findViewById(R.id.group_item_name);
            holder.group_item_type = (TextView) convertView.
                    findViewById(R.id.group_item_type);
            holder.group_item_status = (TextView) convertView.
                    findViewById(R.id.group_item_status);
            holder.group_item_icon = (ImageView) convertView.
                    findViewById(R.id.group_item_icon);
            convertView.setTag(holder);
        }else {
            holder = (ChildHolder) convertView.getTag();
        }
        bindChildView(holder, groupPosition, childPosition);
        return convertView;
    }

    private void bindChildView(final ChildHolder holder, final int groupPosition, int childPosition) {
        ControlInfo controlInfo = groupLists.get(groupPosition).controlInfos.get(childPosition);
        holder.group_item_name.setText(controlInfo.getValveName()+" 号控制阀");
//        holder.group_item_icon.setImageResource(Entiy.getImageResource(controlInfo.getValveStatus()));
        GlideUtil.loadControlExpand(context, holder.group_item_icon, controlInfo);
        holder.group_item_type.setText(controlInfo.getValveAlias());
    }



    @Override
    public int getChildrenCount(int groupPosition) {
        return groupLists.get(groupPosition).controlInfos.size();
    }

    /* ----------------------------Group */
    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupLists.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expand_list_group_item, null);
            holder.expand_list_group_num = (TextView) convertView.findViewById(R.id.expand_list_group_num);
            holder.expand_list_group_time = (TextView) convertView.findViewById(R.id.expand_list_group_time);
            holder.expand_list_group_state = (TextView) convertView.findViewById(R.id.expand_list_group_state);
            holder.expand_list_group_icon = (ImageView) convertView.findViewById(R.id.expand_list_group_icon);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            holder.expand_list_group_icon.setBackgroundResource(R.mipmap.expand_group_p);
        } else {
            holder.expand_list_group_icon.setBackgroundResource(R.mipmap.expand_group_n);
        }

        bindGroupView(holder, groupPosition);
        return convertView;
    }

    private void bindGroupView(GroupHolder holder, final int groupPosition) {
        holder.expand_list_group_num.setText("第 "+groupLists.get(groupPosition).groupInfo.getGroupName()+"组");
        if(groupLists.get(groupPosition).groupInfo.getGroupStatus() == 0) {
            holder.expand_list_group_state.setText("开启轮灌");
            holder.expand_list_group_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NoFastClickUtils.isFastClick()){
                        return;
                    }
                    fragmentTab5.startWork(groupLists.get(groupPosition).groupInfo);
                }
            });
        }else {
            holder.expand_list_group_state.setText("关闭轮灌");
            holder.expand_list_group_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NoFastClickUtils.isFastClick()){
                        return;
                    }
                    fragmentTab5.startWork(groupLists.get(groupPosition).groupInfo);
                }
            });
        }

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupHolder {
        TextView expand_list_group_num;
        TextView expand_list_group_time;
        TextView expand_list_group_state;
        ImageView expand_list_group_icon;
    }

    public class ChildHolder {
        TextView group_item_name;
        TextView group_item_type;
        TextView group_item_status;
        ImageView group_item_icon;

    }

}
