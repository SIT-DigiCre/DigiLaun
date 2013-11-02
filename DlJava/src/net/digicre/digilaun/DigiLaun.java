package net.digicre.digilaun;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import net.digicre.digilaun.work.Work;
import net.digicre.digilaun.work.WorkList;
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

/**
 * Digi Laun のエントリーポイントと、コマンドライン引数の取得機能を提供します。
 * 内部では主ウィンドウに関する処理も行います。
 * @author p10090
 *
 */
public class DigiLaun {
	/**
	 * タイマーのリセット時にウィンドウを隠す時間 [ms] です。
	 */
	private static final long RESET_HIDING_TIME = 2000L;

	/**
	 * XML ファイル読み込み失敗時に表示するメッセージです。
	 */
	private static final String WORKS_READING_FAILURE_MESSAGE =
			"作品ファイルの読み込みに失敗しました。";

	/**
	 * 頒布モード OFF の時に閉じようとしたとき、表示するメッセージです。
	 */
	private static final String CANNOT_CLOSE_MESSAGE =
			"やめたいときは,\n" +
			"デジクリのふだをかけた人に\n" +
			"こえをかけてください.";

	/**
	 * 最新作を示すラベルやメニュー項目のテキストです。
	 */
	static final String NEWEST_TEXT = "最新作";

	/**
	 * 過去作を示すラベルやメニュー項目のテキストです。
	 */
	static final String OLDER_TEXT = "%sの作品";

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
	 * 作品の集合オブジェクトです。
	 */
	WorkList works = new WorkList();

	/**
	 * コマンドライン引数です。
	 */
	private static String[] args = null;

	/**
	 * 頒布モードのフラグです。
	 * 真の時は隠しコマンドを無効化し、普通に閉じて終了できるようになります。
	 */
	private static boolean distributionMode = false;

	/**
	 * 連続使用時間を監視するタイマーです。
	 */
	private Timer timer;

	/**
	 * グラフィックス環境オブジェクトです。全画面表示に使います。
	 */
	GraphicsEnvironment ge = null;
	/**
	 * 全画面表示に使うモニターです。
	 */
	GraphicsDevice gd = null;

	private JFrame frmDigiLaun;
	private JPanel indexPanel;
	private JLabel headLabel;
	private JScrollPane indexScrollPane;

	/**
	 * アプリケーションを起動します。
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		// 引数を処理
		DigiLaun.args = args;
		DigiLaun.distributionMode = true;

		for(String arg : args) {
			// "/DISPLAY", "--display" オプションがなければ頒布モード 
			if(Pattern.compile(
					"\\A(?:/|\\-\\-)display\\z", Pattern.CASE_INSENSITIVE
					).matcher(arg).matches())
				DigiLaun.distributionMode = false;
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					javax.swing.UIManager.setLookAndFeel(
//							javax.swing.UIManager.
//							getSystemLookAndFeelClassName());
					DigiLaun window = new DigiLaun();
//					// 展示モードなら全画面
//					if(!DigiLaun.getDistributionMode())
//						window.setFullScreenMode(true);
					window.frmDigiLaun.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * コマンドライン引数を取得します。
	 * @return コマンドライン引数
	 */
	public static String[] getArguments() {
		return Arrays.copyOf(args, args.length);
	}

	/**
	 * 頒布モードかどうかを取得します。
	 * <br>頒布モードが入っていると、 Digi Laun を
	 * [閉じる] ボタンで終了できます。
	 * @return 頒布モードなら真
	 */
	public static boolean getDistributionMode() {
		return distributionMode;
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
				new Thread() {
					public void run() {
						try {
							timer = new Timer(headLabel, new TimerCallback());
							works.readFromXMLDocument();
						} catch (Exception ex) {
							String str = String.format("%s\n\n%s",
									DigiLaun.WORKS_READING_FAILURE_MESSAGE,
									ex.getLocalizedMessage());

							for(StackTraceElement st : ex.getStackTrace()) {
								str += "\n        at " + st.toString();
							}
							javax.swing.JOptionPane.showMessageDialog(
									frmDigiLaun, str);
							frmDigiLaun.dispose();
							return;
							//System.exit(1);
						}
						DigiLaun.this.initIndexPanel();
					}
				}.start();
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				// 頒布モードなら普通に閉じる
				if(DigiLaun.getDistributionMode()) {
					arg0.getWindow().dispose();
					return;
				}
				// そうでなければメッセージ表示
				javax.swing.JOptionPane.showMessageDialog(arg0.getWindow(),
						DigiLaun.CANNOT_CLOSE_MESSAGE);
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
				if(getFullScreenMode())
					DigiLaun.this.setFullScreenMode(false);
				timer.stop();
//				System.exit(0);
			}
		});
		frmDigiLaun.setBounds(100, 100, 640, 480);
		frmDigiLaun.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDigiLaun.getContentPane().setLayout(new BorderLayout(0, 0));

		headLabel = new JLabel("読み込みちふ…");
		headLabel.setPreferredSize(new Dimension(91, 70));
		headLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		headLabel.setIcon(new ImageIcon("img/Logo.png"));
		headLabel.setBackground(Color.WHITE);
		headLabel.setOpaque(true);
		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headLabel.setFont(headLabel.getFont().deriveFont(headLabel.getFont().getStyle() | Font.BOLD, 23f));
		frmDigiLaun.getContentPane().add(headLabel, BorderLayout.NORTH);

		indexScrollPane = new JScrollPane();
		frmDigiLaun.getContentPane().add(indexScrollPane, BorderLayout.CENTER);
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
	 * 作品集を元に、作品に対応したボタンを
	 * <code>{@link #indexPanel}</code> に登録します。
	 * このメソッドは、イベントディスパッチスレッドからアクセスされます。
	 */
	private void initIndexPanel() {
		// 制作年度ごとの子パネルを追加
		ListIterator<Work> i = works.listIterator();
		GridBagLayout gbl = (GridBagLayout)this.indexPanel.getLayout();
		while(i.hasNext()) {
			YearWorksPanel panel = new YearWorksPanel(i);
			GridBagConstraints gbc = gbl.getConstraints(panel);

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weighty = works.getFirst().getYear() - panel.getYear();
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
//		this.indexPanel.repaint();

		// コンポーネントの追加が終わったら、
		// 最後のイベントとしてタイマーリセット処理を入れる
		this.timer.start();
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
		frmDigiLaun.setVisible(false);

		// ボタン等を有効化
		setButtonsEnabled(true);
		// スクロール位置をリセット
		this.indexScrollPane.getViewport().setAlignmentY(0);

		// 一定時間待つ
		try {
			Thread.sleep(DigiLaun.RESET_HIDING_TIME);
		}
		catch(InterruptedException e) {
		}

		// ウィンドウを再描画して表示
		DigiLaun.this.frmDigiLaun.repaint();
		DigiLaun.this.frmDigiLaun.setVisible(true);

		// タイマーを始める
		timer.start();
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
		if(DigiLaun.getDistributionMode())
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
	 * 全画面モードかどうかを調べます。
	 * @return 全画面モードなら真
	 */
	private boolean getFullScreenMode() {
		synchronized(this) {
			return gd == null || gd.getFullScreenWindow() == frmDigiLaun;
		}
	}

	/**
	 * 全画面モードをセットします。
	 * @param status 真にすると全画面表示になる
	 */
	private void setFullScreenMode(final boolean status) {
		// 画面デバイスが未取得なら取得
		synchronized(this) {
			if(gd == null) {
				if(ge == null)
					ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				gd = ge.getDefaultScreenDevice();
			}
		}
//		EventQueue.invokeLater(new Runnable() {
//			@Override
//			public void run() {
				frmDigiLaun.setResizable(!status);
				frmDigiLaun.setUndecorated(!status);
				gd.setFullScreenWindow(status ? frmDigiLaun : null);
//			}
//		});
	}
}
