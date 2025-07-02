package org.jikisan.taily

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform