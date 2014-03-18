package net.digicre.digilaun.config.regworks;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.border.LineBorder;

/**
 * {@link FileCellEditor} での編集に使う UI です。
 * @author p10090
 */
@SuppressWarnings("serial")
public class FileCellEditorPanel extends JPanel {
	/**
	 * 参照ダイアログを開くとき、
	 * すでにテキストボックスに入力されていたパスからファイルが見つからなかった時に表示するメッセージです。
	 */
	private static final String INPUT_FILE_IS_NOT_FOUND_FORMAT =
			"現在設定中のファイル\n%s\nは見つかりません。" +
			"ファイルが移動、または削除された可能性があります。\n" +
			"ファイルの消息を確認してください。";

	/**
	 * 参照ダイアログのファイルフィルターです。
	 */
	private FileFilter[] fileFilters;

	/**
	 * 参照ダイアログです。
	 */
	private JFileChooser fileChooser;

	private JTextField pathTextField;
	private JButton btnRefer;

	/**
	 * Create the panel.
	 */
	private FileCellEditorPanel() {
		setLayout(new BorderLayout(5, 0));
		
		btnRefer = new JButton("Refer");
		btnRefer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileCellEditorPanel.this.openReferDialog();
			}
		});
		
		pathTextField = new JTextField();
		pathTextField.setBorder(new LineBorder(null));
		add(pathTextField, BorderLayout.CENTER);
		pathTextField.setColumns(10);
		add(btnRefer, BorderLayout.EAST);

	}

	/**
	 * 指定したファイルフィルターをもとに参照ダイアログを表示するパネルを作成します。
	 * @param fileFilters ファイルフィルターの配列
	 *  (null 要素は「すべてのファイル」フィルターに置換される)、
	 *  <code>null</code> なら参照ボタンは表示されない
	 */
	public FileCellEditorPanel(FileFilter[] fileFilters) {
		this();
		if((this.fileFilters = fileFilters) == null)
			remove(btnRefer);
	}

	/**
	 * 参照ダイアログを開きます。
	 */
	public void openReferDialog() {
		// ダイアログを作っていなければ作成
		if(fileChooser == null) {
				fileChooser = new JFileChooser();
	
			// フィルターリセット
			for(FileFilter filter : fileChooser.getChoosableFileFilters())
				fileChooser.removeChoosableFileFilter(filter);
			for(FileFilter filter : fileFilters)
				fileChooser.addChoosableFileFilter(filter != null ?
						filter : fileChooser.getAcceptAllFileFilter());
		}

		fileChooser.setFileFilter(fileFilters[0]);

		// テキストボックスが空なら
		if(this.pathTextField.getText().isEmpty()) {
			// カレントディレクトリーを設定
			fileChooser.setSelectedFile(null);
			fileChooser.setCurrentDirectory(new File("."));
		}
		// テキストボックスが空でなければ
		else {
			// カレントディレクトリーを設定
			final File setFile = new File(this.pathTextField.getText());
			if(setFile.exists()) {
				fileChooser.setSelectedFile(setFile);
			}
			else {
				javax.swing.JOptionPane.showMessageDialog(this,
						String.format(INPUT_FILE_IS_NOT_FOUND_FORMAT,
								setFile.getPath()),
						"Digi Laun", javax.swing.JOptionPane.WARNING_MESSAGE);
			}
			File parent = setFile.getParentFile();

			fileChooser.setCurrentDirectory(
					parent != null && parent.exists() ?
					parent : new File("."));
		}
		// ダイアログを開く
		if(fileChooser.showOpenDialog(this)
				== JFileChooser.APPROVE_OPTION) {
			// ファイルが選択されたらテキストフィールドに反映
			Path path = new Path(fileChooser.getSelectedFile().getPath());
			this.pathTextField.setText(
					new Path(".").relativize(path).toString());
		}
	}

	/**
	 * このパネルに入力された値を設定します。
	 * @param value このパネルに入力される値
	 */
	public void setValue(Object value) {
		this.pathTextField.setText(value instanceof File ?
				((File)value).getPath() : value.toString());
	}

	/**
	 * このパネル内のテキストフィールドを取得します。
	 * @return このパネル内のテキストフィールド
	 */
	JTextField getPathTextField() {
		return pathTextField;
	}
}
