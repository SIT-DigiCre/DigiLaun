/**
 * 
 */
package net.digicre.digilaun;

import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 何かしらのウィンドウイベントが発生するたびに、
 * イベント発生元ウィンドウをデスクトップ領域全体にリサイズするイベントアダプターです。
 * 複数のインスタンスを作る意味が特にないため、
 * 今のところはシングルトンクラスです。
 * @author p10090
 *
 */
public class WindowMaximizer extends WindowAdapter {
	/**
	 * このクラスのインスタンスです。
	 */
	private static WindowMaximizer instance;

	private WindowMaximizer() {}

	/**
	 * このクラスのインスタンスを取得します。
	 * @return このクラスのインスタンス
	 */
	public synchronized static WindowMaximizer getInstance() {
		if(instance == null)
			instance = new WindowMaximizer();
		return instance;
	}

	/**
	 * ウィンドウの寸法を最大にします。
	 * @param window ウィンドウ
	 */
	private void resetBounds(Window window) {
		final Rectangle bounds = java.awt.GraphicsEnvironment.
				getLocalGraphicsEnvironment().getMaximumWindowBounds();

		window.setPreferredSize(bounds.getSize());
		window.setLocation(bounds.getLocation());
		window.setSize(window.getPreferredSize());
	}

	@Override
	public void windowActivated(WindowEvent e) {
		this.resetBounds(e.getWindow());
	}

	@Override
	public void windowClosed(WindowEvent e) {
		this.resetBounds(e.getWindow());
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.resetBounds(e.getWindow());
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		this.resetBounds(e.getWindow());
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		this.resetBounds(e.getWindow());
	}

	@Override
	public void windowIconified(WindowEvent e) {
		this.resetBounds(e.getWindow());
	}

	@Override
	public void windowOpened(WindowEvent e) {
		this.resetBounds(e.getWindow());
	}
}
