package net.digicre.digilaun.work.settable;

import java.io.File;
import java.util.GregorianCalendar;

import net.digicre.digilaun.work.Work;

/**
 * データを編集可能な作品のクラスです。
 * @author p10090
 */
public class SettableWork extends Work {
	/**
	 * 新しい作品を作成します。
	 */
	public SettableWork() {
		this.setDefaultValues();
	}

	/**
	 * この作品に既定値を設定します。
	 */
	public void setDefaultValues() {
		final GregorianCalendar gc = new GregorianCalendar();

		this.name             = "";
		this.year             = gc.get(GregorianCalendar.YEAR);
		this.launchedFile     = null;
		this.args             = new String[0];
		this.iconFile         = null;
		this.summaryImageFile = null;
		this.detailTextFile   = null;
		this.inputDeviceName  = "キーボードまたはゲームパッド";
	}

	/**
	 * 既存の作品オブジェクトと同じ内容を持つ、新しい作品を作成します。
	 * @param work コピー元の作品オブジェクト
	 */
	public SettableWork(Work work) {
		this.name             = work.getName();
		this.year             = work.getYear();
		this.launchedFile     = work.getLaunchedFile();
		this.args             = work.getArgs();
		this.iconFile         = work.getIconFile();
		this.summaryImageFile = work.getSummaryImageFile();
		this.detailTextFile   = work.getDetailTextFile();
		this.inputDeviceName  = work.getInputDeviceName();
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
	 * 開くファイルを設定します。
	 * @param file 開くファイル
	 */
	public void setLaunchedFile(File file) {
		this.launchedFile = file;
	}

	/**
	 * 開く実行ファイルへのコマンドライン引数を設定します。
	 * @param args コマンドライン引数
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	/**
	 * アイコンファイルを設定します。
	 * @param file アイコンファイル
	 */
	public void setIconFile(File file) {
		this.iconFile = file;
	}

	/**
	 * 概要イメージファイルを設定します。
	 * @param file 概要イメージ
	 */
	public void setSummaryImageFile(File file) {
		this.summaryImageFile = file;
	}

	/**
	 * 詳細テキストファイルを設定します。
	 * @param file 詳細テキストファイル
	 */
	public void setDetailTextFile(File file) {
		this.detailTextFile = file;
	}

	/**
	 * 入力デバイス名を設定します。
	 * @param deviceName 入力デバイス名
	 */
	public void setInputDeviceName(String deviceName) {
		this.inputDeviceName = deviceName;
	}
}
