package net.digicre.digilaun;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Digi Laun のスプラッシュを表示しながらコンフィグを読み込むフレームです。
 * @author p10090
 */
@SuppressWarnings("serial")
public class SplashFrame extends JFrame {
	/**
	 * コンフィグを読み込むスレッドです。
	 * @author p10090
	 */
	private class ConfigLoadingThread extends Thread {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			try {
				// コンフィグ読み込み
				try {
					DigiLaun.config.readFromXMLDocument();
				}
				catch(Exception rex1) {
					try {
						DigiLaun.config.getWorks().readFromXMLDocument();
					}
					catch(Throwable rex2) {
						throw rex1;
					}
				}
				// 正常に読めたらスプラッシュを閉じる
				java.awt.EventQueue.invokeAndWait(new Runnable(){
					@Override
					public void run() {
						SplashFrame.this.dispose();
					}
				});
			} catch (final Exception ex) {
				// 読めなかったらエラーメッセージを表示して終了
				java.awt.EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						String str = String.format("%s\n\n%s",
								WORKS_READING_FAILURE_MESSAGE,
								ex.getLocalizedMessage());

						for(StackTraceElement st : ex.getStackTrace()) {
							str += "\n        at " + st.toString();
						}
						javax.swing.JOptionPane.showMessageDialog(
								SplashFrame.this, str);
						System.exit(ErrorCodes.CANNOT_READ_CONFIG);
					}
				});
			}
		}
	}

	/**
	 * XML ファイル読み込み失敗時に表示するメッセージです。
	 */
	private static final String WORKS_READING_FAILURE_MESSAGE =
			"作品ファイルの読み込みに失敗しました。";

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public SplashFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				new SplashFrame.ConfigLoadingThread().start();
			}
		});
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 640, 360);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		final Rectangle bounds = java.awt.GraphicsEnvironment.
				getLocalGraphicsEnvironment().getMaximumWindowBounds();
		setLocation(bounds.x + (bounds.width-getWidth())/2,
				bounds.y + (bounds.height-getHeight())/2);
		
		JLabel splashLabel = new JLabel("読み込みちふ...");
		splashLabel.setFont(splashLabel.getFont().deriveFont(23f));
		splashLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
		splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		splashLabel.setIcon(new ImageIcon("img/Splash.png"));
		splashLabel.setOpaque(true);
		splashLabel.setBackground(Color.WHITE);
		contentPane.add(splashLabel);
	}

}
