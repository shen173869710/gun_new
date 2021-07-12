package com.auto.di.guan.rtm;


import com.auto.di.guan.utils.LogUtils;

import java.util.Map;

import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;

public class MyRtmClientListener implements RtmClientListener {

    private final String TAG = "MyRtmClientListener";

    @Override
    public void onConnectionStateChanged(final int state, int reason) {
            switch (state) {
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                    LogUtils.e(TAG, "CONNECTION_STATE_RECONNECTING");

                    break;
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:

                    LogUtils.e(TAG, "account_offline");

                    break;
            }
    }

    @Override
    public void onMessageReceived(final RtmMessage message, final String peerId) {

        LogUtils.e(TAG, "message = " + message  + "peerId =  "+peerId);

    }

    @Override
    public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {

    }

    @Override
    public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

    }

    @Override
    public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onPeersOnlineStatusChanged(Map<String, Integer> map) {
        for (String key : map.keySet()) {
            LogUtils.e(TAG, "key = " + key  + "peerId =  "+map.get(key));

        }
    }
}
