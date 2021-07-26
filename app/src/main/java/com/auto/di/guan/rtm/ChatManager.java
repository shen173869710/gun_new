package com.auto.di.guan.rtm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.auto.di.guan.BaseApp;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.UserStatusEvent;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.SendMessageOptions;

public class ChatManager {
    private static final String TAG = ChatManager.class.getSimpleName();

    private Context mContext;
    private RtmClient mRtmClient;
    private List<RtmClientListener> mListenerList = new ArrayList<>();

    public ChatManager(Context context) {
        mContext = context;
    }

    public void init() {
        String appID = "2aa0369d8f7e48d2b993df37ca325897";
        try {
            mRtmClient = RtmClient.createInstance(mContext, appID, new RtmClientListener() {
                @Override
                public void onConnectionStateChanged(int state, int reason) {
                    for (RtmClientListener listener : mListenerList) {
                        listener.onConnectionStateChanged(state, reason);
                    }
                }

                @Override
                public void onMessageReceived(RtmMessage rtmMessage, String peerId) {
//                    if (mListenerList.isEmpty()) {
//                        mMessagePool.insertOfflineMessage(rtmMessage, peerId);
//                    } else {
//                        for (RtmClientListener listener : mListenerList) {
//                            listener.onMessageReceived(rtmMessage, peerId);
//                        }
//                    }
                    LogUtils.e(TAG, "onMessageReceived11   peerid = "+peerId + "      message" +rtmMessage.getText());
                    LogUtils.e(TAG, "user =" + BaseApp.getUser());
//                    LogUtils.e(TAG, "parentId =" + BaseApp.getUser().getMemberId()+ " 是否相等 =="+peerId.equals(BaseApp.getUser().getMemberId().toString()));

//                    LogUtils.e(TAG, "parentId =" + BaseApp.getUser().getMemberId()+ " 是否相等 =="+peerId.equals(BaseApp.getUser().getMemberId().toString()));
                    if (!TextUtils.isEmpty(peerId)) {
                        String parentId = BaseApp.getUser().getMemberId().toString();
                        LogUtils.e(TAG, "parentId =" + BaseApp.getUser().getMemberId()+ " 是否相等 =="+peerId.equals(BaseApp.getUser().getMemberId().toString()));
//                        if (peerId.equals(parentId)) {
                            BaseApp.setWebLogin(true);
                        BaseApp.getUser().setMemberId(Long.valueOf(peerId));
                            MessageParse.praseData(rtmMessage.getText(), peerId);
//                        }
                    }
                }

                @Override
                public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {
                    if (mListenerList.isEmpty()) {
                        // If currently there is no callback to handle this
                        // message, this message is unread yet. Here we also
                        // take it as an offline message.
                    } else {
                        for (RtmClientListener listener : mListenerList) {
                            listener.onImageMessageReceivedFromPeer(rtmImageMessage, peerId);
                        }
                    }
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
                public void onPeersOnlineStatusChanged(Map<String, Integer> status) {
                    LogUtils.e(TAG, "onPeersOnlineStatusChanged" + status.toString());
                    for (String key : status.keySet()) {
                        int code = status.get(key);
                        EventBus.getDefault().post(new UserStatusEvent(key, code));
                        if (code == 0) {
                            LogUtils.e(TAG, "  当前用户ID  peerId = " + key + " 当前状态在线 ");
                        }else {
                            LogUtils.e(TAG, "  当前用户ID  peerId = " + key + " 当前状态离线 ");
                        }
                    }

                }
            });
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtm sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        // Global option, mainly used to determine whether
        // to support offline messages now.
    }

    public RtmClient getRtmClient() {
        return mRtmClient;
    }

    /**
     *    用户登录
     */
    public void doLogin() {
        mRtmClient.login(null, BaseApp.getUser().getUserId().toString(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                LogUtils.e(TAG, "login success");
                Set<String> user = new HashSet<>();
                user.add(BaseApp.getUser().getMemberId().toString());
                addPeersOnlineStatusListen(user);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                LogUtils.e(TAG, "login failed: " + errorInfo.getErrorCode());
            }
        });
    }


    public void addPeersOnlineStatusListen(Set<String> user) {
        mRtmClient.subscribePeersOnlineStatus(user, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtils.e(TAG, "subscribePeersOnlineStatus: onSuccess" );
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                LogUtils.e(TAG, "subscribePeersOnlineStatus: onFailure" +errorInfo.getErrorDescription());
            }
        });
    }


    public void doLogout() {
        mRtmClient.logout(null);
        MessageUtil.cleanMessageListBeanList();
    }



    public void sendPeerMessage( String content) {
        if (!BaseApp.isWebLogin()) {
            LogUtils.i(TAG, "web端未登录, 不发送消息");
            return;
        }
        final RtmMessage message = mRtmClient.createMessage();
        message.setText(content);
        SendMessageOptions option = new SendMessageOptions();
        option.enableOfflineMessaging = false;

        LogUtils.e(TAG,"BaseApp.getUser().getMemberId().toString()=" + BaseApp.getUser().getMemberId().toString() );
        mRtmClient.sendMessageToPeer(BaseApp.getUser().getMemberId().toString(), message, option, new ResultCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                LogUtils.e(TAG, "sendPeerMessage : onSuccess");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Entiy.onPeerError(TAG, errorInfo.getErrorCode());
            }
        });
    }

}
