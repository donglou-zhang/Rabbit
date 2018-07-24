package com.rpc2.zl.rpc.common.utils;

import com.google.common.collect.Lists;
import com.rpc2.zl.common.constant.ConstantConfig;
import com.rpc2.zl.rpc.filter.ActiveFilter;
import com.rpc2.zl.rpc.filter.RpcFilter;

import java.util.*;

/**
 * Created by Vincent on 2018/7/16.
 */
public class ActiveFilterUtil {

    private static List<Object> getActiveFilter() {
        List<Object> rpcFilterList = Lists.newArrayList();
        Map<String, Object> rpcFilterMapObject = ApplicationContextUtils.getApplicationContext().getBeansWithAnnotation(ActiveFilter.class);
        if(null != rpcFilterMapObject) {
            rpcFilterList = Lists.newArrayList(rpcFilterMapObject.values());
            Collections.sort(rpcFilterList, new Comparator<Object>(){
                @Override
                public int compare(Object o1, Object o2) {
                    ActiveFilter a1 = (ActiveFilter) o1;
                    ActiveFilter a2 = (ActiveFilter) o2;
                    return a1.order() > a2.order() ? 1 : -1;
                }
            });
        }
        return rpcFilterList;
    }

    public static Map<String, RpcFilter> getFilterMap(boolean isServer) {
        Map<String, RpcFilter> filterMap = new HashMap<>();
        List<Object> rpcFilterList = getActiveFilter();
        for(Object rpcFilterBean : rpcFilterList) {
            Class<?>[] interfaces = rpcFilterBean.getClass().getInterfaces();
            ActiveFilter activeFilter = rpcFilterBean.getClass().getAnnotation(ActiveFilter.class);
            String includeFilterGroupName = !isServer ? ConstantConfig.CONSUMER : ConstantConfig.PROVIDER;
            // 如果当前的client端filter不包含CONSUMER关键字或者server端的不包含PROVIDER关键字，则跳过该filter
            if(null != activeFilter.group() && Arrays.stream(activeFilter.group()).filter(p->p.contains(includeFilterGroupName)).count()==0) {
                continue;
            }
            for(Class<?> clazz : interfaces) {
                // filterBean可能实现了其他接口
                if(clazz.isAssignableFrom(RpcFilter.class)) {
                    filterMap.put(rpcFilterBean.getClass().getName(), (RpcFilter) rpcFilterBean);
                }
            }
        }
        return filterMap;
    }
}
