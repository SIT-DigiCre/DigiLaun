package net.digicre.digilaun;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.Arrays;
import java.util.ListIterator;
import javax.swing.JFrame;

import net.digicre.digilaun.work.Work;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.AWTEventListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.CardLayout;

/**
 * Digi Laun のエントリーポイントと、コマンドライン引数の取得機能を提供します。
 * 内部では主ウィンドウに関する処理も行います。
 * @author p10090
 */
public class DigiLaun {
	/**
	 * タイマーのリセット時にウィンドウを隠す時間 [ms] です。
	 */
	private static final long RESET_HIDING_TIME = 2000L;

	/**
	 * 頒布モード OFF の時に閉じようとしたとき、表示するメッセージです。
	 */
	private static final String CANNOT_CLOSE_MESSAGE =
			"やめたいときは,\n" +
			"デジクリのふだをかけた人に\n" +
			"こえをかけてください.";

	/**
	 * 頒布モード OFF で長時間遊び続けたときのテキストです。
	 */
	private static final String TIMEUP_TEXT =
			"たいけんをはじめてから 1 時間たちました。\n" +
            "そろそろきゅうけいしましょう。";
	/*
	 * 作品ボタンに順に割り当てるアクションキーの文字列です。
	 *
	private static final String BUTTON_ACTION_KEYS =
			"QWERTYUIOPASDFGHJKLZXCVBNM";
	//*/

	/**
	 * Digi Laun の環境設定です。
	 */
	static final Config config = new Config();

	/**
	 * {@link DigiLaun#main(String[])} に与えられたコマンドライン引数です。
	 */
	private static String[] args = null;

	/**
	 * 連続使用時間を監視するタイマーです。
	 */
	private Timer timer;

	private JFrame frmDigiLaun;
	private JPanel indexPanel;
	private JLabel headLabel;
	private JScrollPane indexScrollPane;
	private JPanel mainPanel;

	/**
	 * アプリケーションを起動します。
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		// 引数を保存
		DigiLaun.args = args;

		// インスタンスを作成
		final DigiLaun window = new DigiLaun();

		// スプラッシュを表示してコンフィグ読み込み
		SplashFrame splashFrame = new SplashFrame();
		splashFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// スプラッシュが閉じられたらメインフレームを表示
				window.setFullScreenMode(
						DigiLaun.config.getMode() == Config.Mode.DISPLAY);
				window.frmDigiLaun.setVisible(true);
			}
		});
		splashFrame.setVisible(true);
	}

	/**
	 * コマンドライン引数を取得します。
	 * @return コマンドライン引数
	 */
	public static String[] getArguments() {
		return Arrays.copyOf(args, args.length);
	}

	/**
	 * Create the application.
	 */
	public DigiLaun() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDigiLaun = new JFrame();
		frmDigiLaun.setBounds(new Rectangle(0, 0, 800, 600));
		frmDigiLaun.setTitle("Digi Laun");
		frmDigiLaun.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				DigiLaun.this.onWindowOpened();
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				DigiLaun.this.onWindowClosing();
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
				DigiLaun.this.onWindowClosed();
			}
		});
		frmDigiLaun.setBounds(100, 100, 640, 480);
		frmDigiLaun.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDigiLaun.getContentPane().setLayout(new CardLayout(0, 0));
		
				JLabel splashLabel = new JLabel("じゆむびちふ");
				splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
				splashLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
				splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
				splashLabel.setFont(splashLabel.getFont().deriveFont(23f));
				splashLabel.setIcon(new ImageIcon("img/Splash.png"));
				splashLabel.setOpaque(true);
				splashLabel.setBackground(Color.WHITE);
				frmDigiLaun.getContentPane().add(splashLabel, "name_1386749295936071000");
		
		mainPanel = new JPanel();
		frmDigiLaun.getContentPane().add(mainPanel, "name_1386749345977117000");
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		headLabel = new JLabel((String) null);
		mainPanel.add(headLabel, BorderLayout.NORTH);
		headLabel.setPreferredSize(new Dimension(91, 70));
		headLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		headLabel.setIcon(new ImageIcon("img/Logo.png"));
		headLabel.setOpaque(true);
		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headLabel.setFont(headLabel.getFont().deriveFont(headLabel.getFont().getStyle() | Font.BOLD, 23f));

		indexScrollPane = new JScrollPane();
		mainPanel.add(indexScrollPane, BorderLayout.CENTER);
		indexScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		indexScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		indexPanel = new JPanel();
		indexScrollPane.setViewportView(indexPanel);
		GridBagLayout gbl_indexPanel = new GridBagLayout();
		gbl_indexPanel.columnWidths = new int[]{0};
		gbl_indexPanel.rowHeights = new int[]{0};
		gbl_indexPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_indexPanel.rowWeights = new double[]{Double.MIN_VALUE};
		indexPanel.setLayout(gbl_indexPanel);
	}

	/**
	 * ウィンドウが開かれたときのイベント処理です。
	 */
	private void onWindowOpened() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				initIndexPanel();
			}
		});
	}

	/**
	 * ウィンドウを閉じる要求を受けたときのイベント処理です。
	 */
	private void onWindowClosing() {
		// 頒布モードなら普通に閉じる
		if(config.getMode() == Config.Mode.DISTRIBUTION) {
			this.frmDigiLaun.dispose();
			return;
		}
		// そうでなければメッセージ表示
		javax.swing.JOptionPane.showMessageDialog(this.frmDigiLaun,
				CANNOT_CLOSE_MESSAGE);
	}

	/**
	 * ウィンドウが閉じられたときのイベント処理です。
	 */
	protected void onWindowClosed() {
		timer.stop();
	}

	/**
	 * 作品集を元に、作品に対応したボタンを
	 * <code>{@link #indexPanel}</code> に登録します。
	 * このメソッドは、イベントディスパッチスレッドからアクセスされます。
	 */
	private void initIndexPanel() {
		// 制作年度ごとの子パネルを追加
		ListIterator<Work> i = config.getWorks().listIterator();
		GridBagLayout gbl = (GridBagLayout)this.indexPanel.getLayout();
		while(i.hasNext()) {
			YearWorksPanel panel = new YearWorksPanel(i);
			GridBagConstraints gbc = gbl.getConstraints(panel);

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weighty = config.getWorks().getFirst().getYear() - panel.getYear();
			gbc.anchor = GridBagConstraints.NORTH;

			gbl.setConstraints(panel, gbc);
			this.indexPanel.add(panel);
		}
		{
			JLabel blank = new JLabel();
			GridBagConstraints gbc = gbl.getConstraints(blank);

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weighty = Integer.MAX_VALUE+1.0;
			gbc.anchor = GridBagConstraints.NORTH;

			gbl.setConstraints(blank, gbc);
			this.indexPanel.add(blank);
		}
		// すべての子コンポーネントのイベントを監視し、キーイベントを処理
		this.indexPanel.getToolkit().addAWTEventListener(
				new AWTEventListener() {
					@Override
					public void eventDispatched(AWTEvent arg0) {
						synchronized(arg0) {
							if(!(arg0.getSource() instanceof Component))
								return;
							if(!DigiLaun.this.frmDigiLaun.isActive())
								return;
							for(Component c = (Component)arg0.getSource();
									c != null; c = c.getParent()) {
								if(c == DigiLaun.this.frmDigiLaun)
									DigiLaun.this.processKeyCommand(
											(KeyEvent)arg0);
							}
						}
					}
				}, AWTEvent.KEY_EVENT_MASK);

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// コンポーネントの追加が終わったら、
				// 主パネルを表示してタイマーリセット
				DigiLaun.this.showMainPanel();
				(DigiLaun.this.timer = new Timer(
						DigiLaun.this.headLabel, new TimerCallback())
				).start();
			}
		});

	}

	/**
	 * タイマーが時間切れになったときのコールバックを提供します。
	 * @author p10090
	 *
	 */
	private class TimerCallback implements Runnable {
		/**
		 * アプリ窓を凍結し、作品を体験できなくします。
		 * アプリ作品を体験中であれば、そのアプリを終了します。
		 */
		@Override
		public void run() {
			// メッセージを表示
			javax.swing.JOptionPane.showMessageDialog(
					DigiLaun.this.frmDigiLaun,
	                DigiLaun.TIMEUP_TEXT);
			// ボタン等を無効化
			setButtonsEnabled(false);
		}
	}

	/**
	 * タイマーをリセットし、アプリ窓の凍結を解除します。
	 */
	void resetFrame() {
		// タイマーを止める
		timer.stop();

		// ウィンドウを隠す
		this.showSplashLabel();
		this.frmDigiLaun.repaint();

		// ボタン等を有効化
		this.setButtonsEnabled(true);
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// スクロール位置をリセット
				DigiLaun.this.indexScrollPane.getViewport().setAlignmentY(0);

				// 一定時間待つ
				try {
					Thread.sleep(DigiLaun.RESET_HIDING_TIME);
				}
				catch(InterruptedException e) {
				}

				// ウィンドウを再描画して表示
				DigiLaun.this.mainPanel.repaint();
				DigiLaun.this.showMainPanel();

				// タイマーを始める
				DigiLaun.this.timer.start();
			}
		});
	}

	/**
	 * スプラッシュ画面を表示します。
	 */
	private void showSplashLabel() {
		((CardLayout)this.frmDigiLaun.getContentPane().getLayout()).show(
				this.frmDigiLaun.getContentPane(), "name_1386749295936071000");
	}

	/**
	 * 主パネルを表示します。
	 */
	private void showMainPanel() {
		((CardLayout)this.frmDigiLaun.getContentPane().getLayout()).show(
				this.frmDigiLaun.getContentPane(), "name_1386749345977117000");
	}

	/**
	 * ボタン等の有効・無効をセットします。
	 * @param b ボタン等の有効状態
	 */
	private void setButtonsEnabled(boolean b) {
		for(Component c : indexPanel.getComponents())
			c.setEnabled(b);
	}

	/**
	 * 隠しキー操作を処理します。
	 * @param e
	 */
	private void processKeyCommand(KeyEvent e) {
		// 頒布モードなら無視
		if(config.getMode() == Config.Mode.DISTRIBUTION)
			return;
		// 終了   Ctrl+Shift+2
		if(e.isControlDown() &&
				e.isShiftDown() &&
				e.getKeyCode() == KeyEvent.VK_2)
			//System.exit(0);
			DigiLaun.this.frmDigiLaun.dispose();
		// リセット   Ctrl+Shift+-
		if(e.isControlDown() &&
				e.isShiftDown() &&
				e.getKeyCode() == KeyEvent.VK_MINUS)
			DigiLaun.this.resetFrame();
	}

	/**
	 * 全画面モードをセットします。
	 * @param status 真にすると全画面表示になる
	 */
	private void setFullScreenMode(final boolean status) {
		final Runnable process = new Runnable() {
			@Override
			public void run() {
				frmDigiLaun.setResizable(!status);
				frmDigiLaun.setUndecorated(status);
				if(status)
					frmDigiLaun.addWindowListener(
							WindowMaximizer.getInstance());
				else
					frmDigiLaun.removeWindowListener(
							WindowMaximizer.getInstance());
			}
		};
		if(java.awt.EventQueue.isDispatchThread())
			process.run();
		else
			try {
				java.awt.EventQueue.invokeAndWait(process);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
