package com.aplicacion.turntracking.analytics

import android.content.Context

interface TrackingAnalyticsLogger {

    fun logNewUser(context: Context)
}