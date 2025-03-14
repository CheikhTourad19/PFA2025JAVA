package pfa.java.pfa2025java;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_core;


public class testcv {
    public static void main(String[] args) {
        System.out.println("OpenCV loaded successfully!");
        Mat mat = Mat.eye(3, 3, opencv_core.CV_8UC1).asMat();
        System.out.println("Mat = " + mat);
    }
}
