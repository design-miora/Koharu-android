package com.miora.koharu.native

object KoharuNative {
    init {
        try {
            System.loadLibrary("koharu_ffi")
        } catch (e: UnsatisfiedLinkError) {
            // Handled gracefully during early UI/CI scaffolding
        }
    }

    // Native bridge declarations linking to Rust FFI
    external fun nativeGetVersion(): String
    external fun nativeInitEngine(modelDir: String): Boolean
    external fun nativeProcessPage(imageData: ByteArray, dataLen: Int): String
    external fun nativeFreeString(ptr: Long)

    fun getCoreVersion(): String {
        return try {
            nativeGetVersion()
        } catch (e: Exception) {
            "0.1.0-alpha (Kotlin Fallback)"
        }
    }

    fun initEngine(modelDir: String): Boolean {
        return try {
            nativeInitEngine(modelDir)
        } catch (e: Exception) {
            false
        }
    }

    fun processPage(imageData: ByteArray): String {
        return try {
            nativeProcessPage(imageData, imageData.size)
        } catch (e: Exception) {
            "{\"status\":\"error\",\"message\":\"Failed to invoke native process\"}"
        }
    }
}
