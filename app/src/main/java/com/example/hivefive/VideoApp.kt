package com.example.hivefive

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.hivefive.helper.FirestoreUtils
import dagger.hilt.android.HiltAndroidApp

class AppLifecycleObserver : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        FirestoreUtils.updateUserStatus(true)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        FirestoreUtils.updateUserStatus(false)
    }
}

@HiltAndroidApp
class VideoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())
    }
}