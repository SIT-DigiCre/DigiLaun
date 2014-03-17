package net.digicre.digilaun.config.regworks;

import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.digicre.digilaun.work.WritableWork;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class RegisterPanel extends JPanel {
	/**
	 * 起動ファイルとして標準で受け入れるファイルのフィルターです。
	 */
	public static final FileFilter[] pathFileFilters = {
		new FileNameExtensionFilter(
				"Windows アプリケーション (*.exe)",
				"exe"),
		new FileNameExtensionFilter(
				"コマンドスクリプト (*.bat; *.sh)",
				"bat", "sh"),
		null,
	};
	/**
	 * 画像として標準で受け入れるファイルのフィルターです。
	 */
	public static final FileFilter[] imgPathFileFilters = {
		new FileNameExtensionFilter(
				"画像ファイル (*.GIF; *.JFIF; *.jpg; *.JPEG; *.jpe; *.PNG)",
				"GIF", "JFIF", "jpg", "JPEG", "jpe", "PNG"),
	};
	/**
	 * テキストとして標準で受け入れるファイルのフィルターです。
	 */
	public static final FileFilter[] textPathFileFilters = {
		new FileNameExtensionFilter(
				"文書ファイル (*.txt; *.UTF; *.HTML; *.htm)",
				"txt", "UTF", "HTML", "htm"),
		null,
	};

	/**
	 * ファイル参照に使うダイアログです。
	 */
	private JFileChooser fileChooser;

	private JTextField nameTextField;
	private JTextField pathTextField;
	private JTextField iconPathTextField;
	private JTextField imagePathTextField;
	private JTextField textPathTextField;
	private JSpinner yearSpinner;
	private JTextField inputTextField;

	/**
	 * Create the panel.
	 */
	public RegisterPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0, 0};
		gridBagLayout.rowHeights = new int[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel label = new JLabel("作品名");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridwidth = 2;
		gbc_label.anchor = GridBagConstraints.NORTH;
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(label, gbc_label);
		
		nameTextField = new JTextField();
		GridBagConstraints gbc_nameTextField = new GridBagConstraints();
		gbc_nameTextField.gridwidth = 2;
		gbc_nameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_nameTextField.gridx = 0;
		gbc_nameTextField.gridy = 1;
		add(nameTextField, gbc_nameTextField);
		nameTextField.setColumns(10);
		
		JLabel label_1 = new JLabel("制作年度");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.gridwidth = 2;
		gbc_label_1.anchor = GridBagConstraints.NORTH;
		gbc_label_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		add(label_1, gbc_label_1);
		
		yearSpinner = new JSpinner();
		yearSpinner.setModel(new SpinnerNumberModel(new Integer(2013), new Integer(2003), null, new Integer(1)));
		GridBagConstraints gbc_yearSpinner = new GridBagConstraints();
		gbc_yearSpinner.anchor = GridBagConstraints.WEST;
		gbc_yearSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_yearSpinner.gridx = 0;
		gbc_yearSpinner.gridy = 3;
		add(yearSpinner, gbc_yearSpinner);
		
		JLabel label_7 = new JLabel("入力デバイス (なければ空欄)");
		GridBagConstraints gbc_label_7 = new GridBagConstraints();
		gbc_label_7.gridwidth = 2;
		gbc_label_7.anchor = GridBagConstraints.NORTH;
		gbc_label_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_7.insets = new Insets(0, 0, 5, 0);
		gbc_label_7.gridx = 0;
		gbc_label_7.gridy = 4;
		add(label_7, gbc_label_7);
		
		inputTextField = new JTextField();
		inputTextField.setText("キーボードまたはゲームパッド");
		GridBagConstraints gbc_inputTextField = new GridBagConstraints();
		gbc_inputTextField.gridwidth = 2;
		gbc_inputTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputTextField.insets = new Insets(0, 0, 5, 0);
		gbc_inputTextField.gridx = 0;
		gbc_inputTextField.gridy = 5;
		add(inputTextField, gbc_inputTextField);
		inputTextField.setColumns(10);
		
		JLabel label_2 = new JLabel("起動ファイル");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.gridwidth = 2;
		gbc_label_2.anchor = GridBagConstraints.NORTH;
		gbc_label_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_2.insets = new Insets(0, 0, 5, 0);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 6;
		add(label_2, gbc_label_2);
		
		pathTextField = new JTextField();
		GridBagConstraints gbc_pathTextField = new GridBagConstraints();
		gbc_pathTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_pathTextField.insets = new Insets(0, 0, 5, 5);
		gbc_pathTextField.gridx = 0;
		gbc_pathTextField.gridy = 7;
		add(pathTextField, gbc_pathTextField);
		pathTextField.setColumns(10);
		
		JButton pathReferButton = new JButton("参照...");
		pathReferButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegisterPanel.this.referFile(
						pathFileFilters, pathTextField);
			}
		});
		GridBagConstraints gbc_pathReferButton = new GridBagConstraints();
		gbc_pathReferButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_pathReferButton.insets = new Insets(0, 0, 5, 0);
		gbc_pathReferButton.gridx = 1;
		gbc_pathReferButton.gridy = 7;
		add(pathReferButton, gbc_pathReferButton);
		
		JLabel label_3 = new JLabel("アイコン");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.gridwidth = 2;
		gbc_label_3.anchor = GridBagConstraints.NORTH;
		gbc_label_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_3.insets = new Insets(0, 0, 5, 0);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 8;
		add(label_3, gbc_label_3);
		
		iconPathTextField = new JTextField();
		GridBagConstraints gbc_iconPathTextField = new GridBagConstraints();
		gbc_iconPathTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_iconPathTextField.insets = new Insets(0, 0, 5, 5);
		gbc_iconPathTextField.gridx = 0;
		gbc_iconPathTextField.gridy = 9;
		add(iconPathTextField, gbc_iconPathTextField);
		iconPathTextField.setColumns(10);
		
		JButton iconPathReferButton = new JButton("参照...");
		iconPathReferButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegisterPanel.this.referFile(
						imgPathFileFilters, iconPathTextField);
			}
		});
		GridBagConstraints gbc_iconPathReferButton = new GridBagConstraints();
		gbc_iconPathReferButton.insets = new Insets(0, 0, 5, 0);
		gbc_iconPathReferButton.gridx = 1;
		gbc_iconPathReferButton.gridy = 9;
		add(iconPathReferButton, gbc_iconPathReferButton);
		
		JLabel label_4 = new JLabel("作品の雰囲気や遊び方などを伝える概要イメージ");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.gridwidth = 2;
		gbc_label_4.anchor = GridBagConstraints.NORTH;
		gbc_label_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_4.insets = new Insets(0, 0, 5, 0);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 10;
		add(label_4, gbc_label_4);
		
		imagePathTextField = new JTextField();
		GridBagConstraints gbc_picturePathTextField = new GridBagConstraints();
		gbc_picturePathTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_picturePathTextField.insets = new Insets(0, 0, 5, 5);
		gbc_picturePathTextField.gridx = 0;
		gbc_picturePathTextField.gridy = 11;
		add(imagePathTextField, gbc_picturePathTextField);
		imagePathTextField.setColumns(10);
		
		JButton picturePathReferButton = new JButton("参照...");
		picturePathReferButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterPanel.this.referFile(
						imgPathFileFilters, imagePathTextField);
			}
		});
		GridBagConstraints gbc_picturePathReferButton = new GridBagConstraints();
		gbc_picturePathReferButton.insets = new Insets(0, 0, 5, 0);
		gbc_picturePathReferButton.gridx = 1;
		gbc_picturePathReferButton.gridy = 11;
		add(picturePathReferButton, gbc_picturePathReferButton);
		
		JLabel label_6 = new JLabel("クレジットや権利情報などをまとめた詳細テキスト");
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.gridwidth = 2;
		gbc_label_6.anchor = GridBagConstraints.NORTH;
		gbc_label_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_label_6.insets = new Insets(0, 0, 5, 0);
		gbc_label_6.gridx = 0;
		gbc_label_6.gridy = 12;
		add(label_6, gbc_label_6);
		
		textPathTextField = new JTextField();
		GridBagConstraints gbc_textPathTextField = new GridBagConstraints();
		gbc_textPathTextField.insets = new Insets(0, 0, 0, 5);
		gbc_textPathTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPathTextField.gridx = 0;
		gbc_textPathTextField.gridy = 13;
		add(textPathTextField, gbc_textPathTextField);
		textPathTextField.setColumns(10);
		
		JButton textPathReferButton = new JButton("参照...");
		textPathReferButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterPanel.this.referFile(
						textPathFileFilters, textPathTextField);
			}
		});
		GridBagConstraints gbc_textPathReferButton = new GridBagConstraints();
		gbc_textPathReferButton.gridx = 1;
		gbc_textPathReferButton.gridy = 13;
		add(textPathReferButton, gbc_textPathReferButton);

	}

	/**
	 * ファイルをダイアログで参照し、その相対パスを
	 * <code>{@link JTextField}</code> に格納します。
	 * @param filters
	 * @param destTextField
	 */
	private void referFile(FileFilter[] filters, JTextField destTextField) {
		// 必要に応じてオブジェクト作成
		synchronized (this) {
			if(fileChooser == null)
				fileChooser = new JFileChooser();
		}
		// フィルターリセット
		for(FileFilter f : fileChooser.getChoosableFileFilters())
			fileChooser.removeChoosableFileFilter(f);
		for(FileFilter filter : filters)
			fileChooser.addChoosableFileFilter(filter != null ?
					filter : fileChooser.getAcceptAllFileFilter());
		fileChooser.setFileFilter(filters[0]);
		// ダイアログを開く
		if(fileChooser.showOpenDialog(this)
				== JFileChooser.APPROVE_OPTION) {
			// ファイルが選択されたらテキストフィールドに反映
			Path path = new Path(fileChooser.getSelectedFile().getPath());
			destTextField.setText(
					new Path(".").relativize(path).toString());
		}
	}

	/**
	 * 入力データが最低限そろっているかどうかを調べます。
	 * @return 入力が妥当なら真
	 */
	boolean isInputValid() {
		return ! this.pathTextField.getText().isEmpty();
	}

	/**
	 * 入力データを指定した作品オブジェクトに適用します。
	 * @param work 入力データを適用する作品オブジェクト
	 */
	void applyToWork(WritableWork work) {
		work.setName(this.nameTextField.getText());
		work.setYear((Integer)this.yearSpinner.getValue());
		work.setPath(this.pathTextField.getText());
		work.setIconPath(this.iconPathTextField.getText());
		work.setSummaryImagePath(this.imagePathTextField.getText());
		work.setDetailTextPath(this.textPathTextField.getText());
		work.setInputDeviceName(this.inputTextField.getText());
	}

	/**
	 * 入力データを元に新しい作品オブジェクトを作成します。
	 * @return 入力データを適用された新しい作品オブジェクト
	 */
	WritableWork createWork() {
		WritableWork work = new WritableWork();
		this.applyToWork(work);
		return work;
	}
}
