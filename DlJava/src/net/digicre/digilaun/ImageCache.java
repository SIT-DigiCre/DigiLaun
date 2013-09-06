package net.digicre.digilaun;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

/**
* フォルダまたは JAR から画像を読み込んで保持するクラスです。
* @author p10090
*
*/
public class ImageCache {
	private Toolkit toolkit;
	
	public ImageCache() {
		this.toolkit = Toolkit.getDefaultToolkit();
	}
	
	public ImageCache(Toolkit toolkit) {
		this.toolkit = toolkit;
	}
	
	public Image get(String key) {
		String path;

		try {
			path = new java.io.File(key).getCanonicalPath();
		} catch (IOException e) {
			path = new java.io.File(key).getAbsolutePath();
		}
		return toolkit.getImage(path);
	}
}
