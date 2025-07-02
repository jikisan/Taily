package org.jikisan.taily

import android.app.Application
import io.ktor.http.ContentType
import org.jikisan.cmpecommerceapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application()  {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}