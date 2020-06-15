package com.zhengyinuo.testarouter1

import android.app.Application
import com.zhengyinuo.froute_api.FRouter
import com.zhengyinuo.froute_api.FRouter1

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FRouter.init(this)
//        FRouter1.init(this)
    }
}