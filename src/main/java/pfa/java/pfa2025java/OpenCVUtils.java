package pfa.java.pfa2025java;


import org.opencv.core.Core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class OpenCVUtils {
    static {
        loadOpenCVNativeLibrary();
    }

    private static void loadOpenCVNativeLibrary() {
        try {
            // Determine the operating system and architecture
            String osName = System.getProperty("os.name").toLowerCase();
            String arch = System.getProperty("os.arch").toLowerCase();

            // Map the operating system and architecture to the appropriate library name
            String libraryName;
            if (osName.contains("win") && arch.contains("64")) {
                libraryName = "opencv_java4110.dll"; // Windows 64-bit
            } else if (osName.contains("linux") && arch.contains("64")) {
                libraryName = "libopencv_java451.so"; // Linux 64-bit
            } else if (osName.contains("mac")) {
                libraryName = "libopencv_java451.dylib"; // macOS
            } else {
                throw new UnsupportedOperationException("Unsupported platform: " + osName + " " + arch);
            }

            // Load the native library from resources
            File tempFile = File.createTempFile("opencv_java451", ".dll");
            tempFile.deleteOnExit();

            try (InputStream inputStream = OpenCVUtils.class.getClassLoader().getResourceAsStream("native/" + libraryName)) {
                if (inputStream == null) {
                    throw new IOException("Native library not found in resources: " + libraryName);
                }
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            System.load(tempFile.getAbsolutePath());
            System.out.println("OpenCV native library loaded successfully: " + libraryName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load OpenCV native library", e);
        }
    }

    public static void initialize() {
        // This method ensures the static block is executed
    }
}
