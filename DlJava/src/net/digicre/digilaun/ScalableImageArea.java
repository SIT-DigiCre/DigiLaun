package net.digicre.digilaun;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.ImageObserver;

import javax.swing.JComponent;

import net.digicre.digilaun.work.Work;

@SuppressWarnings("serial")
public class ScalableImageArea extends JComponent implements ComponentListener {

	/**
	 * 画像が読めなかったときに、代わりに表示する画像へのパスです。
	 */
	private static final String ALTERNATIVE_IMAGE_PATH =
			"img/NoDetailImage.png";
	/**
	 * コンポーネントに表示するイメージです。
	 */
	private Image baseImage;
	
	private boolean imgIsAnternative = false;
	
	ScalableImageArea() {
		this.baseImage = null;
	}

	synchronized void setWork(Work work) {
		this.baseImage = this.getToolkit().createImage(work.getPicturePath());
		imgIsAnternative = false;
	}
	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	@Override
	public void paint(Graphics g) {
		// 拡大後の寸法
		int newWidth, newHeight;
		
		// 元イメージがロード失敗なら
		synchronized(this) {
			if(baseImage == null ||
					!imgIsAnternative &&
					(checkImage(baseImage, this) &
							(ImageObserver.ABORT | ImageObserver.ERROR)) != 0) {
				// 別のイメージをロード
				this.baseImage =
						this.getToolkit().createImage(ALTERNATIVE_IMAGE_PATH);
				imgIsAnternative = true;
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
		if(g instanceof java.awt.Graphics2D)
			((java.awt.Graphics2D)g).setRenderingHint(
					RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(baseImage,
				(this.getWidth () - newWidth ) / 2,
				(this.getHeight() - newHeight) / 2,
				newWidth, newHeight, this);
/*		// スケールドイメージがないか、サイズが異なれば
		synchronized(this) {
			if(this.scaledImage == null ||
					this.scaledImage.getWidth (this) != newWidth ||
					this.scaledImage.getHeight(this) != newHeight) {
				// イメージを作り直す
				this.scaledImage = this.baseImage.getScaledInstance(
						newWidth, newHeight,
						Image.SCALE_SMOOTH);
			}
		}
		// 描画
		g.drawImage(scaledImage,
				(this.getWidth () - newWidth ) / 2,
				(this.getHeight() - newHeight) / 2, this);
*/	}
}
