package filters;

import java.awt.image.BufferedImage;

public class Sobel {
    private final int[][] SOBEL_X = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    private final int[][] SOBEL_Y = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

    public BufferedImage applySobelOperator(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = applyKernel(inputImage, x, y, SOBEL_X);
                int gy = applyKernel(inputImage, x, y, SOBEL_Y);

                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
                magnitude = Math.min(255, Math.max(0, magnitude));

                int rgb = (magnitude << 16) | (magnitude << 8) | magnitude;
                edgeImage.setRGB(x, y, rgb);
            }
        }

        return edgeImage;
    }

    private int applyKernel(BufferedImage image, int x, int y, int[][] kernel) {
        int sum = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int pixelValue = image.getRGB(x + i, y + j) & 0xFF;
                sum += pixelValue * kernel[j + 1][i + 1];
            }
        }
        return sum;
    }
}
