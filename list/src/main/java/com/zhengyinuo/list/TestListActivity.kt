package com.zhengyinuo.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhengyinuo.froute_annotation.FRoute
import com.zhengyinuo.froute_api.FRouter
import com.zhengyinuo.froute_api.FRouter1
import kotlinx.android.synthetic.main.activity_test_list.*
@FRoute(path = "/list/list")
class TestListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_list)
        bt_detail.setOnClickListener {
//            startActivity(Intent(this,Class.forName("com.zhengyinuo.detail.TestDetailActivity")))
            FRouter.instance.withPath("/detail/detail").navigation()
//            FRouter1.instance.withPath("/detail/detail").navigation()
        }
    }
}