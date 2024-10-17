package com.shengwang.meeting.demo

import android.app.Application
import io.agora.agoracore.core2.utils.FcrSDKInitUtils

/**
 * author : qinwei@agora.io
 * date : 2024/9/30 14:38
 * description :
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FcrSDKInitUtils.initSDK(this)
    }
}