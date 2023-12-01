package filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Binarization {
    public static BufferedImage binarizeImage(BufferedImage originalImage){
        int threshold = calculateThreshold(originalImage);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage binaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(originalImage.getRGB(x, y));

                int brightness = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());

                int binaryValue = (brightness < threshold) ? 0 : 255;

                Color binaryColor = new Color(binaryValue, binaryValue, binaryValue);
                binaryImage.setRGB(x, y, binaryColor.getRGB());
            }
        }

        return binaryImage;
    }

    private static int calculateThreshold(BufferedImage originalImage) {
        List<Integer> histogram = calculateHistogram(originalImage);
        int total = histogram.size();
        int sum = 0;
        for (int i = 0; i < total; i++) {
            sum += i * histogram.get(i);
        }

        double sumB = 0;
        int wB = 0;
        int wF = 0;

        double maxVariance = 0;
        int threshold = 0;

        for (int i = 0; i < total; i++) {
            wB += histogram.get(i);
            if (wB == 0) {
                continue;
            }

            wF = total - wB;
            if (wF == 0) {
                break;
            }

            sumB += i * histogram.get(i);

            double mB = sumB / wB;
            double mF = (sum - sumB) / wF;

            double varianceBetween = wB * wF * Math.pow((mB - mF), 2);

            if (varianceBetween > maxVariance) {
                maxVariance = varianceBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private static java.util.List<Integer> calculateHistogram(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] histogram = new int[256];

        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                Color color = new Color(image.getRGB(x, y));
                int gray = (color.getRed() + color.getBlue() + color.getGreen()) / 3;

                histogram[gray]++;
            }
        }

        java.util.List<Integer> result = arrayToList(histogram);
        return result;
    }

    private static java.util.List<Integer> arrayToList(int[] array) {
        java.util.List<Integer> list = new ArrayList<>(array.length);

        for (int value : array) {
            list.add(value);
        }

        return list;
    }
}
