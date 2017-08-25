package com.wxsk.vr.mine.service;

import com.wxsk.vr.mine.service.remote.IJpushServiceRemote;

/**
 * 极光推送
 */
public interface JpushService extends IJpushServiceRemote{
	public void sendDigFinished();
}
