package com.auto.di.guan.rtm;

import com.auto.di.guan.utils.LogUtils;

import java.util.List;

import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMessage;

/**
 *      用户加入房间监听
 */
public class MyChannelListener implements RtmChannelListener {

    private String TAG  = "MyChannelListener";
    @Override
    public void onMemberCountUpdated(int i) {

    }

    @Override
    public void onAttributesUpdated(List<RtmChannelAttribute> list) {

    }

    @Override
    public void onMessageReceived(final RtmMessage message, final RtmChannelMember fromMember) {
            String account = fromMember.getUserId();
            LogUtils.e(TAG, "onMessageReceived account = " + account + " msg = " + message);
            MessageBean messageBean = new MessageBean(account, message, false);
    }

    @Override
    public void onImageMessageReceived(final RtmImageMessage rtmImageMessage, final RtmChannelMember rtmChannelMember) {
            String account = rtmChannelMember.getUserId();
            LogUtils.e(TAG, "onMessageReceived account = " + account + " msg = " + rtmImageMessage);
            MessageBean messageBean = new MessageBean(account, rtmImageMessage, false);

    }

    @Override
    public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onMemberJoined(RtmChannelMember member) {
        LogUtils.e(TAG, "onMemberJoined member = " +member.getUserId());
    }

    @Override
    public void onMemberLeft(RtmChannelMember member) {
        LogUtils.e(TAG, "onMemberLeft member = " +member.getUserId());
    }
}
