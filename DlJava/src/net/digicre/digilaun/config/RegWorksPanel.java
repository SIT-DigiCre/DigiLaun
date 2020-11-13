package net.digicre.digilaun.config;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import net.digicre.digilaun.Config;
import net.digicre.digilaun.config.regworks.WorkTable;
import net.digicre.digilaun.work.WorkList;

import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 作品データの登録・編集・削除を行う UI を提供します。
 * @author p10090
 */
@SuppressWarnings("serial")
public class RegWorksPanel extends ConfigPanel implements TableModelListener {
	private WorkTable workTable;

	/**
	 * Create the panel.
	 */
	public RegWorksPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		workTable = new WorkTable();
		scrollPane.setViewportView(workTable);
		workTable.getModel().addTableModelListener(this);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.SOUTH);
		
		JButton btnAdd = new JButton("追加(D)");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegWorksPanel.this.workTable.addWork();
			}
		});
		btnAdd.setMnemonic('D');
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("削除(R)");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegWorksPanel.this.workTable.removeSelectedRows();
			}
		});
		btnRemove.setMnemonic('R');
		panel.add(btnRemove);

	}

	/**
	 * このパネルで編集された作品データをリストとして取得します。
	 * @return このパネルで編集された作品リスト
	 */
	public WorkList getWorkList() {
		return this.workTable.getEditedList();
	}

	/**
	 * 指定されたコンフィグをもとに、このパネルのデータを設定します。
	 * @param config 既定のコンフィグ
	 */
	public void setWorkList(Config config) {
		this.workTable.setEditedList(config.getWorks());
	}

	/**
	 * コンフィグを適用します。
	 * @param config 適用先のコンフィグオブジェクト
	 */
	@Override
	public void applyConfig(Config config) {
		config.getWorks().clear();
		config.getWorks().addAll(getWorkList());
		config.getWorks().sortByYear();
	}

	/**
	 * テーブルモデルが少しでも変更された時の処理です。
	 * ConfigChanged イベントを発生させます。
	 */
	@Override
	public void tableChanged(TableModelEvent arg0) {
		this.fireConfigChanged();
	}

}
