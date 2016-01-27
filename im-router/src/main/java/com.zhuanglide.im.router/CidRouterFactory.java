package com.zhuanglide.im.router;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.zhuanglide.core.ClientSession;
import com.zhuanglide.util.Utils;

import java.util.List;
import java.util.Random;

/**
 * 根据cid转发
 * Created by wwj on 16.1.26.
 */
public class CidRouterFactory extends AbstractLoadBalance {
    private Random random = new Random();
    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        Object[] args = invocation.getArguments();
        //约定第一个参数为cid
        try {
            long cid = (null == args || args.length < 1) ? null : (Long) args[0];
            if (cid > 0) {
                String ip = Utils.ipv4Long2String(cid);
                for (int i = 0; i < invokers.size(); i++) {
                    if (ip.equals(invokers.get(i).getUrl().getIp())) {
                        return invokers.get(i);
                    }
                }
            }
        } catch (Exception e) {
        }
        return invokers.get(random.nextInt(invokers.size()));
    }
}
