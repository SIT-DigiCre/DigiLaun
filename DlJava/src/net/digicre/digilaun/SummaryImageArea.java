package net.digicre.digilaun;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.ImageObserver;

import javax.swing.JComponent;

import net.digicre.digilaun.work.Work;

/**
 * 作品の詳細イメージを、必要に応じて伸縮しながら表示するコンポーネントです。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
public class SummaryImageArea extends JComponent implements ComponentListener {

	/**
	 * 画像が読めなかったときに、代わりに表示する画像へのパスです。
	 */
	private static final String ALTERNATIVE_IMAGE_PATH =
			"img/NoSummaryImage.png";

	/**
	 * 代替イメージです。
	 */
	private static Image alternativeImage = null;

	/**
	 * この <code>DetailImageArea</code> に関連付けられた作品オブジェクトです。
	 */
	private Work work;

	/**
	 * コンポーネントに表示するイメージです。
	 */
	private Image baseImage;

	/**
	 * 新しい <code>DetailImageArea</code> を作成します。
	 */
	SummaryImageArea() {
		this.baseImage = null;
	}

	/**
	 * この <code>DetailImageArea</code> に関連付ける作品をセットします。
	 * @param work
	 */
	synchronized void setWork(Work work) {
		if(this.work == work)
			return;
		this.work = work;
		try {
			this.baseImage =
					ImageCache.getInstance().getImage(work.getPicturePath());
		}
		catch(java.io.IOException e) {
			createAlternativeImage();
			this.baseImage = alternativeImage;
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	/**
	 * このコンポーネントがリサイズされた時のイベント処理です。
	 * このコンポーネントを再描画します。
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	/**
	 * 代替イメージのインスタンスを作成し、フィールドに格納します。
	 * すでにあれば何もしません。
	 */
	private synchronized void createAlternativeImage() {
		if(alternativeImage == null) {
			alternativeImage =
					this.getToolkit().getImage(ALTERNATIVE_IMAGE_PATH);
		}
	}
	/**
	 * このコンポーネントを描画します。
	 * @param g 描画用 Graphics オブジェクト
	 */
	@Override
	public void paint(Graphics g) {
		// 拡大後の寸法
		int newWidth, newHeight;

		// 元イメージがロード失敗なら
		synchronized(this) {
			if(baseImage == null ||
					baseImage != alternativeImage &&
					(checkImage(baseImage, this) &
							(ImageObserver.ABORT | ImageObserver.ERROR)) != 0) {
				// 別のイメージをロード
				createAlternativeImage();
				this.baseImage = alternativeImage;
				this.repaint();
				return;
			}
		}
		// 拡大後の寸法を計算
		{
			// 拡大率の分子、分母
			int zoom_n, zoom_d;
			if(this.getHeight() * this.baseImage.getWidth(this) <
					this.getWidth() * this.baseImage.getHeight(this)) {
				zoom_n = this.getHeight();
				zoom_d = this.baseImage.getHeight(this);
			}
			else {
				zoom_n = this.getWidth();
				zoom_d = this.baseImage.getWidth(this);
			}
			// 書く必要がなければ終了
			if(zoom_n <= 0)
				return;
			newWidth  =
					this.baseImage.getWidth (this) * zoom_n / zoom_d;
			newHeight =
					this.baseImage.getHeight(this) * zoom_n / zoom_d;
		}
		// 描画
		if(g instanceof Graphics2D)
			((Graphics2D)g).setRenderingHint(
					RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(baseImage,
				(this.getWidth () - newWidth ) / 2,
				(this.getHeight() - newHeight) / 2,
				newWidth, newHeight, this);
	}
}
