package com.zhuanglide.core.service;

import com.zhuanglide.core.ServerSession;

/**
 * Created by wwj on 16.1.26.
 */
public interface IMService {

    /**
     * 建立连接
     */
    void connect(long cid,int sid,long userId);

    /**
     * 发布
     * @param msg
     */
    void publish(long userId, String msg);

    /**
     * 删除连接
     */
    void disconnect(long userId);
}
