package net.digicre.digilaun;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 画像データのキャッシュを行うシングルトンクラスです。
 * 通常の <code>{@link Image}</code>
 * オブジェクトはファイルをロックしますが、
 * このクラスはファイルをロックせずにキャッシュを行う機能を提供します。
 * @author p10090
 *
 */
public class ImageCache {
	/**
	 * このクラスのインスタンスです。
	 */
	private static ImageCache instance = null;

	/**
	 * 画像の読み込みに使うツールキットです。
	 */
	private Toolkit toolkit;

	/**
	 * 読み込んだ画像のキャッシュです。
	 */
	private Map<File, Image> caches;

	/**
	 * このクラスは、外部で新しいインスタンスを作成できないシングルトンクラスです。
	 */
	private ImageCache() {
		this.toolkit = Toolkit.getDefaultToolkit();
		this.caches = new HashMap<File, Image>();
	}

	/**
	 * このクラスのインスタンスを取得します。
	 * @return このクラスのインスタンス
	 */
	public synchronized static ImageCache getInstance() {
		if(instance == null)
			instance = new ImageCache();
		return instance;
	}

	/**
	 * 画像 (<code>{@link Image}</code>) を取得します。
	 * @param path 取得する画像ファイルへのパス
	 * @return ファイルから読み込んだ、またはキャッシュ済みの画像
	 * @throws IOException 入出力例外
	 */
	public Image getImage(String path) throws IOException {
		return this.getImage(new File(path));
	}

	/**
	 * 画像 (<code>{@link Image}</code>) を取得します。
	 * @param file 取得する画像ファイル
	 * @return ファイルから読み込んだ、またはキャッシュ済みの画像
	 * @throws IOException 入出力例外
	 */
	public Image getImage(File file) throws IOException {
		synchronized(this.caches) {
			if(!caches.containsKey(file)) {
				FileInputStream fis = new FileInputStream(file);
				byte[] imgBytes = new byte[fis.available()];

				fis.read(imgBytes);
				fis.close();
				caches.put(file, toolkit.createImage(imgBytes));
			}
		}
		return caches.get(file);
	}
}
