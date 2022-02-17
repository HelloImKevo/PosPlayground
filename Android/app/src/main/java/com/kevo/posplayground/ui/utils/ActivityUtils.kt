package com.kevo.posplayground.ui.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

object ActivityUtils {

    private val TAG = "ActivityUtils"

    /**
     * Creates an intent for the activity class, and pushes the activity to the
     * foreground using the "Start Activity" API. If the activity does not have
     * a "Launch Mode" specified in the application Manifest, then a new instance
     * of the activity may be created. The [Intent.FLAG_ACTIVITY_SINGLE_TOP]
     * flag is specified to avoid this, but if it becomes an issue, the POS App
     * will need to specify an appropriate "Launch Mode" for this activity, like
     * `singleTask`. Additionally, it is the POS App's responsibility to
     * ensure that an instance of this activity is already running in the background.
     *
     * @param fallbackActivityClass The activity class provided in the original request
     * from the consuming application. This activity may no longer be active or at the
     * top of the stack, and should only be used as a fallback option.
     * @see [
     * Android Documentation - Activity Launch Mode](https://developer.android.com/guide/topics/manifest/activity-element.lmode)
     */
    @JvmStatic
    fun pushToForeground(context: Context, fallbackActivityClass: Class<out Activity?>?) {
        var topActivityClass = fallbackActivityClass
        var component: ComponentName? = null
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        try {
            component = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activityManager.appTasks[0].taskInfo.topActivity
            } else {
                activityManager.getRunningTasks(1)[0].topActivity
            }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        }

        // Example Package: com.pos.checkout
        // Example Class: com.pos.checkout.CheckoutMainActivity
        if (component != null) {
            try {
                val componentClass = Class.forName(component.className)
                if (Activity::class.java.isAssignableFrom(componentClass)) {
                    topActivityClass = componentClass as Class<out Activity?>
                }
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, Log.getStackTraceString(e))
            } catch (e: ClassCastException) {
                Log.e(TAG, Log.getStackTraceString(e))
            }
        }

        if (topActivityClass == null) {
            Log.w(TAG, "The POS App could not determine which Activity " +
                    "should be pushed to the foreground!")
            return
        }

        Log.i(TAG, "Bringing $topActivityClass to the foreground.")

        /*
        From Android documentation:

        If FLAG_ACTIVITY_NEW_TASK is set, this activity will become the start of a new
        task on this history stack. A task (from the activity that started it to the
        next task activity) defines an atomic group of activities that the user can
        move to. Tasks can be moved to the foreground and background; all of the
        activities inside of a particular task always remain in the same order.

        When using FLAG_ACTIVITY_NEW_TASK, if a task is already running for the activity
        you are now starting, then a new activity will not be started; instead, the current
        task will simply be brought to the front of the screen with the state it was last in.
        See Intent.FLAG_ACTIVITY_MULTIPLE_TASK for a flag to disable this behavior.

        If FLAG_ACTIVITY_SINGLE_TOP is set, the activity will not be launched if it is
        already running at the top of the history stack.
         */
        val intent = Intent(context, topActivityClass)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        context.startActivity(intent)
    }
}
