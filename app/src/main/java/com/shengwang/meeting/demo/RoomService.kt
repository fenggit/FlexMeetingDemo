package com.shengwang.meeting.demo

import cn.shengwang.http.bean.FcrSceneCreateRoomReq
import cn.shengwang.http.bean.FcrSceneCreateRoomRes
import cn.shengwang.http.bean.FcrSceneJoinRoomReq
import cn.shengwang.http.bean.FcrSceneJoinRoomRes
import io.agora.core.common.http.bean.HttpBaseRes
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * author : qinwei@agora.io
 * date : 2024/11/11 10:33
 * description :
 */
interface RoomService {
    /**
     * 创建房间(免登录)
     *
     * createRoomWithLogin
     */
    @POST("conference/companys/v1/rooms")
    suspend fun addRoom(@Body req: FcrSceneCreateRoomReq): HttpBaseRes<FcrSceneCreateRoomRes>

    /**
     * 加入房间(免登录)
     */
    @PUT("conference/companys/v1/rooms")
    suspend fun loadRoom(@Body req: FcrSceneJoinRoomReq): HttpBaseRes<FcrSceneJoinRoomRes>
}