package org.jikisan.taily

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.jikisan.cmpecommerceapp.di.initKoin

fun MainViewController() = ComposeUIViewController(

    configure = { initKoin() }
) {
    Napier.base(DebugAntilog())
    App()
}