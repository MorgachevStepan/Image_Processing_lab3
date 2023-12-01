package filters;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Hough {
    public static void calculateHough(String outputImagePath, String originalImagePath, String pathToSave) {
        Mat binaryImage = Imgcodecs.imread(outputImagePath, Imgcodecs.IMREAD_GRAYSCALE);
        Mat lines = findLines(binaryImage);
        List<MatOfPoint> squares = findSquares(lines);
        Mat originalImage = Imgcodecs.imread(originalImagePath);
        drawSquares(originalImage, squares);
        Imgcodecs.imwrite(pathToSave, originalImage);
    }

    private static Mat findLines(Mat binaryImage) {
        Mat lines = new Mat();
        Imgproc.HoughLinesP(binaryImage, lines, 1, Math.PI / 180, 50, 50, 10);
        return lines;
    }

    private static List<MatOfPoint> findSquares(Mat lines) {
        List<MatOfPoint> squares = new ArrayList<>();

        // Объединение линий в квадраты
        for (int i = 0; i < lines.rows(); i++) {
            double[] val = lines.get(i, 0);
            double x1 = val[0], y1 = val[1], x2 = val[2], y2 = val[3];

            for (int j = i + 1; j < lines.rows(); j++) {
                double[] nextVal = lines.get(j, 0);
                double nx1 = nextVal[0], ny1 = nextVal[1], nx2 = nextVal[2], ny2 = nextVal[3];

                // Проверка, можно ли объединить линии в квадрат
                if (isSquare(x1, y1, x2, y2, nx1, ny1, nx2, ny2)) {
                    squares.add(createSquare(x1, y1, x2, y2, nx1, ny1, nx2, ny2));
                }
            }
        }

        return squares;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static boolean isSquare(double x1, double y1, double x2, double y2,
                                    double nx1, double ny1, double nx2, double ny2) {
        double minLength = 50;

        if (distance(x1, y1, x2, y2) < minLength || distance(nx1, ny1, nx2, ny2) < minLength) {
            return false;
        }

        boolean areParallel = areLinesParallel(x1, y1, x2, y2, nx1, ny1, nx2, ny2);

        boolean areEqual = distance(x1, y1, x2, y2) == distance(nx1, ny1, nx2, ny2);

        boolean areDistancesEqual = distanceBetweenLines(x1, y1, x2, y2, nx1, ny1, nx2, ny2) / distance(x1, y1, x2, y2) >= 0.925
                && distanceBetweenLines(x1, y1, x2, y2, nx1, ny1, nx2, ny2) / distance(x1, y1, x2, y2) <= 1.075;

        return areParallel && areEqual && areDistancesEqual;
    }

    private static boolean areLinesParallel(double x1, double y1, double x2, double y2,
                                            double nx1, double ny1, double nx2, double ny2) {
        return (x2 - x1) * (ny2 - ny1) == (nx2 - nx1) * (y2 - y1) ;
    }

    private static double distanceBetweenLines(double x1, double y1, double x2, double y2,
                                               double nx1, double ny1, double nx2, double ny2) {
        return Math.abs((nx2 - nx1) * (y1 - ny1) - (x1 - nx1) * (ny2 - ny1)) /
                Math.sqrt(Math.pow(nx2 - nx1, 2) + Math.pow(ny2 - ny1, 2));
    }

    private static MatOfPoint createSquare(double x1, double y1, double x2, double y2,
                                           double nx1, double ny1, double nx2, double ny2) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        Point p3 = new Point(nx2, ny2);
        Point p4 = new Point(nx1, ny1);

        MatOfPoint square = new MatOfPoint(p1, p2, p3, p4);

        return square;
    }


    private static void drawSquares(Mat image, List<MatOfPoint> squares) {
        for (MatOfPoint square : squares) {
            Imgproc.polylines(image, List.of(square), true, new Scalar(0, 255, 0), 2);
        }
    }
}
