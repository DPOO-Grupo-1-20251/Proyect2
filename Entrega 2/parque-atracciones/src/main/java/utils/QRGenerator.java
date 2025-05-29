package utils;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

public class QRGenerator {

    public static BufferedImage generateFakeQR(String content, int size) {
        // This is a FAKE QR generator that creates a placeholder image
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

        g.setColor(Color.BLACK);
        g.drawString("QR", size / 2 - 10, size / 2);

        g.dispose();
        return image;
    }
}
