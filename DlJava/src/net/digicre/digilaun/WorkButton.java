package net.digicre.digilaun;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import net.digicre.digilaun.work.Work;

/**
 * 作品のボタンです。押すと概要ダイアログを開きます。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
class WorkButton extends JButton {
	/**
	 * アイコンの高さです。
	 */
	static final int ICON_HEIGHT = 64;
	/**
	 * このボタンの推奨サイズです。
	 */
	private static final Dimension PREFERRED_SIZE =
			new Dimension(6, ICON_HEIGHT+6);
	/**
	 * このボタンに関連付けられた <code>{@link Work}</code> です。
	 */
	private final Work relatedWork;

	/**
	 * このボタンの初期化処理です。
	 * イベントディスパッチスレッドで実行されます。
	 * @author p10090
	 *
	 */
	private class InitProc implements Runnable {
		/**
		 * <code>{@link WorkButton}</code> を初期化します。
		 */
		@Override
		public void run() {
			synchronized(WorkButton.this.relatedWork) {
				try {
					// アイコンを設定
					WorkButton.this.setIcon(new ImageIcon(
							ImageCache.getInstance().getImage(
									relatedWork.getIconFile()).
							getScaledInstance(
									-1, ICON_HEIGHT,
									Image.SCALE_SMOOTH)));
				}
				catch(Exception e) {
					WorkButton.this.setIcon(null);
				}
				// イベントを登録
				WorkButton.this.addActionListener(new ActionProc());
				// その他の調整
				java.awt.Font font = WorkButton.this.getFont();
				WorkButton.this.setFont(
						font.deriveFont(font.getSize2D()*2F));
				WorkButton.this.setHorizontalAlignment(
						SwingConstants.LEADING);
				WorkButton.this.setHorizontalTextPosition(
						SwingConstants.TRAILING);
				WorkButton.this.setVerticalTextPosition(
						SwingConstants.CENTER);
				WorkButton.this.setPreferredSize(
						PREFERRED_SIZE);
				WorkButton.this.setText(
						WorkButton.this.relatedWork.getName());
			}
		}
	}

	/**
	 * 作品ボタンが押されたときの処理です。
	 * 概要ダイアログを開きます。
	 * @author p10090
	 *
	 */
	private class ActionProc implements java.awt.event.ActionListener {
		/**
		 * 作品ボタンが押されたときの処理です。
		 * 概要ダイアログを開きます。
		 * @param arg0 イベント
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			SummaryDialog detailDialog = new SummaryDialog(
					((WorkButton)arg0.getSource()).relatedWork);
			detailDialog.setVisible(true);
		}
	}

	/**
	 * 新しい <code>WorkButton</code> を作成します。
	 * @param work
	 */
	WorkButton(Work work) {
		super();
		this.relatedWork = work;

		java.awt.EventQueue.invokeLater(new InitProc());
	}
}
