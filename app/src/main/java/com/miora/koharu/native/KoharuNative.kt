package com.miora.koharu.native

object KoharuNative {
    init {
        try {
            System.loadLibrary("koharu_ffi")
        } catch (e: UnsatisfiedLinkError) {
            // Library missing: the fallbacks below keep the app from crashing.
        }
    }

    // Native bridge: each one maps to a Rust function named
    // Java_com_miora_koharu_native_KoharuNative_<name>
    external fun nativeGetVersion(): String
    external fun nativeInitEngine(modelDir: String): Boolean
    external fun nativeProcessPage(imageData: ByteArray, dataLen: Int): String

    // Throwable (not Exception) because a missing native function throws
    // UnsatisfiedLinkError, which is an Error.
    fun getCoreVersion(): String {
        return try {
            nativeGetVersion()
        } catch (e: Throwable) {
            "0.1.0-alpha (Kotlin Fallback)"
        }
    }

    fun initEngine(modelDir: String): Boolean {
        return try {
            nativeInitEngine(modelDir)
        } catch (e: Throwable) {
            false
        }
    }

    fun processPage(imageData: ByteArray): String {
        return try {
            nativeProcessPage(imageData, imageData.size)
        } catch (e: Throwable) {
            "{\"status\":\"error\",\"message\":\"Failed to invoke native process\"}"
        }
    }
}
