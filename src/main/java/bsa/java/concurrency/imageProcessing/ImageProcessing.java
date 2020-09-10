package bsa.java.concurrency.imageProcessing;

import bsa.java.concurrency.exceptions.MyIoException;
import bsa.java.concurrency.fs.FileSystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public abstract class ImageProcessing {
    final FileSystem fs;

    public ImageProcessing(FileSystem fs) {
        this.fs = fs;
    }

    public abstract BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight);

    public BufferedImage scale2Gray(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++) {
                int p = originalImage.getRGB(i,j);
                int r = (p>>16) & 0xff;
                int g = (p>>8) & 0xff;
                int b = p & 0xff;
                int avg = (r+g+b)/3;
                p = (avg<<16) | (avg<<8) | avg;

                originalImage.setRGB(i, j, p);
            }

//        fs.save("grayed.jpg", fs.bytesFromBufImage(originalImage));

        return originalImage;
//      var output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//      output.getGraphics().drawImage(originalImage, 0, 0, null);
//      return output;
    }

    public long calculateDHash(byte[] bytesOfImage) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(bytesOfImage));
        } catch (IOException e) {
            throw new MyIoException("error reading file");
        }

        image = scale2Gray(resizeImage(image, 9, 9));
/*
        var result = image.getScaledInstance(9, 9, Image.SCALE_SMOOTH);
        image = new BufferedImage(9, 9, BufferedImage.TYPE_BYTE_GRAY);
        image.getGraphics().drawImage(result, 0, 0, null);
*/
// diagonal from 9x9
        long hash = 0;
        for(int i=1; i<9; i++)
            for(int j=1; j<9; j++) {
                int prev = image.getRGB(i - 1, j - 1) & 0xff;
                int cur = image.getRGB(i, j) & 0xff;

                hash |= cur > prev ? 1 : 0;
                hash <<= 1;
            }
        return hash;
    }
}
