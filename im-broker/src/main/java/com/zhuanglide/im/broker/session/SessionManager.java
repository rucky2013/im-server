package com.zhuanglide.im.broker.session;

import com.zhuanglide.core.ClientSession;
import com.zhuanglide.util.Utils;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wwj on 16.1.26.
 */
public class SessionManager {
    private Map<Integer, ClientSession> sessionMap = Collections.synchronizedMap(new HashMap<Integer, ClientSession>());
    private AtomicInteger atomicInteger = new AtomicInteger();
    public static final AttributeKey<Integer> SESSION_KEY = AttributeKey.newInstance("SID");

    public static long clientId = 0;
    /**
     * 保存session
     * @param channel
     */
    public synchronized void addSession(Channel channel) {
        int sid = atomicInteger.getAndIncrement();
        if (sid < 0) {
            atomicInteger.set(1);
            sid = 0;
        }
        channel.attr(SESSION_KEY).set(sid);
        ClientSession session = new ClientSession();
        session.setSid(sid);
        session.setChannel(channel);
        sessionMap.put(sid,session);
    }

    public ClientSession getSession(int sid) {
        return sessionMap.get(sid);
    }

    public void removeSession(Channel channel) {
        sessionMap.remove(channel.attr(SESSION_KEY).get());
    }

    public ClientSession getSession(Channel channel) {
        return sessionMap.get(channel.attr(SESSION_KEY).get());
    }

    public long getClientId(){
        if (clientId < 1) {
            synchronized (SessionManager.class) {
                if(clientId < 1){
                    try {
                        clientId = Utils.ipv4String2long(Inet4Address.getLocalHost().getHostAddress());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return clientId;

    }


}
