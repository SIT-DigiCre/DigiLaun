package net.digicre.digilaun;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JLabel;

/**
 * Digi Laun 独特のタイマー機能を提供します。
 * @author p10090
 *
 */
class Timer implements Runnable {
	/**
	 * ラベルのテキストです。
	 */
	private static final String[] TEXT = {
			"作品をえらんでください",
			"遊んでくれてありがとうございます!",
			"遊びすぎです…",
			"TIME UP!"
	};
	/**
	 * ラベルの更新周期 [ms] です。
	 */
	private static final long UPDATE_INTERVAL = 10000L;
	/**
	 * テキストの更新間隔 [ms] です。
	 */
	private static final long TEXT_UPDATE_INTERVAL  =
			3600000L/(TEXT.length-1); 
	/**
	 * ラベル背景の色相の周期 [ms] です。
	 */
	private static final float BACKGROUND_HUE_CYCLE =
			3600E+3F/2F;
	/**
	 * ラベル背景の初期色相です。
	 */
	private static final float BACKGROUND_HUE_INIT = 1F / 3F;
	/**
	 * ラベル背景の彩度です。
	 */
	private static final float BACKGROUND_SATUATION = 0.5F;
	/**
	 * ラベル背景の明るさです。
	 */
	private static final float BACKGROUND_BRIGHTNESS = 1.0F;
	/**
	 *  走っているスレッドです。<br>
	 *  別のインスタンスを代入すると、元のスレッドは終了します。
	 */
	private Thread running = null;
	/**
	 * この <code>Timer</code> に関連付けられたラベルです。
	 */
	private JLabel timerLabel;
	/**
	 * <code>{@link #timerLabel}</code> に設定する背景色です。
	 */
	private Color labelBgColor;
	/**
	 * <code>{@link #timerLabel}</code> に設定するテキストです。
	 */
	private String labelText;
	/**
	 * タイマーが時間切れになったときのコールバックです。
	 */
	private Runnable callback;

	/**
	 * 新しいタイマーを作成します。
	 * @param label 関連付けるラベル
	 * @param callback 時間切れになったときのコールバック
	 */
	Timer(JLabel label, Runnable callback) {
		this.timerLabel = label;
		this.callback = callback;
	}

	/**
	 * ラベル更新タイマーを停止します。
	 * このメソッドは、ガーベッジ
	 * コレクションによって解放されるときに呼び出されます。
	 * @exception Throwable 例外またはエラー
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			this.stop();
		}
		finally {
			super.finalize();
		}
	}

	/**
	 * ラベル更新タイマーを停止します。
	 */
	void stop() {
		Thread t;

		// このタイマーからランニングスレッドへの参照を切り離す
		synchronized(this) {
			t = running;
			running = null;
		}

		// 割り込んですぐに停止させる
		if(t != null)
			t.interrupt();
	}

	/**
	 * ラベルの状態をリセットし、更新タイマーを開始します。
	 * タイマー作動中にこのメソッドを呼び出すと、タイマーをリセットします。
	 */
	void start() {
		// 現行のスレッドを止める
		stop();
		// 頒布モードならラベルを初期化して終了
		if(DigiLaun.config.getMode() == Config.Mode.DISTRIBUTION) {
			updateLabelText(0L);
			updateLabelBackgroundColor(0L);
			return;
		}
		(running = new Thread(this)).start();
	}

	/**
	 * ラベルの背景色を更新します。
	 * @param dt タイマー起動からの経過時間 [ms]
	 */
	void updateLabelBackgroundColor(long dt) {
		this.labelBgColor = Color.getHSBColor(
				dt / BACKGROUND_HUE_CYCLE + BACKGROUND_HUE_INIT,
				dt >= (TEXT.length-1) * TEXT_UPDATE_INTERVAL ?
						0.0F : BACKGROUND_SATUATION,
				BACKGROUND_BRIGHTNESS);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Timer.this.timerLabel.setBackground(Timer.this.labelBgColor);
			}
		});
	}

	/**
	 * ラベルのテキストを更新します。
	 * @param dt タイマー起動からの経過時間 [ms]
	 */
	void updateLabelText(long dt) {
		Timer.this.labelText = Timer.TEXT[(int)(
				dt / Timer.TEXT_UPDATE_INTERVAL
				)];
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Timer.this.timerLabel.setText(Timer.this.labelText);
			}
		});
	}

	/**
	 * ラベル自動更新スレッドのエントリーポイントです。
	 * このメソッドは、<code>{@link #stop()}</code>
	 *  が呼び出されるまで継続します。
	 */
	@Override
	public void run() {
		// タイマー起動時刻
		final long t0 = System.currentTimeMillis();
		// 現在時刻
		long t = t0;
		// タイマー起動からの経過時間
		long dt = 0;
		// 前のループ時の dt
		long pt = -TEXT_UPDATE_INTERVAL;

		// 定期ループ
		while(Thread.currentThread() == running) {
			// 背景色の更新
			updateLabelBackgroundColor(dt);
			// 必要ならテキストも更新
			if(dt/TEXT_UPDATE_INTERVAL != pt/TEXT_UPDATE_INTERVAL)
				synchronized(this) {
					updateLabelText(dt);
					// 最後のテキストを表示したらタイマーを止める
					if(dt/TEXT_UPDATE_INTERVAL >= TEXT.length-1) {
						this.stop();
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								callback.run();
							}
						});
					}
				}

			// 次の周期まで寝る
			pt = t;
			try {
				Thread.sleep((t+=UPDATE_INTERVAL) - System.currentTimeMillis());
			}
			catch(IllegalArgumentException e) {
				t = System.currentTimeMillis();
			}
			catch(InterruptedException e) {
				t = System.currentTimeMillis();
			}
			dt = t - t0;
		}
	}

}
