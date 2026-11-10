# Koharu Android

Koharu Android is a native mobile manga processing and translation application featuring a high-performance **Rust backend core** integrated via JNI (`cargo-ndk`) and a memory-efficient **Jetpack Compose UI**.

## Architecture & Tech Stack

* **Frontend UI:** Kotlin, Jetpack Compose, Material 3, Android SAF (Storage Access Framework) for zero-copy file/archive access.
* **Backend Core:** Rust FFI (`koharu-ffi`), utilizing the `image` crate for high-speed local image decoding and text region analysis.
* **CI/CD:** GitHub Actions automated pipeline compiling Rust targets (`aarch64-linux-android`, `x86_64-linux-android`) alongside Android Gradle builds.

## Project Structure

```text
Koharu-android/
├── app/src/main/java/com/miora/koharu/
│   ├── MainActivity.kt            # App root, navigation & SAF pickers
│   ├── native/KoharuNative.kt     # JNI bridge to Rust FFI
│   └── ui/                        # Grid views and Reader screens
├── crates/koharu-ffi/
│   ├── Cargo.toml                 # Rust library configuration
│   └── src/lib.rs                 # Rust FFI entry points & image decoding
└── .github/workflows/
    └── android-ci.yml             # Cloud CI/CD build configuration

Building Locally
1. Ensure you have the Android SDK, NDK, and Rust stable toolchain installed with mobile targets:
rustup target add aarch64-linux-android x86_64-linux-android
cargo install cargo-ndk --locked

2. Build the Android debug package:
./gradlew assembleDebug
