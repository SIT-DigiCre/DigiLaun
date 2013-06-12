package net.digicre.digilaun;

import java.awt.EventQueue;

public class DigiLaun {
	private static String[] args = null;

	/**
	 * プログラムのエントリーポイントです。
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		DigiLaun.args = args;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainJFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * コマンドライン引数を取得します。
	 * @return コマンドライン引数
	 */
	public static String[] getArguments() {
		return args;
	}
}
