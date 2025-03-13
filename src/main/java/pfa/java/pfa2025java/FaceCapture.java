package pfa.java.pfa2025java;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class FaceCapture {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static byte[] captureFace() {
        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_default.xml");
        VideoCapture camera = new VideoCapture(0);
        Mat frame = new Mat();

        if (!camera.isOpened()) {
            System.out.println("Erreur lors de l'ouverture de la caméra.");
            return null;
        }

        camera.read(frame);
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(frame, faceDetections);

        for (Rect rect : faceDetections.toArray()) {
            Mat face = new Mat(frame, rect);
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", face, buffer);
            camera.release();
            return buffer.toArray();
        }
        camera.release();
        return null;
    }
}
