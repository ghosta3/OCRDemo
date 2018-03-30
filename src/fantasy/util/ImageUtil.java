package fantasy.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage convertToGray(BufferedImage in) throws IOException {

        int w = in.getWidth();
        int h = in.getHeight();
        BufferedImage bufferedImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(in, 0,0, null);
        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
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

        ImageIO.write(bufferedImage, "jpg", new File("D:/abc.jpg"));
        return bufferedImage;
    }

    private static int judge(int[][] dataIn2D, int x, int y) {
        int weight = dataIn2D[x - 1][y - 1] + dataIn2D[x - 1][y] + dataIn2D[x - 1][y + 1]
                + dataIn2D[x][y - 1] + dataIn2D[x][y + 1]
                + dataIn2D[x + 1][y - 0] + dataIn2D[x + 1][y] + dataIn2D[x + 1][y + 1];

        return weight;
    }
}
