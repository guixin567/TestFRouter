package com.zhengyinuo.froute_api;

import com.zhengyinuo.froute_annotation.FRouteMeta;

import java.util.HashMap;

public interface IRoutePath {
    public void loadInfo(HashMap<String, FRouteMeta> routeMap);
}
