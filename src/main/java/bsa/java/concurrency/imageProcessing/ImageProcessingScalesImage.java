package bsa.java.concurrency.imageProcessing;

import bsa.java.concurrency.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ImageProcessingScalesImage extends ImageProcessing {

    @Autowired
    public ImageProcessingScalesImage(FileSystem fs) {
        super(fs);
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {

        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH); //Image.SCALE_DEFAULT
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

//        super.fs.save("scaled.jpg",
//               super.fs.bytesFromBufImage(outputImage));

        return outputImage;
    }
}
