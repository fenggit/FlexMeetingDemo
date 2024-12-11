package com.shengwang.meeting.demo

import android.app.Application
import cn.shengwang.scene.FcrUISceneCreator
import io.agora.agoracore.core2.utils.FcrSDKInitUtils
import io.agora.core.common.helper.SPreferenceManager
import io.agora.core.common.http.utils.FcrHttpEnv
import io.agora.core.common.utils.CommonConstants

/**
 * author : qinwei@agora.io
 * date : 2024/9/30 14:38
 * description :
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FcrSDKInitUtils.initSDK(this)
        // 临时测试
        SPreferenceManager.putObject(CommonConstants.KEY_SP_ENV, FcrHttpEnv.PRE)
    }
}