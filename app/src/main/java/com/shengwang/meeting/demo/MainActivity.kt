package com.shengwang.meeting.demo

import android.Manifest
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import cn.shengwang.scene.FcrUIExitReason
import cn.shengwang.scene.FcrUISceneConfig
import cn.shengwang.scene.FcrUISceneCreator
import cn.shengwang.scene.FcrUISceneCreatorConfig
import cn.shengwang.scene.FcrUISceneObserver
import com.permissionx.guolindev.PermissionX
import io.agora.agoracore.core2.bean.FcrUserRole
import io.agora.core.common.obs.FcrError

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "FlexMeetingDemo"
    }

    private val mFcrUISceneObserver = object : FcrUISceneObserver {
        override fun onLaunchSuccess(roomId: String) {
            super.onLaunchSuccess(roomId)
            Log.i(TAG, "onLaunchSuccess roomId:$roomId")
        }

        override fun onLaunchFailure(roomId: String, error: FcrError) {
            super.onLaunchFailure(roomId, error)
            Log.e(TAG, "onLaunchFailure roomId:$roomId error:$error")
        }

        override fun onExited(reason: FcrUIExitReason) {
            super.onExited(reason)
            Log.e(TAG, "onExited  reason $reason")
        }

        override fun onLaunchCancel(roomId: String) {
            super.onLaunchCancel(roomId)
            Log.e(TAG, "onLaunchCancel roomId:$roomId")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        FcrUISceneCreator.addObserverOfUIScene(mFcrUISceneObserver)
    }

    fun onJoinClick(view: View) {
        //进会需要录音和相机权限
        PermissionX.init(this).permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
            .request { allGranted, _, _ ->
                if (allGranted) {
                    doJoin()
                } else {
                    Toast.makeText(this@MainActivity, "no permissions", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun doJoin() {
        val progressDialog = ProgressDialog(this).apply {
            this.setMessage("获取房间信息")
            this.setCancelable(false)
        }
        RoomRepository.loadRoomInfo(lifecycleScope, object : RoomRepository.OnRoomListener {
            override fun onLoading() {
                progressDialog.show()
            }

            override fun onSuccess(
                appId: String,
                roomToken: String,
                roomId: String,
                userId: String,
                username: String,
                userRole: FcrUserRole
            ) {
                progressDialog.dismiss()
                goMeeting(appId, roomToken, roomId, userId, username, userRole)
            }

            override fun onFailure(msg: String) {
                progressDialog.dismiss()
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * TODO 所有参数请使用自己项目的数据，此数据仅用于测试
     */
    fun goMeeting(
        appId: String, roomToken: String, roomId: String, userId: String, username: String, userRole: FcrUserRole
    ) {
        val creatorConfig = FcrUISceneCreatorConfig(appId, userId)
        val sceneConfig = FcrUISceneConfig(roomId, roomToken, username, userRole, "")
        FcrUISceneCreator.init(creatorConfig)
        FcrUISceneCreator.launch(this, sceneConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        FcrUISceneCreator.removeObserverOfUIScene(mFcrUISceneObserver)
    }
}