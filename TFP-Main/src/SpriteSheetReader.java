
import java.awt.image.BufferedImage;

public class SpriteSheetReader {

    private static BufferedImage[] sprites;

    public SpriteSheetReader() {
    }

    public static BufferedImage[] getSprites(int x, int y, int size, BufferedImage bigImg) {
        sprites = new BufferedImage[x*y];
        int k =0;
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                sprites[k] = bigImg.getSubimage(j * size, i * size, size, size);
                k++;
            }
        }
        return sprites;
    }
}
