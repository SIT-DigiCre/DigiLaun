package net.digicre.digilaun;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
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

public class DigiLaun {
	/**
	 * タイマーのリセット時にウィンドウを隠す時間 [ms] です。
	 */
	private final long RESET_HIDING_TIME = 2000L;

	/**
	 * XML ファイル読み込み失敗時に表示するメッセージです。
	 */
	private final String WORKS_READING_FAILURE_MESSAGE =
			"作品ファイルの読み込みに失敗しました。";
	
	/**
	 * 作品ボタンに順に割り当てるアクションキーの文字列です。
	 */
	private final String BUTTON_ACTION_KEYS =
			"QWERTYUIOPASDFGHJKLZXCVBNM";
	
	/**
	 * 中央パネルの子コンポーネントのサイズです。
	 */
	private final Dimension MAIN_COMPONENTS_SIZE = new Dimension(0, 160);

	/**
	 * 画像のキャッシュオブジェクトです。
	 */
	private ImageCache imgCache = new ImageCache();
	
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
	private JFrame frame;
	private JPanel indexPanel;
	private JMenu chooseFromYearMnc;
	private JLabel headLabel;

	/**
	 * Launch the application.
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DigiLaun window = new DigiLaun();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.addKeyListener(new KeyAdapter() {
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
					DigiLaun.this.frame.dispose();
				// リセット   Ctrl+Shift+-
				if(arg0.isControlDown() &&
						arg0.isShiftDown() &&
						arg0.getKeyCode() == KeyEvent.VK_MINUS)
					DigiLaun.this.resetFrame();
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				new Thread() {
					public void run() {
						try {
							timer = new Timer(headLabel);
							works.readFromXMLDocument();
						} catch (Exception ex) {
							String str = String.format("%s\n\n%s",
									DigiLaun.this.WORKS_READING_FAILURE_MESSAGE,
									ex.toString());
							
							for(StackTraceElement st : ex.getStackTrace()) {
								str += "\n        at " + st.toString();
							}
							javax.swing.JOptionPane.showMessageDialog(
									frame, str);
							frame.dispose();
							return;
							//System.exit(1);
						}
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								DigiLaun.this.initIndexPanel();
							}
						});
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
						"やめたいときは, "+
						"デジクリのふだを"+
						"かけた人に\nこえを"+
						"かけてください.");
			}
		});
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		headLabel = new JLabel("読み込みちふ…");
		headLabel.setBackground(Color.WHITE);
		headLabel.setOpaque(true);
		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headLabel.setFont(headLabel.getFont().deriveFont(headLabel.getFont().getStyle() | Font.BOLD, 23f));
		frame.getContentPane().add(headLabel, BorderLayout.NORTH);
		
		JScrollPane indexScrollPane = new JScrollPane();
		frame.getContentPane().add(indexScrollPane, BorderLayout.CENTER);
		indexScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		indexScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		indexPanel = new JPanel();
		indexScrollPane.setViewportView(indexPanel);
		indexPanel.setLayout(new GridLayout(0, 1, 2, 2));
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		chooseFromYearMnc = new JMenu("制作時期から選ぶ(C)");
		chooseFromYearMnc.setMnemonic('C');
		menuBar.add(chooseFromYearMnc);
		
		JMenuItem mntmn = new JMenuItem("最新作(N)");
		mntmn.setMnemonic('N');
		chooseFromYearMnc.add(mntmn);
	}
	
	/**
	 * 作品集を元に、作品に対応したボタンを
	 * <code>{@link indexPanel}</code> に登録します。
	 * このメソッドは、イベントディスパッチスレッドからアクセスされます。
	 */
	private void initIndexPanel() {
		// メニューで数字ニーモニックが使用済みかどうか
		boolean[] menuMnemonicUsed = new boolean[10];
		// 前のループで処理した作品オブジェクト
		Work workPrev = null;
		// ボタンを追加した作品の数
		int worksCount = 0;
		
		for(Work work : works) {
			JButton button = new JButton();
			// ボタンのフォントを既定値の2倍に設定
			{
				Font font = button.getFont();
				button.setFont(font.deriveFont(font.getSize2D()*2F));
			}
			
			// 前の作品と年度が違えば年度ラベルを追加
			if(workPrev == null || workPrev.getYear() > work.getYear()) {
				// 追加する年度ラベル (フォントは既定値の3倍サイズ)
				JLabel label = new JLabel();
				{
					Font font = label.getFont();
					label.setFont(font.deriveFont(font.getSize2D()*4F));
				}
				
				// 最新作でなければ
				if(workPrev != null) {
					// 「n年度前」または「昨年度」テキストをセット
					Calendar calendar = Calendar.getInstance();
					calendar.roll(Calendar.MONTH, -3);

					label.setText(String.format("%s年度\nの作品",
							calendar.get(Calendar.YEAR) == work.getYear()+1
							? "昨" : Short.toString(work.getYear())));
					// 背景画像をセット
					label.setIcon(new ImageIcon(imgCache.get(
							"img/LabelBackOlder.png")));
					
					// メニューにジャンプ項目を追加
					JMenuItem mnitm = new JMenuItem(label.getText());
					if(!menuMnemonicUsed[work.getYear()%10]) {
						menuMnemonicUsed[work.getYear()%10] = true;
						mnitm.setMnemonic(new int[]{
								KeyEvent.VK_0,
								KeyEvent.VK_1,
								KeyEvent.VK_2,
								KeyEvent.VK_3,
								KeyEvent.VK_4,
								KeyEvent.VK_5,
								KeyEvent.VK_6,
								KeyEvent.VK_7,
								KeyEvent.VK_8,
								KeyEvent.VK_9,
						}[work.getYear()%10]);
						if(mnitm.getText().indexOf('0'+work.getYear()%10) < 0) {
							mnitm.setMnemonic('P');
							mnitm.setText(mnitm.getText() + "(P)");
						}
					}
					this.chooseFromYearMnc.add(mnitm);
				}
				// 最新作なら
				else {
					// 「最新作」テキストをセット
					label.setText("最新作");
					// 背景画像をセット
					label.setIcon(new ImageIcon(this.imgCache.get(
							"img/LabelBackNewest.png")));
				}
				// 背景画像とテキストの位置関係をセット
				label.setHorizontalTextPosition(SwingConstants.TRAILING);
				label.setVerticalTextPosition(SwingConstants.CENTER);
				// サイズを整えて追加
				label.setPreferredSize(this.MAIN_COMPONENTS_SIZE);
				this.indexPanel.add(label);
			}
			// ボタンの文字列をセット
			button.setText(work.getName());
			// アクションキーを追加できればアクションキーを追加
			if(worksCount < this.BUTTON_ACTION_KEYS.length()) {
				char actionKey = this.BUTTON_ACTION_KEYS.charAt(worksCount);
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
			button.setPreferredSize(this.MAIN_COMPONENTS_SIZE);
			this.indexPanel.add(button);
			
			workPrev = work;
			++worksCount;
		}
		this.indexPanel.repaint();
		
		// コンポーネントの追加が終わったら、
		// 最後のイベントとしてタイマーリセット処理を入れる
		this.timer.start();
	}

	/**
	 * アプリ窓を凍結し、作品を体験できなくします。
	 * アプリ作品を体験中であれば、そのアプリを終了します。
	 */
	void freeze() {
		javax.swing.JOptionPane.showMessageDialog(DigiLaun.this.frame,
                "ゲームを始めて1時間たちました。\n" +
                "そろそろきゅうけいしましょう。");
		DigiLaun.this.indexPanel.setEnabled(false);
	}
	
	/**
	 * タイマーをリセットし、アプリ窓の凍結を解除します。
	 */
	void resetFrame() {
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					DigiLaun.this.frame.setVisible(false);
					try {
						Thread.sleep(DigiLaun.this.RESET_HIDING_TIME);
					}
					catch(InterruptedException e) {
					}
					DigiLaun.this.frame.repaint();
					DigiLaun.this.frame.setVisible(true);
				}
				
			});
			timer.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
