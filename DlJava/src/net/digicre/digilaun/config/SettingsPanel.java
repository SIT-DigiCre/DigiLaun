package net.digicre.digilaun.config;

import net.digicre.digilaun.Config;
import net.digicre.digilaun.Config.Mode;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.util.Enumeration;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class SettingsPanel extends ConfigPanel {
	private final ButtonGroup btngrpMode = new ButtonGroup();
	private JRadioButton rdbtnDisplayMode;
	private JRadioButton rdbtnDistributionMode;

	/**
	 * Create the panel.
	 */
	public SettingsPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		
		JLabel label = new JLabel("起動モード");
		
		rdbtnDisplayMode = new JRadioButton("展示 (Display) モード");
		rdbtnDisplayMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsPanel.this.fireConfigChanged();
			}
		});
		rdbtnDisplayMode.setActionCommand("DISPLAY");
		rdbtnDisplayMode.setMnemonic('Y');
		btngrpMode.add(rdbtnDisplayMode);
		
		rdbtnDistributionMode = new JRadioButton("頒布 (Distribution) モード");
		rdbtnDistributionMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsPanel.this.fireConfigChanged();
			}
		});
		rdbtnDistributionMode.setActionCommand("DISTRIBUTION");
		rdbtnDistributionMode.setMnemonic('N');
		btngrpMode.add(rdbtnDistributionMode);
		
		JTextArea txtrDigiLaun = new JTextArea();
		txtrDigiLaun.setFocusable(false);
		txtrDigiLaun.setEditable(false);
		txtrDigiLaun.setText("Digi Laun をデスクトップと同じ大きさで表示します。\r\n内部タイマーを有効にし、連続利用時間を測定できます。\r\n作品の体験ログを記録します。\r\nDigi Laun を終了するにはキーコマンドが必要です。");
		txtrDigiLaun.setBackground(UIManager.getColor("Panel.background"));
		
		JTextArea txtrDigiLaun_1 = new JTextArea();
		txtrDigiLaun_1.setBackground(UIManager.getColor("Panel.background"));
		txtrDigiLaun_1.setEditable(false);
		txtrDigiLaun_1.setFocusable(false);
		txtrDigiLaun_1.setText("Digi Laun を標準の大きさで表示します。\r\n内部タイマーを無効にします。\r\n作品の体験ログを記録しません。\r\n隠しキーコマンドは一切使えません。");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setAutoCreateContainerGaps(true);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(label)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(21)
									.addComponent(txtrDigiLaun, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
								.addComponent(rdbtnDisplayMode)))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(21)
									.addComponent(txtrDigiLaun_1, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
								.addComponent(rdbtnDistributionMode))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnDisplayMode)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtrDigiLaun, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnDistributionMode)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtrDigiLaun_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(181, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

	}

	public SettingsPanel(Config config) {
		this();
		this.setSettings(config);
	}

	public void setSettings(Config config) {
		// 起動モードのチェックをセット
		Enumeration<AbstractButton> i = this.btngrpMode.getElements();
		while(i.hasMoreElements()) {
			AbstractButton radioButton = i.nextElement();
			if(radioButton.getActionCommand() == config.getMode().toString()) {
				radioButton.setSelected(true);
				break;
			}
		}
	}

	@Override
	public void applyConfig(Config config) {
		// 起動モードのチェックを適用
		Enumeration<AbstractButton> i = this.btngrpMode.getElements();
		while(i.hasMoreElements()) {
			AbstractButton radioButton = i.nextElement();
			if(radioButton.isSelected()) {
				config.setMode(Mode.valueOf(radioButton.getActionCommand()));
				break;
			}
		}
	}
}
