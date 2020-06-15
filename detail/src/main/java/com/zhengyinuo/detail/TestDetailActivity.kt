package com.zhengyinuo.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhengyinuo.froute_annotation.FRoute

@FRoute(path = "/detail/detail")
class TestDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_detail)
    }
}