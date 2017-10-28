package assets;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagesManager extends AssetType<BufferedImage>{
	
	public BufferedImage readImage(String filename) {
		BufferedImage bi = assets().get(filename);
		if(bi != null){
			ColorModel cm = bi.getColorModel();
			return new BufferedImage(cm, bi.copyData(null), cm.isAlphaPremultiplied(), null);
		} else {
			new Exception("Image file "+filename+" missing").printStackTrace();
			return null;
		}
	}

	@Override
	public File directory() {
		return new File("res\\images\\");
	}

	@Override
	public BufferedImage load(File f) throws IOException {
		return ImageIO.read(f);
	}
	
}
