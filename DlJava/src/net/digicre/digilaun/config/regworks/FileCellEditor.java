/**
 * 
 */
package net.digicre.digilaun.config.regworks;

import java.awt.Component;

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
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 * @see FileCellEditorPanel
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		final FileCellEditorPanel editorPanel =
				(FileCellEditorPanel)this.editorComponent.getParent();
		editorPanel.setBackground(isSelected ?
				table.getSelectionBackground() : table.getBackground());
		editorPanel.setValue((String)value);
		return editorPanel;
	}

}
