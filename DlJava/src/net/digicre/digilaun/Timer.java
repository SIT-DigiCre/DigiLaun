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
	 * ラベルのテキスト
	 */
	private final String[] TEXT = {
			"作品をえらんでください",
			"遊んでくれてありがとうございます!",
			"遊びすぎです…",
			"TIME UP!"
	};
	/**
	 * ラベルの更新周期 [ms]
	 */
	private final long UPDATE_INTERVAL = 60000L;
	/**
	 * テキストの更新間隔 [ms]
	 */
	private final long TEXT_UPDATE_INTERVAL  = 1200000L; 
	/**
	 * ラベル背景の色相の周期 [ms]
	 */
	private final float BACKGROUND_HUE_CYCLE = 1800E+3F;
	/**
	 * ラベル背景の初期色相
	 */
	private final float BACKGROUND_HUE_INIT = 1F / 3F;
	/**
	 * ラベル背景の彩度
	 */
	private final float BACKGROUND_SATUATION = 0.5F;
	/**
	 * ラベル背景の明るさ
	 */
	private final float BACKGROUND_BRIGHTNESS = 1.0F;
	/**
	 *  走っているスレッド<br>
	 *  別インスタンスを代入すると、元のスレッドは終了する
	 */
	private Thread running = null;
	/**
	 * この <code>Timer</code> に関連付けられたラベルです。
	 */
	private JLabel timerLabel;
	/**
	 * <code>{@link timerLabel}</code> に設定する背景色です。
	 */
	private Color labelBgColor;
	/**
	 * <code>{@link timerLabel}</code> に設定するテキストです。
	 */
	private String labelText;

	/**
	 * 新しいタイマーを作成します。
	 * @param label 関連付けるラベル
	 */
	Timer(JLabel label) {
		this.timerLabel = label;
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
		
		synchronized(this) {
			t = running;
			running = null;
		}
		
		if(t != null)
			t.interrupt();
	}

	/**
	 * ラベルの状態をリセットし、更新タイマーを開始します。
	 * タイマー作動中にこのメソッドを呼び出すと、タイマーをリセットします。
	 */
	void start() {
		stop();
		(running = new Thread(this)).start();
	}

	/**
	 * ラベルの背景色を更新します。
	 * @param dt タイマー起動からの経過時間 [ms]
	 */
	void updateLabelBackgroundColor(long dt) {
		this.labelBgColor = Color.getHSBColor(
				dt / this.BACKGROUND_HUE_CYCLE + this.BACKGROUND_HUE_INIT,
				dt >= (this.TEXT.length-1) * this.TEXT_UPDATE_INTERVAL ?
						0.0F : this.BACKGROUND_SATUATION,
				this.BACKGROUND_BRIGHTNESS);
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
		Timer.this.labelText = Timer.this.TEXT[(int)(
				dt / Timer.this.TEXT_UPDATE_INTERVAL
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
	 * このメソッドは、<code>{@link stopTimer()}</code>
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
		long pt = -this.TEXT_UPDATE_INTERVAL;
		
		while(Thread.currentThread() == running) {
			// 背景色の更新
			updateLabelBackgroundColor(dt);
			// 必要ならテキストも更新
			if(dt/this.TEXT_UPDATE_INTERVAL != pt/this.TEXT_UPDATE_INTERVAL)
				synchronized(this) {
					// 最後のテキストを表示したらタイマーを止める
					updateLabelText(dt);
					if(dt/this.TEXT_UPDATE_INTERVAL >= this.TEXT.length-1) {
						this.stop();
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								System.out.println(timerLabel.getParent());
								timerLabel.getParent().setEnabled(false);
							}
						});
					}
				}
			
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
