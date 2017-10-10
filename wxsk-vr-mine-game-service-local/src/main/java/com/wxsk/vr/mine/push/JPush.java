package com.wxsk.vr.mine.push;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.List;

// https://docs.jiguang.cn/jpush/server/push/rest_api_v3_push/
//https://docs.jiguang.cn/jpush/server/3rd/java_sdk/
public class JPush {
    public static final String APP_KEY = "f45379cb60be7098f6f84a6a";
    public static final String MASTER_SECRET = "fdda81381d6ee53ed196c118";

    /**
     * 快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
     *
     * @return
     */
    public static PushPayload buildPushObject_all_all_alert(String alert) {
        return PushPayload.alertAll(alert);
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT。
     *
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String alert, String alias) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(alert)).build();
    }

    /**
     * 构建推送对象：所有平台，推送目标是registrationId为 "regid"，通知内容为 ALERT。
     *
     * @return
     */
    public static PushPayload buildPushObject_all_registrationid_alert(List<String> registrationids, String alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationids))
                .setNotification(Notification.alert(alert))
                .build();
    }

    /**
     * 构建推送对象：平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
     *
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String alert, String title) {
        return PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(alert, title, null)).build();
    }

    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String alert, String msg_content) {
        return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).setBadge(5)
                                .setSound("happy").addExtra("from", "JPush").build())
                        .build())
                .setMessage(Message.content(msg_content))
                .setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
    }

    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String msg_content, List<String> aliases) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.alias(aliases))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }

    public static void main(String[] args) {
        ClientConfig config = ClientConfig.getInstance();
        JPushClient JPUSHCLIENT = new JPushClient(JPush.MASTER_SECRET, JPush.APP_KEY, null, config);
        PushPayload payload = JPush.buildPushObject_all_alias_alert("mmmmmmmmmm", "13165ffa4e3d108423f");
        try {
            JPUSHCLIENT.sendPush(payload);
        } catch (APIConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (APIRequestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
