package com.shengwang.meeting.demo

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.shengwang.scene.FcrUISceneConfig
import cn.shengwang.scene.FcrUISceneCreator
import cn.shengwang.scene.FcrUISceneCreatorConfig
import com.permissionx.guolindev.PermissionX
import io.agora.agoracore.core2.bean.FcrUserRole

class MainActivity : AppCompatActivity() {
    // 填写项目的 App ID，可在声网控制台中生成
    private val appId = "47b7535dcb9a4bb4aa592115266eae98"

    // 填写频道名
    private val roomId = "351975881"

    // 填写声网控制台中生成的临时 Token
    private val roomToken =
        "007eJxTYAhb5R7+mZuxO6NoG1ffAeY9UcEtTwx0lzbniThJrtglvluBwcQ8ydzU2DQlOcky0SQpySQx0dTSyNDQ1MjMLDUx1dLC8rxAekMgI4P3v/qfzAxMDIxACOIrMFikGlgYJxobWloaJaVYpBglmhoZpyYbJVqaphlbmltYsDIwAFUZWyaZmBonJZoZJKeZJqYlJieZmxmnJVskGRknmiYbGbHDTeRkMDY1tDQ3tbAwJGw4IwMAn8k5uQ=="
    private val userId = "1234"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun join(view: View) {
        PermissionX.init(this).permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    val creatorConfig = FcrUISceneCreatorConfig(appId, userId)
                    val userName = "Agroa" //用户名称
                    val userRole = FcrUserRole.HOST //角色
                    val sceneConfig = FcrUISceneConfig(roomToken, roomId, userName, userRole, "")
                    FcrUISceneCreator.init(creatorConfig)
                    FcrUISceneCreator.launch(this, sceneConfig)
                } else {
                    Toast.makeText(this@MainActivity, "no permissions", Toast.LENGTH_SHORT).show()
                }
            }
    }
}