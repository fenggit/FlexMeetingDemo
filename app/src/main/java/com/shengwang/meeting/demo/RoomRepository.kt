package com.shengwang.meeting.demo

import cn.shengwang.http.bean.FcrSceneCreateRoomReq
import cn.shengwang.http.bean.FcrSceneJoinRoomReq
import io.agora.agoracore.core2.bean.FcrUserRole
import io.agora.agoracore.core2.utils.FcrHashUtil
import io.agora.core.common.http.app.AppHostUtil
import io.agora.core.common.http.core.HttpIgnoreCer
import io.agora.core.common.utils.GsonUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit

object RoomRepository {
    interface OnRoomListener {
        fun onLoading()
        fun onSuccess(
            appId: String, roomToken: String, roomId: String, userId: String, username: String, userRole: FcrUserRole
        )

        fun onFailure(msg: String)
    }

    fun loadRoomInfo(scope: CoroutineScope, listener: OnRoomListener) {
        var appId = ""
        var roomId: String
        var token = ""
        val username = "agora"
        val userRole = FcrUserRole.HOST
        val userId = FcrHashUtil.md5(username + userRole).lowercase()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            // 处理未捕获的异常
            throwable.printStackTrace()
            listener.onFailure(throwable.message ?: "")
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .sslSocketFactory(HttpIgnoreCer.createSslSocketFactory(), HttpIgnoreCer.createTrustManager())
            .hostnameVerifier { _, _ -> true }.build()
        val baseUrl: String = AppHostUtil.getAppHostUrl()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonUtil.getGsonX()))
            .build()
        val roomService = retrofit.create(RoomService::class.java)
        scope.launch(exceptionHandler) {
            val roomName = "MyRoom" + System.currentTimeMillis()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + 60 * 1000 * 45                     // 45 minutes
            val createRoomReq = FcrSceneCreateRoomReq(roomName, startTime, endTime)
            listener.onLoading()
            val createResult = roomService.addRoom(createRoomReq)
            if (createResult.code == 0) {
                roomId = createResult.data.roomId
            } else {
                throw CancellationException(createResult.error)
            }
            //do load room
            val joinRoomReq = FcrSceneJoinRoomReq(roomId, userRole.value, userId, username)
            val room = roomService.loadRoom(joinRoomReq)
            if (room.code == 0) {
                room.data?.let {
                    appId = it.appId
                    token = it.token
                }
            } else {
                throw CancellationException(room.error)
            }
            listener.onSuccess(
                appId = appId,
                roomToken = token,
                roomId = roomId,
                userId = userId,
                username = username,
                userRole = userRole
            )
        }
    }
}