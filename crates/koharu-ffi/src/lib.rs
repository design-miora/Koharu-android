// Koharu Android FFI Bridge Stub

#[no_mangle]
pub extern "C" fn koharu_version() -> *const std::ffi::c_char {
    let version = std::ffi::CString::new("0.1.0-alpha").unwrap();
    version.into_raw()
}
