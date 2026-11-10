package com.miora.koharu.native

object KoharuNative {
    init {
        try {
            System.loadLibrary("koharu_ffi")
        } catch (e: UnsatisfiedLinkError) {
            // Handled gracefully during early UI/CI scaffolding before NDK compilation is attached
        }
    }

    // Native bridge function declaration linking to Rust FFI
    external fun nativeGetVersion(): String

    fun getCoreVersion(): String {
        return try {
            nativeGetVersion()
        } catch (e: Exception) {
            "0.1.0-alpha (Kotlin Fallback)"
        }
    }
}
