package pfa.java.pfa2025java;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FaceCapture {

    static {
        // Load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static byte[] captureFace() {
        // Load the Haar Cascade file from resources
        CascadeClassifier faceDetector = loadCascadeClassifier();
        if (faceDetector == null) {
            System.out.println("Erreur: Impossible de charger le fichier Haar Cascade.");
            return null;
        }

        // Open the camera
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Erreur lors de l'ouverture de la caméra.");
            return null;
        }

        // Capture a frame
        Mat frame = new Mat();
        if (!camera.read(frame)) {
            System.out.println("Erreur lors de la capture de l'image.");
            camera.release();
            return null;
        }

        // Detect faces in the frame
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(frame, faceDetections);

        // If a face is detected, extract and return it as a byte array
        for (Rect rect : faceDetections.toArray()) {
            Mat face = new Mat(frame, rect);
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", face, buffer);
            camera.release();
            return buffer.toArray();
        }

        // Release the camera and return null if no face is detected
        camera.release();
        System.out.println("Aucun visage détecté.");
        return null;
    }

    private static CascadeClassifier loadCascadeClassifier() {
        try {
            // Load the Haar Cascade file from resources
            InputStream inputStream = FaceCapture.class.getClassLoader().getResourceAsStream("haarcascade_frontalface_default.xml");
            if (inputStream == null) {
                throw new IOException("Fichier Haar Cascade introuvable dans les ressources.");
            }

            // Create a temporary file to store the Haar Cascade file
            File tempFile = File.createTempFile("haarcascade_frontalface_default", ".xml");
            tempFile.deleteOnExit(); // Delete the file when the JVM exits

            // Copy the Haar Cascade file from resources to the temporary file
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Load the Haar Cascade file into the CascadeClassifier
            CascadeClassifier faceDetector = new CascadeClassifier(tempFile.getAbsolutePath());
            if (faceDetector.empty()) {
                throw new IOException("Échec du chargement du fichier Haar Cascade.");
            }

            return faceDetector;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier Haar Cascade: " + e.getMessage());
            return null;
        }
    }
}