package com.zhuanglide.core;

import io.netty.channel.Channel;

import java.io.Serializable;

/**
 * Created by wwj on 16.1.26.
 */
public class ClientSession implements Serializable{
    private int sid;
    private long userId;
    private Channel channel;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
