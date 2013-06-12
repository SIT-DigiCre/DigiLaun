package net.digicre.digilaun;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * DigiLaun の連続使用時間を背景色で表現するラベルです。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
class TimeLabel extends JLabel implements Runnable {

	/**
	 * ラベルのテキスト
	 */
	private final String[] TEXT = {
			"作品をえらんでください",
			"遊んでくれてありがとうございます!",
			"遊びすぎです…。",
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
	 * 新しい <code>TimeLabel</code> を作成します。
	 */
	TimeLabel() {
		super("読み込みちふ…", CENTER);

		java.awt.Font defaultFont = getFont();

		setBackground(Color.getHSBColor(
				0.0F, 0.0F, this.BACKGROUND_BRIGHTNESS));
		setOpaque(true);
		setFont(new java.awt.Font(
				defaultFont.getName(),
				defaultFont.getStyle(),
				22//defaultFont.getSize()
				));
	}
	
	/**
	 * ラベル更新タイマーを停止します。
	 * このメソッドは、ガーベッジコレクションによって解放されるときに呼び出されます。
	 * @exception Throwable 例外またはエラー
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			this.stopTimer();
		}
		finally {
			super.finalize();
		}
	}

	/**
	 * ラベル更新タイマーを停止します。
	 */
	void stopTimer() {
		Thread t;
		
		synchronized(this) {
			t = running;
			running = null;
		}
		
		if(t != null)
			t.interrupt();
	}

	/**
	 * ラベルの状態と、更新タイマーをリセットします。
	 */
	void resetTimer() {
		stopTimer();
		(running = new Thread(this)).start();
	}

	/**
	 * ラベルの背景色を更新します。
	 * @param dt タイマー起動からの経過時間 [ms]
	 */
	void updateBackgroundColor(long dt) {
		this.setBackground(Color.getHSBColor(
				dt / this.BACKGROUND_HUE_CYCLE + this.BACKGROUND_HUE_INIT,
				dt >= (this.TEXT.length-1) * this.TEXT_UPDATE_INTERVAL ?
						0.0F : this.BACKGROUND_SATUATION,
				this.BACKGROUND_BRIGHTNESS));
	}
	
	/**
	 * ラベル自動更新スレッドのエントリーポイントです。
	 * このメソッドは、<code>{@link stopTimer()}</code> が呼び出されるまで継続します。
	 */
	@Override
	public void run() {
		// タイマー起動時刻
		final long t0 = System.currentTimeMillis();
		// 現在時刻
		long t = t0;
		// タイマー起動からの経過時間
		long dt;
		// 前のループ時の dt
		long pt = -this.TEXT_UPDATE_INTERVAL;
		
		while(Thread.currentThread() == running) {
			// 背景色の更新
			updateBackgroundColor(dt=t-t0);
			// 必要ならテキストも更新
			if(dt/this.TEXT_UPDATE_INTERVAL != pt/this.TEXT_UPDATE_INTERVAL)
				synchronized(this) {
					// 最後のテキストを表示したらタイマーを止める
					if(dt/this.TEXT_UPDATE_INTERVAL >= this.TEXT.length-1) {
						this.setText(this.TEXT[(int)(dt/this.TEXT_UPDATE_INTERVAL)]);
						this.stopTimer();
						((MainJFrame)this.getParent()).freeze();
					}
					else
						this.setText(this.TEXT[(int)(dt/this.TEXT_UPDATE_INTERVAL)]);
				}
			
			pt = t;
			try {
				Thread.sleep((t+=UPDATE_INTERVAL) - System.currentTimeMillis());
			}
			catch(IllegalArgumentException e) {
			}
			catch(InterruptedException e) {
			}
		}
	}

}
