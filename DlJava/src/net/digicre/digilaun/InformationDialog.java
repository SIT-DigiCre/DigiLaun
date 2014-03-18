package net.digicre.digilaun;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.ChangedCharSetException;
import net.digicre.digilaun.work.Work;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.ScrollPaneConstants;

/**
 * 作品の詳しい情報を表示する機能を提供します。
 * 外部アプリに頼らなくても表示できる情報は、このダイアログに表示します。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
public class InformationDialog extends JDialog {
	/**
	 * テキスト領域の既定の幅です。
	 */
	private static final int DEFAULT_VIEW_WIDTH  = 980; 

	/**
	 * テキスト領域の既定の高さです。
	 */
	private static final int DEFAULT_VIEW_HEIGHT = DEFAULT_VIEW_WIDTH * 3/4; 

	/**
	 * タイトルバーに表示するテキストのフォーマットです。
	 */
	private static final String TITLE_FORMAT = "%s の詳しい情報";

	/**
	 * リンクをたどれないときのエラーメッセージです。
	 */
	protected static final Object LINK_ERROR_MESSAGE =
			"リンクを開けませんでした.";

	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JEditorPane editorPane;

	/**
	 * Create the dialog.
	 */
	private InformationDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("詳しい情報");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scrollPane = new JScrollPane();
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setPreferredSize(new Dimension(980, 735));
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				editorPane = new JEditorPane();
				editorPane.addHyperlinkListener(new HyperlinkListener() {
					public void hyperlinkUpdate(HyperlinkEvent arg0) {
						onHyperLinkUpdate(arg0);
					}
				});
				editorPane.setEditable(false);
				scrollPane.setViewportView(editorPane);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						// OK 押したら閉じる
						InformationDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	/**
	 * ハイパーリンクの状態が変化した時の処理です。
	 * ハイパーリンクがアクティブ化された時にリンク先を表示します。
	 * リンクが file プロトコルかつ {@link JEditorPane} で表示できればこのダイアログで、
	 * それ以外のリンクは既定の Web ブラウザーで表示します。
	 * @param e イベントオブジェクト
	 */
	private void onHyperLinkUpdate(HyperlinkEvent e) {
		// マウスオーバー、マウスアウトイベントは無視
		//クリックのみ処理
		if(e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
			return;
		// FILE プロトコルでないか、
		// JEditorPane で表示できなければブラウザーで開く
		if(!e.getURL().getProtocol().equals("file") ||
				!fileCanShowInEditorPane(e.getURL().getPath())) {
			try {
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(e.getURL().toURI());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return;
		}
		// リンクを開く
		try {
			((JEditorPane)e.getSource()).setPage(
					e.getURL());
		} catch (IOException ex) {
			ex.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(
					InformationDialog.this,
					String.format("%s\n\n%s",
							InformationDialog.
								LINK_ERROR_MESSAGE,
							ex.getLocalizedMessage()));
		}
	}

	/**
	 * 作品の詳しい情報テキストを表示する、新しい
	 * <code>{@link InformationDialog}</code> を作成します。
	 * @param work 作品オブジェクト
	 * @throws IOException txt, UTF, HTML
	 * のいずれでもない情報テキストファイルのオープンに失敗した
	 */
	static void open(final Work work) {
		// 拡張子が txt でも UTF でも HTML でもなければ
		if(!fileCanShowInEditorPane(work.getDetailTextFile())) {
			// 普通にオープンを試みる
			Desktop d = Desktop.getDesktop();
			try {
				d.open(work.getDetailTextFile());
				return;
			} catch (Exception e) {
			}
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				InformationDialog it = new InformationDialog();

				// タイトルセット
				if(work.getName() != null &&
						!work.getName().isEmpty())
					it.setTitle(String.format(TITLE_FORMAT, work.getName()));
				// テキスト領域が既定値になるようサイズを調整
				it.pack();
				{
					Rectangle rect =
							it.scrollPane.getViewportBorderBounds();
					rect.x =
					rect.y = 0;
					rect.width  =
							it.getWidth()  - rect.width  + DEFAULT_VIEW_WIDTH;
					rect.height =
							it.getHeight() - rect.height + DEFAULT_VIEW_HEIGHT;
					it.setBounds(rect);
				}
				it.pack();
				// テキストペインのセット
				if(work.getDetailTextFile() != null) {
					File file = work.getDetailTextFile();
					try {
						it.editorPane.setContentType(
								"text/plain;charset=Shift_JIS");
						it.editorPane.setPage(file.toURI().toURL());
					}
					catch(ChangedCharSetException e) {}
					catch(Exception e) {
						it.editorPane.setForeground(Color.RED);
						it.editorPane.setText(e.getLocalizedMessage());
					}
				}
				it.setVisible(true);
			}
		});
	}

	/**
	 * ファイルが <code>{@link JEditorPane}</code> で表示できるかを判定します。
	 * @param path ファイルへのパス
	 * @return ファイルが <code>{@link JEditorPane}</code> で表示できれば真
	 */
	static boolean fileCanShowInEditorPane(String path) {
		int n = path.lastIndexOf('.');
		if(n < 0)
			return false;
		String pathExt = path.substring(n+1);
		return pathExt.matches("\\A(?i:txt|RTF|HTML?)\\z");
	}

	/**
	 * ファイルが <code>{@link JEditorPane}</code> で表示できるかを判定します。
	 * @param file ファイル
	 * @return ファイルが <code>{@link JEditorPane}</code> で表示できれば真
	 */
	static boolean fileCanShowInEditorPane(File file) {
		return fileCanShowInEditorPane(file.getPath());
	}
}
