package net.digicre.digilaun.config.regworks;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import net.digicre.digilaun.work.Work;
import net.digicre.digilaun.work.WorkList;
import net.digicre.digilaun.work.WritableWork;

/**
 * 作品テーブルです。このテーブルは作品リストを可変長配列で保持し、
 * テーブルが編集されると内部で保持している配列に直ちに反映します。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
public class WorkTable extends JTable {
	/**
	 * テーブルに対応する作品リストです。
	 */
	private ArrayList<Work> editedWorksArray;

	/**
	 * 新しい作品表を作成します。
	 */
	public WorkTable() {
		this(null);
	}

	/**
	 * 指定された作品リストと同じデータを持つ、新しい作品表を作成します。
	 * @param workList データの元になる作品リスト
	 */
	public WorkTable(WorkList workList) {
		// モデルを初期化
		WorkTableModel model;
		TableColumnModel columnModel;
		TableRowSorter<WorkTableModel> rowSorter;

		this.setModel(model = new WorkTableModel());
		columnModel = this.getColumnModel();
		for(int i = 0; i < columnModel.getColumnCount(); ++i)
			if(model.getColumnClass(i).equals(File.class))
				columnModel.getColumn(i).setCellEditor(
						new FileCellEditor(model.getColumnFileFilters(i)));
		model.addTableModelListener(this);

		// その他の初期化
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		rowSorter = model.createRowSorter();
		this.setRowSorter(rowSorter);
		this.setEditedList(workList);
	}

	/**
	 * 指定された作品リストのデータをこのテーブルに反映します。
	 * @param workList テーブルに反映する作品リスト
	 */
	public void setEditedList(WorkList workList) {
		if(workList == null)
			this.editedWorksArray = new ArrayList<Work>();
		else synchronized(workList) {
			this.editedWorksArray =
					new ArrayList<Work>(workList.size()+10);
			for(Work work : workList)
				this.editedWorksArray.add(new WritableWork(work));
		}
		((WorkTableModel)this.getModel()).setWorkList(this.editedWorksArray);
		((WorkTableModel)this.getModel()).fireTableDataChanged();
	}

	/**
	 * このテーブルのデータを {@link WorkList} 型で取得します。
	 * @return このテーブルのデータを保持した作品リスト
	 */
	public WorkList getEditedList() {
		WorkList works = new WorkList();

		for(Work work : this.editedWorksArray)
			works.addFirst(new Work(work));
		works.sortByYear();

		return works;
	}

	/**
	 * 新しい今年度作品を表に追加します。
	 */
	public void addWork() {
		this.addRow(new WritableWork());
	}

	/**
	 * 作品を表に追加します。
	 * @param work 追加する作品データ
	 */
	private void addRow(Work work) {
		((WorkTableModel)this.getModel()).addRow(work);
		java.awt.EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				WorkTable.this.changeSelection(0, 0, false, false);
			}});
	}

	/**
	 * 選択された行を削除します。
	 */
	public synchronized void removeSelectedRows() {
		// 画面上で選択されてる行番号
		final int[] rows = this.getSelectedRows();

		// モデルでの行番号に変換
		for(int i = 0; i < rows.length; ++i)
			rows[i] = this.convertRowIndexToModel(rows[i]);
		Arrays.sort(rows);

		// 削除 (連続で選択された行はまとめて削除)
		int intervalLast = -1;
		for(int i = rows.length-1; i >= 0; --i)
			if(rows[i] < this.editedWorksArray.size()) {
				if(i == rows.length-1 || rows[i]+1 != rows[i+1])
					intervalLast = rows[i];
				if(i == 0 || rows[i]-1 != rows[i-1]) {
					((WorkTableModel)WorkTable.this.getModel()).
					removeRow(rows[i], intervalLast);
				}
			}
		// 選択解除
		if(WorkTable.this.getRowCount() > 0)
			WorkTable.this.removeRowSelectionInterval(
					0, WorkTable.this.getRowCount()-1);
	}
}
