use std::ffi::{CStr, CString};
use std::os::raw::c_char;

#[no_mangle]
pub extern "C" fn koharu_version() -> *const c_char {
    let version = CString::new("0.1.0-alpha").unwrap();
    version.into_raw()
}

/// Initializes the Koharu processing engine with model path configurations
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

    // Placeholder for initializing OCR/Translation models in Rust backend
    true
}

/// Stub for processing a manga page image buffer (JPEG/PNG bytes)
#[no_mangle]
pub extern "C" fn koharu_process_page(
    image_data: *const u8,
    data_len: usize,
) -> *const c_char {
    if image_data.is_null() || data_len == 0 {
        let err = CString::new("Error: Invalid image buffer").unwrap();
        return err.into_raw();
    }

    // Processing pipeline stub: returns JSON result of detected text regions
    let result_json = CString::new("{\"status\":\"success\",\"regions_detected\":0}").unwrap();
    result_json.into_raw()
}

/// Free memory allocated for strings passed back to Kotlin/JNI
#[no_mangle]
pub extern "C" fn koharu_free_string(s: *mut c_char) {
    if !s.is_null() {
        unsafe {
            let _ = CString::from_raw(s);
        }
    }
}
