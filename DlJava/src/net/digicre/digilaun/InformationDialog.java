package net.digicre.digilaun;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

@SuppressWarnings("serial")
public class InformationDialog extends JDialog implements HyperlinkListener {

	/**
	 * タイトルバーに表示するテキストのフォーマットです。
	 */
	private static final String TITLE_FORMAT = "%s の詳しい情報";
	
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;

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
			contentPanel.add(scrollPane, BorderLayout.CENTER);
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
	 * 作品の詳しい情報テキストを表示する、新しい
	 * <code>{@link InformationDialog}</code> を作成します。
	 * @param work 作品オブジェクト
	 * @throws IOException txt, UTF, HTML
	 * のいずれでもない情報テキストファイルのオープンに失敗した
	 */
	static void open(Work work) {
		//TODO 拡張子が txt でも UTF でも HTML でもなければ
		String str = "ahta.html";
		Pattern p = Pattern.compile(".");
		Matcher m = p.matcher(str);
		System.out.println(str);
		System.out.println(p);
		System.out.println(m.matches());
		System.out.println(m.group(0));
		System.out.println("\\.(?i:txt|RTF|HTML?|XHT(?:ML)?)$");
		System.out.println(work.getTextPath().matches(
				"\\.(?i:txt|RTF|HTML?|XHT(?:ML)?)$"));
		if(!work.getTextPath().matches(
				"\\.(?i:txt|RTF|HTML?|XHT(?:ML)?)$")) {
			System.out.println("awanaiyo");
			// 普通にオープンを試みる
			Desktop d = Desktop.getDesktop();
			try {
				d.open(new File(work.getTextPath()));
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		InformationDialog it = new InformationDialog();
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(it);
		
		// タイトルセット
		if(work.getName() != null &&
				!work.getName().isEmpty())
			it.setTitle(String.format(TITLE_FORMAT, work.getName()));
		// テキストペインのセット
		if(work.getTextPath() != null &&
				!work.getTextPath().isEmpty()) {
			File file = new File(work.getTextPath());
			try {
				editorPane.setContentType("text/plain;charset=Shift_JIS");
				editorPane.setPage(file.toURI().toURL());
/*				HTMLDocument doc =
						new HTMLDocument();
				doc.setBase(file.getParentFile().toURI().toURL());
				InputStream in = new FileInputStream(file);
				
				textPane.read(in,
						file.getName().matches("\\.(?i:HTML?)$") ? doc : null);
*/			}
			catch(ChangedCharSetException e) {}
			catch(Exception e) {
				editorPane.setForeground(Color.RED);
				editorPane.setText(e.getLocalizedMessage());
			}
		}
		
		it.scrollPane.getViewport().add(editorPane);
		it.setVisible(true);
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent arg0) {
		// マウスオーバー、マウスアウトイベントは無視。クリックのみ処理
		if(arg0.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
			return;
		// FILE プロトコルでなければブラウザーで開く
		if(!arg0.getURL().getProtocol().equals("file")) {
			try {
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(arg0.getURL().toURI());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		// リンクを開く
		try {
			((JEditorPane)arg0.getSource()).setPage(arg0.getURL());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
