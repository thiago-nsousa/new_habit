package com.example.newhabit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
    Annotate the Application class to trigger Hilt code generation.
    Injects the application context into dependencies that need it.
 */
@HiltAndroidApp
class MyApplication : Application()