package net.digicre.digilaun;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
* フォルダまたは JAR から画像を読み込んで保持するクラスです。
* @author p10090
*
*/
public class ImageCache {
	public final List<String> imgExtensions = Arrays.asList(new String[]
			{ "png", "gif", "jpg"});//, "bmp", "ico" });
	private Toolkit toolkit;
	public String basePath = "./img";
	
	public ImageCache() {
		this.toolkit = Toolkit.getDefaultToolkit();
	}
	
	public ImageCache(Toolkit toolkit) {
		this.toolkit = toolkit;
	}
	
	public Image get(String key) {
		String strFormat = String.format("%s/%s.%%s", basePath, key);
		Iterator<String> i = imgExtensions.iterator();
		while(i.hasNext()) {
			String ext = i.next();
			try {
				return toolkit.getImage(String.format(strFormat, ext));
			}
			catch(Exception e) {
			}
		}
		throw new RuntimeException("cannot load image: "+key);
	}
}
