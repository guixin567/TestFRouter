package com.zhengyinuo.froute_api

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.zhengyinuo.froute_annotation.FRouteMeta
import com.zhengyinuo.froute_api.util.ClassUtils
import java.util.*

class FRouter {
    private var path: String? = null

    companion object {
        var mContext:Application? = null
        var mHandler:Handler? = null
        val instance by lazy { FRouter() }
        val routeMap = HashMap<String, FRouteMeta>()



        fun init(application: Application){
            //加载到方法
            mContext = application
            mHandler = Handler(Looper.getMainLooper())
            //从DEX文件获取到自动生成的类，并调用方法加载到内存中
            val routePathClassNames = ClassUtils.getFileNameByPackageName(mContext, FConfig.IRoutePathPackage)
            routePathClassNames.filter { it.startsWith(FConfig.IRoutePathPackage + FConfig.prefixForPath) }.forEach {
                (Class.forName(it).getConstructor().newInstance() as? IRoutePath)?.loadInfo(routeMap)
            }
        }
    }

    fun withPath(path: String): FRouter {
        this.path = path
        return this
    }

    fun navigation() {
        val clazz = routeMap[path]?.destination ?: return
        val intent = Intent(mContext, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        runInMainThread(runnable = Runnable {
            mContext?.startActivity(intent)
        })

    }

    private fun runInMainThread(runnable: Runnable){
       if(Looper.getMainLooper().thread != Thread.currentThread()) {
           mHandler?.post(runnable)
       }else{
           runnable.run()
       }
    }

}