package com.easywashing

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus


class DynamicFeatureManager private constructor(val context: Context) {

    private val splitInstallManager: SplitInstallManager =
        SplitInstallManagerFactory.create(context)

    companion object : SingletonHolder<DynamicFeatureManager, Context>(::DynamicFeatureManager) {

        @kotlin.jvm.JvmField
        var DYNAMIC_FEATURE_VIDEO_TOUR: String = "dynamicWashing"
    }

    fun launch(moduleName: String) {

        val request = SplitInstallRequest.newBuilder()
            .addModule(moduleName)
            .build()
        if (splitInstallManager.installedModules.contains(moduleName)) {
            onSuccessfulLoad(moduleName, launch = true)
        } else {
            splitInstallManager.registerListener(listener)
            splitInstallManager.startInstall(request)
        }

    }

    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->
        val multiInstall = state.moduleNames().size > 1
        state.moduleNames().forEach { name ->
            // Handle changes in state.
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    context.startIntentSender(state.resolutionIntent()?.intentSender, null, 0, 0, 0)
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    onSuccessfulLoad(name, launch = !multiInstall)
                }

                SplitInstallSessionStatus.FAILED -> {
                    toastAndLog("Error: ${state.errorCode()} for module ${state.moduleNames()}")
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        if (launch) {

            try {
                context.startActivity(
                    Intent().setClassName(
                        "com.easywashing",
                        "com.easywashing.dynamic.MyDynamicActivity"
                    )
                )
            } catch (classNotFoundException: ClassNotFoundException) {
                classNotFoundException.printStackTrace()
                //  Toast.makeText(context,"Class Not Found",Toast.LENGTH_SHORT).show()
            }
        }
    }

    public fun uninstallModule(modulesName: ArrayList<String>) {
        splitInstallManager.deferredUninstall(modulesName)
    }

    private fun toastAndLog(s: String) {
        Log.v("moduleDownload", s)
    }

}