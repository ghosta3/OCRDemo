import net.sourceforge.tess4j.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //System.out.println(System.getProperty("sun.arch.data.model"));
        //System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "F:/Study/OCR/lib/win32-x86" : "F:/Study/OCR/lib/win32-x86-64");

        File imageFile = new File("D:\\Download\\SimpleValidateCode.jpg");
        BufferedImage in = ImageIO.read(imageFile);
        BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        convert(in, image);
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        // File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build bundles English data
        // instance.setDatapath(tessDataFolder.getParent());

        try {
            instance.setLanguage("eng");
            String result = instance.doOCR(image);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void convert(BufferedImage in, BufferedImage image) throws IOException {
        Graphics2D g = image.createGraphics();
        g.drawImage(in, 0, 0, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        int w = in.getWidth();
        int h = in.getHeight();
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int[][] dataIn2D = new int[h][w];


        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                dataIn2D[i][j] = data[i * w + j] & 0xFF;
            }
        }

        for (int i = 1; i < h - 1; i++) {
            for (int j = 1; j < w - 1; j++) {
                if (judge(dataIn2D, i, j) > 1300) {
                    data[i * w + j] = -1;
                }
            }
        }

        ImageIO.write(image, "jpg", new File("D:/abc.jpg"));

    }

    private static int judge(int[][] dataIn2D, int x, int y) {
        int weight = dataIn2D[x - 1][y - 1] + dataIn2D[x - 1][y] + dataIn2D[x - 1][y + 1]
                + dataIn2D[x][y - 1] + dataIn2D[x][y + 1]
                + dataIn2D[x + 1][y - 0] + dataIn2D[x + 1][y] + dataIn2D[x + 1][y + 1];

        return weight;
    }
}
