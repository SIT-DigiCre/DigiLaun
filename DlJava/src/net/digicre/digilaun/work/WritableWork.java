package net.digicre.digilaun.work;

import java.util.GregorianCalendar;

/**
 * データを編集可能な作品のマメのクラスです。
 * @author p10090
 */
public class WritableWork extends Work {
	/**
	 * 新しい作品のマメを作成します。
	 */
	public WritableWork() {
		this.setDefaultValues();
	}

	/**
	 * この作品に既定値を設定します。
	 */
	public void setDefaultValues() {
		final GregorianCalendar gc = new GregorianCalendar();

		this.name = "";
		this.year = gc.get(GregorianCalendar.YEAR);
		this.path = "";
		this.args = new String[0];
		this.iconPath = "";
		this.summaryImagePath = "";
		this.detailTextPath = "";
		this.inputDeviceName = "キーボードまたはゲームパッド";
	}

	/**
	 * 既存の作品オブジェクトと同じ内容を持つ、新しい作品のマメを作成します。
	 * @param work コピー元の作品オブジェクト
	 */
	public WritableWork(Work work) {
		this.name = work.name;
		this.year = work.year;
		this.path = work.path;
		this.args = work.args;
		this.iconPath = work.iconPath;
		this.summaryImagePath = work.summaryImagePath;
		this.detailTextPath = work.detailTextPath;
		this.inputDeviceName = work.inputDeviceName;
	}

	/**
	 * 作品名を設定します。
	 * @param name 作品名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 制作年度を設定します。
	 * @param year 制作年度
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * 開くファイルへのパスを設定します。
	 * @param path 開くファイルへのパス
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 開く実行ファイルへのコマンドライン引数を設定します。
	 * @param args コマンドライン引数
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	/**
	 * アイコンへのパスを設定します。
	 * @param path アイコンへのパス
	 */
	public void setIconPath(String path) {
		this.iconPath = path;
	}

	/**
	 * 概要イメージへのパスを設定します。
	 * @param path 概要イメージへのパス
	 */
	public void setSummaryImagePath(String path) {
		this.summaryImagePath = path;
	}

	/**
	 * 詳細テキストへのパスを設定します。
	 * @param path 詳細テキストへのパス
	 */
	public void setDetailTextPath(String path) {
		this.detailTextPath = path;
	}

	/**
	 * 入力デバイス名を設定します。
	 * @param deviceName 入力デバイス名
	 */
	public void setInputDeviceName(String deviceName) {
		this.inputDeviceName = deviceName;
	}
}
