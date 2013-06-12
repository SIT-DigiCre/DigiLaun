package net.digicre.digilaun;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import net.digicre.digilaun.work.Work;
import net.digicre.digilaun.work.WorkSet;

@SuppressWarnings("serial")
class MainJFrame extends JFrame {
	private class ComponentAdder implements Runnable {
		private JComponent component;
		ComponentAdder(JComponent component) {
			this.component = component;
		}
		@Override
		public void run() {
			MainJFrame.this.scrollPane.add(this.component);
		}
	}
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
			"1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
	
	/**
	 * 中央パネルの、コンポーネントのサイズです。
	 */
	private final Dimension MAIN_COMPONENTS_SIZE = new Dimension(250, 250);

	/**
	 * 画像のキャッシュオブジェクトです。
	 */
	private ImageCache imgCache = new ImageCache();
	/**
	 * アプリ窓を含む、このアプリのインスタンスです。
	 */
	static DigiLaun window;
	
	/**
	 * 作品集
	 */
	WorkSet works = new WorkSet();

	private JScrollPane scrollPane;
	private TimeLabel timeLabel;

	/**
	 * Create the application.
	 */
	public MainJFrame() {
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// 既定のフォントを変更
		FontUIResource fontUIResource = new FontUIResource(
				new Font("MS UI Gothic", Font.PLAIN, 12));
		for(java.util.Map.Entry<Object, Object> entry :
			UIManager.getDefaults().entrySet()) {
		    if(entry.getKey().toString().toLowerCase().endsWith("font")) {
		      UIManager.put(entry.getKey(), fontUIResource);
		    }
		  }
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						try {
							works.readFromXMLDocument();
						} catch (Exception e) {
							String str = String.format("%s\n\n%s",
									WORKS_READING_FAILURE_MESSAGE,
									e.toString());
							
							for(StackTraceElement st : e.getStackTrace()) {
								str += "\n        at " + st.toString();
							}
							javax.swing.JOptionPane.showMessageDialog(
									MainJFrame.this, str);
							MainJFrame.this.dispose();
							return;
							//System.exit(1);
						}
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								MainJFrame.this.initScrollPane();
							}
						});
					}
				}.start();
			}
			public void windowClosing(WindowEvent arg0) {
				javax.swing.JOptionPane.showMessageDialog(MainJFrame.this,
						"やめたいときは、"+
						"デジクリのふだを"+
						"かけた人に\nこえを"+
						"かけてください。");
			}
			//*
			public void windowClosed(WindowEvent arg0) {
				//System.exit(0);
				timeLabel.stopTimer();
			}
			//*/
		});
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// 終了   Ctrl+Shift+2
				if(e.isControlDown() &&
						e.isShiftDown() &&
						e.getKeyCode() == KeyEvent.VK_2)
					//System.exit(0);
					MainJFrame.this.dispose();
				// リセット   Ctrl+Shift+-
				if(e.isControlDown() &&
						e.isShiftDown() &&
						e.getKeyCode() == KeyEvent.VK_MINUS)
					reset();
			}
		});
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		timeLabel = new TimeLabel();
		this.getContentPane().add(timeLabel, BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * 作品集を元に、作品に対応したボタンを中央パネルに登録します。
	 */
	private void initScrollPane() {
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
				// 追加する年度ラベル (フォントは既定値の2倍サイズ)
				JLabel label = new JLabel();
				{
					Font font = label.getFont();
					label.setFont(font.deriveFont(font.getSize2D()*2F));
				}
				
				// 最新作でなければ
				if(workPrev != null) {
					// 「n年度前」または「昨年度」テキストをセット
					Calendar calendar = Calendar.getInstance();
					calendar.roll(-3, Calendar.MONTH);

					label.setText(String.format("%s年度\nの作品",
							calendar.get(Calendar.YEAR) == work.getYear()+1
							? "昨" : Short.toString(work.getYear())));
					// 背景画像をセット
					label.setIcon(
							new ImageIcon(imgCache.get("LabelBackOlder")));
				}
				// 最新作なら
				else {
					// 「最新作」テキストをセット
					label.setText("最新作");
					// 背景画像をセット
					label.setIcon(
							new ImageIcon(imgCache.get("LabelBackNewest")));
				}
				// 背景画像とテキストの位置関係をセット
				label.setHorizontalTextPosition(SwingConstants.CENTER);
				label.setVerticalTextPosition(SwingConstants.CENTER);
				// サイズを整えて追加
				label.setPreferredSize(MAIN_COMPONENTS_SIZE);
				EventQueue.invokeLater(new ComponentAdder(label));
			}
			// ボタンの文字列をセット
			button.setText(work.getName());
			// アクションキーを追加できればアクションキーを追加
			if(worksCount < BUTTON_ACTION_KEYS.length()) {
				button.setText(button.getText()+"\n("+
						BUTTON_ACTION_KEYS.charAt(worksCount)+")");
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
			EventQueue.invokeLater(new ComponentAdder(button));
			
			workPrev = work;
			++worksCount;
		}
		
		// コンポーネントの追加が終わったら、
		// 最後のイベントとしてタイマーリセット処理を入れる
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				timeLabel.resetTimer();
			}
		});
	}

	/**
	 * アプリ窓を凍結し、作品を体験できなくします。
	 * アプリ作品を体験中であれば、そのアプリを終了します。
	 */
	void freeze() {
		javax.swing.JOptionPane.showMessageDialog(this,
                "ゲームを始めて1時間たちました。\n" +
                "そろそろきゅうけいしましょう。");
		scrollPane.setEnabled(false);
	}
	
	/**
	 * タイマーをリセットし、アプリ窓の凍結を解除します。
	 */
	void reset() {
		this.setVisible(false);
		timeLabel.stopTimer();
		try {
			Thread.sleep(RESET_HIDING_TIME);
		}
		catch(InterruptedException e) {
		}
		timeLabel.resetTimer();
		this.repaint();
		this.setVisible(true);
	}
}
