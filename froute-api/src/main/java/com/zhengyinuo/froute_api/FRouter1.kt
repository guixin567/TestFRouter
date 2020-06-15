package com.zhengyinuo.froute_api

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.zhengyinuo.froute_annotation.FRouteMeta
import java.util.*

class FRouter1 {
    private var path: String? = null
    private var group: String? = null

    companion object {
        var mContext: Application? = null
        var mHandler: Handler? = null
        val instance by lazy { FRouter1() }

        val routeMap = HashMap<String, FRouteMeta>()

        fun init(application: Application) {
            //加载到方法
            mContext = application
            mHandler = Handler(Looper.getMainLooper())
        }
    }

    fun withPath(path: String): FRouter1 {
        this.path = path
        val secondSeparatorIndex = path.lastIndexOf("/")
        this.group = path.substring(1, secondSeparatorIndex)
        return this
    }

    fun navigation() {
        var clazz = routeMap[path]?.destination
        if (clazz == null) {
            val className = FConfig.IRoutePathPackage + FConfig.prefixForPath + group
            val iRoutePath = Class.forName(className).newInstance() as? IRoutePath
            iRoutePath?.loadInfo(routeMap)
            clazz = routeMap[path]?.destination
        }
        if(clazz == null) return
        val intent = Intent(mContext, clazz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        runInMainThread(runnable = Runnable {
            mContext?.startActivity(intent)
        })

    }

    private fun runInMainThread(runnable: Runnable) {
        if (Looper.getMainLooper().thread != Thread.currentThread()) {
            mHandler?.post(runnable)
        } else {
            runnable.run()
        }
    }

}