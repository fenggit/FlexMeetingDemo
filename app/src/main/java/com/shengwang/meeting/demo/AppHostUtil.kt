package com.shengwang.meeting.demo

import io.agora.core.common.http.bean.FcrRegion
import io.agora.core.common.http.bean.FcrRegionUtils
import io.agora.core.common.http.utils.FcrHttpEnv

/**
 * date : 2022/9/13
 * description : Demo层域名
 */
object AppHostUtil {

    /**
     * 课前域名
     */
    fun getAppHostUrl(): String {
        return String.format(FcrHttpEnv.PROD.appHost, FcrRegionUtils.getAppRegion(FcrRegion.CN.value))
    }
}