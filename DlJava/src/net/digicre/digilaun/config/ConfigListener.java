package net.digicre.digilaun.config;

import java.util.EventListener;

/**
 * @author p10090
 * コンフィグが UI などで変更された時のイベントを扱うリスナーです。
 */
public interface ConfigListener extends EventListener {
	/**
	 * コンフィグが少しでも変更された時の処理です。
	 * @param e イベントオブジェクト
	 */
	void configChanged(ConfigEvent e);
}
