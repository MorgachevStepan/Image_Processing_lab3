import filters.Binarization;
import filters.Hough;
import filters.Sobel;
import org.opencv.core.Core;
import utils.ImageLoader;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection {
    public static final String FIRST_SAMPLE = "src/main/resources/Shapes";
    public static final String SECOND_SAMPLE = "src/main/resources/Shapes2";
    public static final String THIRD_SAMPLE = "src/main/resources/Shapes3";
    public static final String FOURTH_SAMPLE = "src/main/resources/Shapes4";
    public static final String BIN_TYPE = "_B";
    public static final String SQUARE_TYPE = "_S";
    public static final String PNG = ".png";
    public static final String JPG = ".jpg";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        ImageLoader imageLoader = new ImageLoader();
        Sobel sobel = new Sobel();

        /*String inputImagePath = FIRST_SAMPLE + PNG;
        String outputImagePath = FIRST_SAMPLE + BIN_TYPE + PNG; //First Sample
        String pathToSave = FIRST_SAMPLE + SQUARE_TYPE + PNG;*/

        /*String inputImagePath = SECOND_SAMPLE + PNG;
        String outputImagePath = SECOND_SAMPLE + BIN_TYPE + PNG;  //Second Sample (without bin)
        String pathToSave = SECOND_SAMPLE + SQUARE_TYPE + PNG;*/

        /*String inputImagePath = THIRD_SAMPLE + PNG;
        String outputImagePath = THIRD_SAMPLE + BIN_TYPE + PNG;  //Third Sample
        String pathToSave = THIRD_SAMPLE + SQUARE_TYPE + PNG;*/

        String inputImagePath = FOURTH_SAMPLE + PNG;
        String outputImagePath = FOURTH_SAMPLE + BIN_TYPE + PNG;  //Fourth Sample
        String pathToSave = FOURTH_SAMPLE + SQUARE_TYPE + PNG;

        BufferedImage inputImage = imageLoader.loadImage(inputImagePath);
        BufferedImage edgeImage = sobel.applySobelOperator(inputImage);
        BufferedImage binImage = Binarization.binarizeImage(edgeImage);

        imageLoader.saveImage(binImage, outputImagePath);
        Hough.calculateHough(outputImagePath, inputImagePath, pathToSave);
    }
}