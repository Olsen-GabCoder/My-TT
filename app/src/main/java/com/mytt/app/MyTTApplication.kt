// CORRIGÉ : Le package doit impérativement être "com.mytt.app"
package com.mytt.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyTTApplication : Application()