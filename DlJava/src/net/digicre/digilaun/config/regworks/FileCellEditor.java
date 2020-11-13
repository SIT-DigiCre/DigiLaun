/**
 * 
 */
package net.digicre.digilaun.config.regworks;

import java.io.File;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellEditor;

/**
 * {@link java.io.File} (で使える相対パス表現文字列) を編集するためのエディターです。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
public class FileCellEditor extends DefaultCellEditor implements
		TableCellEditor {
	/**
	 * 指定した {@link FileFilter} を基にファイル選択ダイアログを表示できる、
	 * 新しい FileCellEditor を作成します。
	 * @param fileFilters フィルター
	 * (配列内に null を入れると「すべてのファイル」のフィルターに置換される)
	 */
	public FileCellEditor(FileFilter[] fileFilters) {
		super(new FileCellEditorPanel(fileFilters).getPathTextField());
	}

	/**
	 * このエディターでの編集に使うコンポーネント
	 * ({@link FileCellEditorPanel}) を返します。
	 * @param table 編集する表
	 * @param value 編集するセルの現在の値 ({@link File} または文字列型)
	 * @param isSelected 編集するセルが選択されているかどうか
	 * @param row 編集するセルの行番号
	 * @param column 編集するセルの列番号
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public FileCellEditorPanel getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		final FileCellEditorPanel editorPanel =
				(FileCellEditorPanel)this.editorComponent.getParent();
		if(isSelected) {
			editorPanel.setBackground(table.getSelectionBackground());
			editorPanel.getPathTextField().requestFocusInWindow();
		}
		else
			editorPanel.setBackground(table.getBackground());
		editorPanel.setValue(value);
		return editorPanel;
	}

	/**
	 * このエディターで編集された値を取得します。
	 * @return このエディターで編集された値
	 */
	@Override
	public File getCellEditorValue() {
		return new File((String)super.getCellEditorValue());
	}
}
