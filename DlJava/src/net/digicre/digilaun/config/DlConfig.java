package net.digicre.digilaun.config;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JButton;

import net.digicre.digilaun.Config;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Dimension;

/**
 * Digi Laun のコンフィグを行うアプリのメインクラスです。
 * @author p10090
 * @see #main(String[])
 */
public class DlConfig implements ConfigListener {
	/**
	 * 設定ファイルを読めなかった時のエラーメッセージのフォーマットです。
	 */
	static final String READ_ERROR_FORMAT = "設定ファイルの読み込みに失敗しました。\n\n%s";

	/**
	 * 設定ファイルを書けなかった時のエラーメッセージのフォーマットです。
	 */
	static final String WRITE_ERROR_FORMAT = "設定ファイルの書き込みに失敗しました。\n\n%s";

	/**
	 * 保存せずに閉じようとした時の確認メッセージです。
	 */
	static final String EXIT_CONFIRM = "設定が変更されている可能性があります。保存しますか？";

	/**
	 * キャンセルしようとした時の確認メッセージです。
	 */
	static final String CANCEL_CONFIRM = "設定を保存せずに終了します。よろしいですか？";

	/**
	 * 変更中の設定オブジェクトです。
	 */
	Config config;

	private JFrame frmDigiLaun;
	private RegWorksPanel pnlRegWorks;
	private JButton btnApply;
	private SettingsPanel pnlSettings;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DlConfig window = new DlConfig();
					window.frmDigiLaun.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	@SuppressWarnings("deprecation")
	public DlConfig() {
		this.config = new Config();
		try {
			config.readFromXMLDocument();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				config.getWorks().readFromXMLDocument();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,
						String.format(READ_ERROR_FORMAT, e.getMessage()), "Digi Laun",
						JOptionPane.ERROR_MESSAGE);

			}
		}
		initialize();
		this.pnlRegWorks.setWorkList(config);
		this.pnlSettings.setSettings(config);
		this.addToConfigListeners();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDigiLaun = new JFrame();
		frmDigiLaun.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// 設定が変更されてるかもしれなければ保存を促す
				if(DlConfig.this.btnApply.isEnabled()) {
					switch(JOptionPane.showConfirmDialog(arg0.getWindow(),
							DlConfig.EXIT_CONFIRM, DlConfig.this.frmDigiLaun.getTitle(),
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE)) {
					case JOptionPane.CANCEL_OPTION:
						// キャンセルなら何もしない
						return;
					case JOptionPane.YES_OPTION:
						// 「はい」なら保存
						try {
							DlConfig.this.applyConfig();
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					case JOptionPane.NO_OPTION:
						// 「いいえ」なら保存処理をスキップ
					}
				}
				// 閉じる
				arg0.getWindow().dispose();
			}
		});
		frmDigiLaun.setTitle("Digi Laun 環境設定ツール");
		frmDigiLaun.setBounds(0, 100, 1024, 512);
		frmDigiLaun.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDigiLaun.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmDigiLaun.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		pnlRegWorks = new RegWorksPanel();
		tabbedPane.addTab("作品データベース", null, pnlRegWorks, null);
		
		pnlSettings = new SettingsPanel();
		tabbedPane.addTab("設定", null, pnlSettings, null);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		frmDigiLaun.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton("OK");
		btnOk.setPreferredSize(new Dimension(100, 26));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 適用して閉じる
				try {
					DlConfig.this.applyConfig();
					DlConfig.this.frmDigiLaun.dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnOk);
		
		JButton btnCancel = new JButton("キャンセル");
		btnCancel.setPreferredSize(new Dimension(100, 26));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 設定が変更されてるかもしれなければ確認する
				if(DlConfig.this.btnApply.isEnabled()) {
					switch(JOptionPane.showConfirmDialog(DlConfig.this.frmDigiLaun,
							DlConfig.CANCEL_CONFIRM, DlConfig.this.frmDigiLaun.getTitle(),
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE)) {
					case JOptionPane.CANCEL_OPTION:
						return;
					case JOptionPane.OK_OPTION:
					}
				}
				// 閉じる
				DlConfig.this.frmDigiLaun.dispose();
			}
		});
		panel.add(btnCancel);
		
		btnApply = new JButton("適用(A)");
		btnApply.setPreferredSize(new Dimension(100, 26));
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 適用
				try {
					DlConfig.this.applyConfig();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnApply.setEnabled(false);
		btnApply.setMnemonic('A');
		panel.add(btnApply);
	}

	/**
	 * 各種 ConfigPanel のコンフィグリスナーにこのオブジェクトを追加します。
	 */
	private void addToConfigListeners() {
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				pnlRegWorks.addConfigListener(DlConfig.this);
				pnlSettings.addConfigListener(DlConfig.this);
			}});
	}

	/**
	 * コンフィグを設定ファイルに「適用」します。
	 * 例外が発生した場合は、メッセージボックスを表示してからその例外を投げます。
	 * @throws Exception 設定ファイル保存時の例外
	 */
	private void applyConfig() throws Exception {
		// パネルでのコンフィグをオブジェクトに適用
		pnlRegWorks.applyConfig(this.config);
		pnlSettings.applyConfig(this.config);

		// ファイルに出力
		try {
			config.WriteToXMLDocument();
			this.btnApply.setEnabled(false);
		} catch (Exception e) {
			// 失敗したらエラーダイアログを表示
			JOptionPane.showMessageDialog(this.frmDigiLaun,
					String.format(WRITE_ERROR_FORMAT, e.getMessage()),
					"Digi Laun", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}

	/**
	 * コンフィグが少しでも変更された時の処理です。
	 * @param e イベントオブジェクト
	 */
	@Override
	public void configChanged(ConfigEvent e) {
		// [適用] ボタンを有効化
		this.btnApply.setEnabled(true);
	}
}
