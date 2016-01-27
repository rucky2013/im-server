package com.zhuanglide.core.service;

/**
 * Created by wwj on 16.1.26.
 */
public interface BrokerService {
    void push(long cid,int sid, String msg);
}
