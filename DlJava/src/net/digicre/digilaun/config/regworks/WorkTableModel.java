package net.digicre.digilaun.config.regworks;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import net.digicre.digilaun.work.Work;
import net.digicre.digilaun.work.WritableWork;

/**
 * 作品表のモデルです。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
public class WorkTableModel extends AbstractTableModel {
	/**
	 * この表モデルの列データを扱うクラスです。
	 * @author p10090
	 */
	private abstract class Column<CellType> {
		/**
		 * この列からデータを取得します。
		 * @param row 行番号
		 * @return この列の <code>row</code> 行目から取得したデータ
		 */
		final CellType get(int row) {
			return this.get(WorkTableModel.this.relatedList.get(row));
		}

		/**
		 * この列にデータを設定します。
		 * @param row 行番号
		 * @param value この列の <code>row</code> 行目に設定するデータ
		 */
		@SuppressWarnings("unchecked")
		final void set(int row, Object value) {
			this.set(
					(WritableWork)WorkTableModel.this.relatedList.get(row),
					(CellType)value);
		}

		/**
		 * この列からデータを取得します。
		 * @param work 取得元の作品オブジェクト
		 * @return <code>work</code> から取得したデータ
		 */
		abstract CellType get(Work work);

		/**
		 * この列にデータを設定します。
		 * @param work 設定先の作品オブジェクト
		 * @param value <code>work</code> に設定するデータ
		 */
		abstract void set(WritableWork work, CellType value);

		/**
		 * この列の名前を取得します。
		 * @return 列名
		 */
		abstract String getName();

		/**
		 * この列の型を取得します。
		 * @return 列の型
		 */
		abstract Class<CellType> getType();

		/**
		 * この列のファイルフィルターを取得します。
		 * 列のデータがファイルのパスである場合に指定します。
		 * @return 列のファイルフィルター。
		 * 列のデータがファイルのパスでなければ <code>null</code>
		 */
		FileFilter[] getFileFilters() { return null; }
	}

	/**
	 * この表モデルにある列の配列です。
	 */
	Column<?>[] columns = {
			new Column<Integer>() {
				@Override String getName()
				{ return "制作年度"; }
				@Override Class<Integer> getType()
				{ return Integer.class; }
				@Override Integer get(Work work)
				{ return work.getYear(); }
				@Override void set(WritableWork work, Integer value)
				{ work.setYear(value); }
			}, new Column<String>() {
				@Override String getName()
				{ return "名前"; }
				@Override Class<String> getType()
				{ return String.class; }
				@Override String get(Work work)
				{ return work.getName(); }
				@Override void set(WritableWork work, String value)
				{ work.setName(value); }
			}, new Column<String>() {
				@Override String getName()
				{ return "入力デバイス"; }
				@Override Class<String> getType()
				{ return String.class; }
				@Override String get(Work work)
				{ return work.getInputDeviceName(); }
				@Override void set(WritableWork work, String value)
				{ work.setInputDeviceName(value); }
			}, new Column<File>() {
				final FileFilter[] fileFilters =
					{
						new FileNameExtensionFilter(
								"Windows アプリケーション (*.exe)",
								"exe"),
						new FileNameExtensionFilter(
								"コマンドスクリプト (*.bat; *.sh)",
								"bat", "sh"),
						null,
					};
				@Override FileFilter[] getFileFilters()
				{ return this.fileFilters; }
				@Override String getName()
				{ return "パス"; }
				@Override Class<File> getType()
				{ return File.class; }
				@Override File get(Work work)
				{ return work.getLaunchedFile(); }
				@Override void set(WritableWork work, File value)
				{ work.setPath(value); }
//			}, new Column() {
//				@Override String getName()
//				{ return "コマンドライン引数"; }
//				@Override Class<String[]> getType()
//				{ return String[].class; }
//				@Override Object get(WorkBean work)
//				{ return work.getArgs(); }
//				@Override void set(WritableWorkBean work, Object value)
//				{ work.setArgs((String[])value); }
			}, new Column<File>() {
				final FileFilter[] fileFilters =
					{
						new FileNameExtensionFilter(
								"画像ファイル (*.GIF; *.JFIF; *.jpg; *.JPEG; *.jpe; *.PNG)",
								"GIF", "JFIF", "jpg", "JPEG", "jpe", "PNG"),
					};
				@Override FileFilter[] getFileFilters()
				{ return this.fileFilters; }
				@Override String getName()
				{ return "アイコン"; }
				@Override Class<File> getType()
				{ return File.class; }
				@Override File get(Work work)
				{ return work.getIconFile(); }
				@Override void set(WritableWork work, File value)
				{ work.setIconFile(value); }
			}, new Column<File>() {
				final FileFilter[] fileFilters =
					{
						new FileNameExtensionFilter(
								"画像ファイル (*.GIF; *.JFIF; *.jpg; *.JPEG; *.jpe; *.PNG)",
								"GIF", "JFIF", "jpg", "JPEG", "jpe", "PNG"),
					};
				@Override FileFilter[] getFileFilters()
				{ return this.fileFilters; }
				@Override String getName()
				{ return "概要画像"; }
				@Override Class<File> getType()
				{ return File.class; }
				@Override File get(Work work)
				{ return work.getSummaryImageFile(); }
				@Override void set(WritableWork work, File value)
				{ work.setSummaryImageFile(value); }
			}, new Column<File>() {
				final FileFilter[] fileFilters =
					{
						new FileNameExtensionFilter(
								"文書ファイル (*.txt; *.UTF; *.HTML; *.htm)",
								"txt", "UTF", "HTML", "htm"),
						null,
					};
				@Override FileFilter[] getFileFilters()
				{ return this.fileFilters; }
				@Override String getName()
				{ return "詳細テキスト"; }
				@Override Class<File> getType()
				{ return File.class; }
				@Override File get(Work work)
				{ return work.getDetailTextFile(); }
				@Override void set(WritableWork work, File value)
				{ work.setDetailTextFile(value); }
			}
	};
	private ArrayList<Work> relatedList;

	/**
	 * 新しい作品表モデルを作成します。
	 */
	public WorkTableModel() {
		this(new ArrayList<Work>());
	}

	/**
	 * 指定された作品リストを使用する新しい作品表モデルを作成します。
	 * @param workList この表モデルで使う作品リスト
	 */
	public WorkTableModel(ArrayList<Work> workList) {
		this.relatedList = workList;
	}

	/**
	 * この表モデルで使っている作品リストを取得します。
	 * @return この表モデルで使っている作品リスト
	 */
	public ArrayList<Work> getWorkList() {
		return this.relatedList;
	}

	/**
	 * この表モデルで使う作品リストを設定します。
	 * @param workList この表モデルで使う作品リスト
	 * @exception ぬるぽ workList が null の場合
	 */
	public void setWorkList(ArrayList<Work> workList) {
		if(workList == null)
			throw new NullPointerException();
		this.relatedList = workList;
	}

	/**
	 * この表モデルの列数を取得します。
	 * @return この表モデルの列数
	 */
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	/**
	 * この表モデルの行数を取得します。
	 * @return この表モデルの行数
	 */
	@Override
	public int getRowCount() {
		return relatedList.size();
	}

	/**
	 * この表モデルの列の名前を取得します。
	 * @param col 列番号
	 * @return 列の名前
	 */
	@Override
	public String getColumnName(int col) {
		return this.columns[col].getName();
	}

	/**
	 * この表モデルの列の型を取得します。
	 * @param col 列番号
	 * @return 列の型
	 */
	@Override
	public Class<?> getColumnClass(int col) {
		return this.columns[col].getType();
	}

	/**
	 * この表モデルの値が編集可能かどうか取得します。
	 * @return true
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return this.getRowCount() > row &&
				this.getColumnCount() > col &&
				this.relatedList.get(row) instanceof WritableWork;
	}

	/**
	 * この表モデルの値を取得します。
	 * @param row 行番号
	 * @param col 列番号
	 * @return 値
	 */
	@Override
	public Object getValueAt(int row, int col) {
		return this.columns[col].get(row);
	}

	/**
	 * この表モデルの値を設定します。
	 * @param aValue 新しい値
	 * @param row 行番号
	 * @param col 列番号
	 */
	@Override
	public void setValueAt(Object aValue, int row, int col) {
		// 設定前後の値が等価ならイベントを起こさない
		final boolean changed = aValue != null
				? !aValue.equals(this.getValueAt(row, col))
						: this.getValueAt(row, col) != null;

		this.columns[col].set(row, aValue);
		if(changed)
			this.fireTableCellUpdated(row, col);
	}

	/**
	 * この表モデルに行を追加します。
	 * @param work 作品
	 */
	public void addRow(Work work) {
		this.relatedList.add(new WritableWork(work));
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
						WorkTableModel.this.fireTableDataChanged();
//						WorkTableModel.this.fireTableRowsInserted(
//						WorkTableModel.this.relatedList.size()-1,
//						WorkTableModel.this.relatedList.size()-1);
			}
		});
	}

	/**
	 * この表モデルから行を削除します。
	 * @param firstRow 削除する行範囲の初め
	 * @param lastRow 削除する行範囲の終わり
	 */
	public void removeRow(final int firstRow, final int lastRow) {
		if(firstRow > lastRow)
			return;
		for(int i = lastRow; i >= firstRow; --i)
			this.relatedList.remove(i);
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				WorkTableModel.this.fireTableDataChanged();
//				WorkTableModel.this.fireTableRowsDeleted(firstRow, lastRow);
			}
		});
	}

	/**
	 * この表モデルの、指定列のファイルフィルターを取得します。
	 * 列のデータがファイルのパスである場合に指定します。
	 * @return 列のファイルフィルター。
	 * 列のデータがファイルのパスでなければ <code>null</code>
	 */
	FileFilter[] getColumnFileFilters(int col) {
		return this.columns[col].getFileFilters();
	}

	/**
	 * この表モデルを年度順に整頓する {@link TableRowSorter} を作成し、返します。
	 * @return この表モデルを年度順に整頓する {@link TableRowSorter}
	 */
	public TableRowSorter<WorkTableModel> createRowSorter() {
		TableRowSorter<WorkTableModel> rowSorter = new TableRowSorter<WorkTableModel>(this);

		rowSorter.setSortKeys(Arrays.asList(new RowSorter.SortKey[] {
				new RowSorter.SortKey(0, SortOrder.DESCENDING),
				new RowSorter.SortKey(1, SortOrder.ASCENDING),
				new RowSorter.SortKey(3, SortOrder.ASCENDING),
		}));

		return rowSorter;
	}
}
