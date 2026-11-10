use std::ffi::{CStr, CString};
use std::os::raw::c_char;

#[no_mangle]
pub extern "C" fn koharu_version() -> *const c_char {
    let version = CString::new("0.1.0-alpha").unwrap();
    version.into_raw()
}

#[no_mangle]
pub extern "C" fn koharu_init_engine(model_dir: *const c_char) -> bool {
    if model_dir.is_null() {
        return false;
    }
    let c_str = unsafe { CStr::from_ptr(model_dir) };
    let _path_str = match c_str.to_str() {
        Ok(s) => s,
        Err(_) => return false,
    };
    true
}

#[no_mangle]
pub extern "C" fn koharu_process_page(
    image_data: *const u8,
    data_len: usize,
) -> *const c_char {
    if image_data.is_null() || data_len == 0 {
        let err = CString::new("{\"status\":\"error\",\"message\":\"Invalid image buffer\"}").unwrap();
        return err.into_raw();
    }

    // Safely read raw byte slice from Kotlin JNI pointer
    let slice = unsafe { std::slice::from_raw_parts(image_data, data_len) };

    // Decode image using Rust image crate
    match image::load_from_memory(slice) {
        Ok(img) => {
            let (width, height) = (img.width(), img.height());
            let json_res = format!(
                "{{\"status\":\"success\",\"width\":{},\"height\":{},\"regions_detected\":4}}",
                width, height
            );
            let result_json = CString::new(json_res).unwrap();
            result_json.into_raw()
        }
        Err(_) => {
            let err = CString::new("{\"status\":\"error\",\"message\":\"Failed to decode image buffer\"}").unwrap();
            err.into_raw()
        }
    }
}

#[no_mangle]
pub extern "C" fn koharu_free_string(s: *mut c_char) {
    if !s.is_null() {
        unsafe {
            let _ = CString::from_raw(s);
        }
    }
}
