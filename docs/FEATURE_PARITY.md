# Feature Parity Matrix: Upstream vs. Android Target

**Legend:**
- 🔴 Not Audited / Not Started
- 🟡 Planned / In Progress
- 🟢 Implemented & Verified

| Feature Category | Upstream Feature | Android Status | Implementation Notes / Target |
| :--- | :--- | :---: | :--- |
| **Android Architecture** | Native Container & CI | 🟢 | Jetpack Compose + Cargo-NDK FFI Skeleton + GitHub Actions CI |
| **Project Management** | Import Folders, CBZ, Images | 🟢 | SAF-compliant document picker (`OpenDocumentTree` & `OpenDocument`) |
| **Detection & OCR** | Text Region Segmentation | 🟡 | Rust FFI pipeline stub (`koharu_process_page`) ready for ONNX integration |
| **Translation** | Local GGUF LLM | 🔴 | Hybrid AI Engine; User-imported GGUF models |
| **Inpainting** | Generative Text Removal | 🔴 | Memory-optimized page tile processing |
| **Canvas & Editor** | Interactive Pan / Zoom / Selection | 🟢 | `PageReaderScreen` & `PageGridScreen` Jetpack Compose navigation views |
| **Export** | PNG / JPEG / CBZ Export | 🔴 | Background WorkManager export service |
