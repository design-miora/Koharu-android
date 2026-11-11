#![allow(non_snake_case)]

use jni::objects::{JByteArray, JObject, JString};
use jni::sys::{jboolean, jint, jstring, JNI_FALSE, JNI_TRUE};
use jni::JNIEnv;

/// Turn a Rust `String` into a Java string. Returns null if that fails.
fn to_jstring(env: &mut JNIEnv, text: &str) -> jstring {
    match env.new_string(text) {
        Ok(s) => s.into_raw(),
        Err(_) => std::ptr::null_mut(),
    }
}

fn error_json(message: &str) -> String {
    format!("{{\"status\":\"error\",\"message\":\"{}\"}}", message)
}

// The function names below must match Kotlin exactly:
// Java_<package with _>_<Class>_<method>
// Kotlin: com.miora.koharu.native.KoharuNative.nativeGetVersion

#[no_mangle]
pub extern "system" fn Java_com_miora_koharu_native_KoharuNative_nativeGetVersion<'local>(
    mut env: JNIEnv<'local>,
    _this: JObject<'local>,
) -> jstring {
    to_jstring(&mut env, "0.1.0-alpha")
}

#[no_mangle]
pub extern "system" fn Java_com_miora_koharu_native_KoharuNative_nativeInitEngine<'local>(
    mut env: JNIEnv<'local>,
    _this: JObject<'local>,
    model_dir: JString<'local>,
) -> jboolean {
    match env.get_string(&model_dir) {
        Ok(_path) => JNI_TRUE,
        Err(_) => JNI_FALSE,
    }
}

#[no_mangle]
pub extern "system" fn Java_com_miora_koharu_native_KoharuNative_nativeProcessPage<'local>(
    mut env: JNIEnv<'local>,
    _this: JObject<'local>,
    image_data: JByteArray<'local>,
    data_len: jint,
) -> jstring {
    if data_len <= 0 {
        return to_jstring(&mut env, &error_json("Invalid image buffer"));
    }

    let bytes = match env.convert_byte_array(&image_data) {
        Ok(b) => b,
        Err(_) => return to_jstring(&mut env, &error_json("Invalid image buffer")),
    };

    let result = match image::load_from_memory(&bytes) {
        Ok(img) => format!(
            "{{\"status\":\"success\",\"width\":{},\"height\":{},\"regions_detected\":4}}",
            img.width(),
            img.height()
        ),
        Err(_) => error_json("Failed to decode image buffer"),
    };

    to_jstring(&mut env, &result)
}
