/*
 * ImageLoader allows the Game to load images
*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	public static BufferedImage imageLoader(String path){
		BufferedImage temp = null;
		try {
			temp = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("Cannot find image\n" + e.getMessage());
		} catch (Exception e){
                    System.out.println("Image loader had proplem\n" + e.getMessage());
                }
		return temp;
		
	}
}
