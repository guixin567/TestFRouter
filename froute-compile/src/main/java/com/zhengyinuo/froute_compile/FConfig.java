package com.zhengyinuo.froute_compile;

public interface FConfig {
    //注解处理器用到的全类名
    String FRoutePath = "com.zhengyinuo.froute_annotation.FRoute";

    //通过build.gradle传递过来的module名，用于区分module
    String ModuleName = "ModuleName";


    //生成类名的前缀
    String FRoute$$Group$$ = "FRoute$$Group$$";

    //生成方法名
    String loadInfo = "loadInfo";

    String routeMap = "routeMap";

    String packageName = "com.zhengyinuo.route";

    //api 包中 接口的全路径
    String IRoutePath = "com.zhengyinuo.froute_api.IRoutePath";
}
