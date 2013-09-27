package net.digicre.digilaun;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import net.digicre.digilaun.work.Work;
import net.digicre.digilaun.work.WorkSet;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
	private static final String NEWEST_TEXT = "最新作";
	
	/**
	 * 過去作を示すラベルやメニュー項目のテキストです。
	 */
	private static final String OLDER_TEXT = "%sの作品";
	
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
	 * <code>{@link indexPanel}</code> に配置する子コンポーネントのサイズです。
	 */
	private static final Dimension MAIN_COMPONENTS_SIZE = new Dimension(6, 70);

	private static final int ICON_HEIGHT = MAIN_COMPONENTS_SIZE.height - 6;
	/**
	 * 画像のキャッシュオブジェクトです。
	 */
	//TODO:static ImageCache imgCache = new ImageCache();
	
	/**
	 * 作品集
	 */
	WorkSet works = new WorkSet();

	/**
	 * コマンドライン引数
	 */
	private static String[] args = null;
	
	/**
	 * 頒布モード
	 * 真の時は隠しコマンドを無効化し、普通に閉じて終了できるようになる
	 */
	private static boolean distributionMode = false;
	private Timer timer;
	
	private JFrame frmDigiLaun;
	private JPanel indexPanel;
	private JMenu chooseFromYearMnc;
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
					DigiLaun window = new DigiLaun();
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
	 * @return 頒布モードかどうか
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
		frmDigiLaun.setTitle("Digi Laun");
		frmDigiLaun.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// 頒布モードなら無視
				if(DigiLaun.getDistributionMode())
					return;
				// 終了   Ctrl+Shift+2
				if(arg0.isControlDown() &&
						arg0.isShiftDown() &&
						arg0.getKeyCode() == KeyEvent.VK_2)
					//System.exit(0);
					DigiLaun.this.frmDigiLaun.dispose();
				// リセット   Ctrl+Shift+-
				if(arg0.isControlDown() &&
						arg0.isShiftDown() &&
						arg0.getKeyCode() == KeyEvent.VK_MINUS)
					DigiLaun.this.resetFrame();
			}
		});
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
									ex.toString());
							
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
				timer.stop();
			}
		});
		frmDigiLaun.setBounds(100, 100, 640, 480);
		frmDigiLaun.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDigiLaun.getContentPane().setLayout(new BorderLayout(0, 0));
		
		headLabel = new JLabel("読み込みちふ…");
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
		indexPanel.setLayout(new GridLayout(0, 1, 0, 4));
		
		JMenuBar menuBar = new JMenuBar();
		frmDigiLaun.setJMenuBar(menuBar);
		
		chooseFromYearMnc = new JMenu("制作時期から選ぶ(C)");
		chooseFromYearMnc.setMnemonic('C');
		menuBar.add(chooseFromYearMnc);
	}
	
	/**
	 * 作品集を元に、作品に対応したボタンを
	 * <code>{@link indexPanel}</code> に登録します。
	 * このメソッドは、イベントディスパッチスレッドからアクセスされます。
	 */
	private void initIndexPanel() {
		// 前のループで処理した作品オブジェクト
		Work workPrev = null;
		// ボタンを追加した作品の数
		//int worksCount = 0;
		
		for(Work work : works) {
			// 前の作品と年度が違えば年度ラベルを追加
			if(workPrev == null || workPrev.getYear() != work.getYear())
				makeYearComponents(work.getYear());
			// ボタンを追加
			makeWorkButton(work);
			/*
			// ボタンの文字列をセット
			button.setText(work.getName());
			// アクションキーを追加できればアクションキーを追加
			if(worksCount < BUTTON_ACTION_KEYS.length()) {
				char actionKey = BUTTON_ACTION_KEYS.charAt(worksCount);
				button.setText(button.getText()+"("+actionKey+")");
				button.setMnemonic(actionKey);
			}
			// ボタンのアイコンと、テキストの位置をセット
			try {
				button.setIcon(
					new ImageIcon(imgCache.get(work.getIconPath())));
				button.setHorizontalTextPosition(SwingConstants.CENTER);
				button.setVerticalTextPosition(SwingConstants.BOTTOM);
			}
			catch(Exception e) {}
			
			// サイズを整えて追加
			button.setPreferredSize(MAIN_COMPONENTS_SIZE);
			this.indexPanel.add(button);
			//*/
			
			workPrev = work;
			//++worksCount;
		}
		this.indexPanel.repaint();
		
		// コンポーネントの追加が終わったら、
		// 最後のイベントとしてタイマーリセット処理を入れる
		this.timer.start();
	}

	/**
	 * 制作年度を示すラベルとメニュー項目を
	 * <code>{@link indexPanel}</code> に追加します。
	 * @param year 制作年度
	 */
	private void makeYearComponents(int year) {
		// 基本情報を準備
		final Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.MONTH, -3);
		final int leftYears = calendar.get(Calendar.YEAR) - year;
		
		// アイコンを準備
		String imgFilename = String.format(
				"img/LabelBack%s.png", leftYears != 0 ? "Older" : "Newest");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(imgFilename).getScaledInstance(
				-1, DigiLaun.MAIN_COMPONENTS_SIZE.height,
				Image.SCALE_SMOOTH);

		// ラベルの情報を準備
		final String labelText = leftYears == 0 ?
				DigiLaun.NEWEST_TEXT : String.format(DigiLaun.OLDER_TEXT,
						leftYears == 1 ? "昨年度" :
							Integer.toString(year) + "年度");
		final ImageIcon labelIcon = new ImageIcon(img);
		
		// メニュー項目の情報を準備
		final char itemMnemonic =
				leftYears == 0 ? 'N' : (
				leftYears == 1 ? 'P' : '\0');
		final String itemText = labelText + (
				itemMnemonic != '\0' ? "("+itemMnemonic+")" : "");

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// ラベルを追加
				final JLabel label = new JLabel(labelText);

				Font font = label.getFont();
				label.setFont(font.deriveFont(font.getSize2D()*4F));
				label.setIcon(labelIcon);
				label.setVerticalAlignment(SwingConstants.BOTTOM);
				label.setHorizontalTextPosition(SwingConstants.TRAILING);
				label.setVerticalTextPosition(SwingConstants.CENTER);
				label.setPreferredSize(DigiLaun.MAIN_COMPONENTS_SIZE);
				indexPanel.add(label);
				
				// メニュー項目を追加
				JMenuItem item = new JMenuItem(itemText);
				item.setMnemonic(itemMnemonic);
				item.addActionListener(new ActionListener() {
					// 項目をクリックしたら
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// 指定した年度ラベルへスクロール
						/*
						java.awt.Point p = label.getLocation();
						p.x = Math.min(p.x,
								DigiLaun.this.indexPanel.getWidth() -
								DigiLaun.this.indexScrollPane.
								getViewport().getWidth());
						p.y = Math.min(p.y,
								DigiLaun.this.indexPanel.getHeight() -
								DigiLaun.this.indexScrollPane.
								getViewport().getHeight());
						//*/
						DigiLaun.this.indexScrollPane.getViewport().
						setViewPosition(label.getLocation());
 						DigiLaun.this.indexScrollPane.getViewport().repaint();
					}
				});
				DigiLaun.this.chooseFromYearMnc.add(item);
			}
		});
	}

	/**
	 * 作品のボタンを <code>{@link indexPanel}</code> に追加します。
	 * @param work 作品
	 */
	private void makeWorkButton(final Work work) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(work.getIconPath()).getScaledInstance(
				-1, DigiLaun.ICON_HEIGHT,
				Image.SCALE_SMOOTH);
		final ImageIcon icon = work.getIconPath() != null ?
				new ImageIcon(img) : null;
		final ActionListener action = new ActionListener() {
			// ボタンがクリックされたときの処理
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 詳細ウィンドウを開く
/*				JFrame parent;
				{
					java.awt.Container container;
					for(
							container =
								((Component)arg0.getSource()).getParent();
							container != null;
							container = container.getParent()) {
						if(container instanceof JFrame) {
							parent = (JFrame)container;
							break;
						}
					}
				}
*/				DetailDialog detailDialog = new DetailDialog(work);
				detailDialog.setVisible(true);
			}
		};
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// ボタンを追加
				JButton button = new JButton(work.getName());
				
				Font font = button.getFont();
				button.setFont(font.deriveFont(font.getSize2D()*2F));
				button.setIcon(icon);
				button.addActionListener(action);
				button.setHorizontalAlignment(SwingConstants.LEADING);
				button.setHorizontalTextPosition(SwingConstants.TRAILING);
				button.setVerticalTextPosition(SwingConstants.CENTER);
				button.setPreferredSize(MAIN_COMPONENTS_SIZE);
				DigiLaun.this.indexPanel.add(button);
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
			javax.swing.JOptionPane.showMessageDialog(DigiLaun.this.frmDigiLaun,
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

}
