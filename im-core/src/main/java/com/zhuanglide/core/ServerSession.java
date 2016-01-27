package com.zhuanglide.core;

import java.io.Serializable;

/**
 * client session
 * Created by wwj on 16.1.26.
 */
public class ServerSession implements Serializable{
    private long cid;
    private int sid;
    private long userId;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {

        this.userId = userId;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
