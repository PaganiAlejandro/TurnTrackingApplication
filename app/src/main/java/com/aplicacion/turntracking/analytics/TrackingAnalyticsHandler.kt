package com.aplicacion.turntracking.analytics

import android.app.Application
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.security.AccessControlContext

class TrackingAnalyticsHandler(): TrackingAnalyticsLogger {

    override fun logNewUser(context: Context) {
        val analytics = FirebaseAnalytics.getInstance(context)
        val bundle = Bundle()
        bundle.putString("mensaje", "Nuevo inicio secion de usuario")
        analytics.logEvent("Login",bundle)
    }

}