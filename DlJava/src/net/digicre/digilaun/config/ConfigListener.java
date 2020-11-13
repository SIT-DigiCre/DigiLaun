package net.digicre.digilaun.config;

import java.util.EventListener;

/**
 * コンフィグが UI などで変更された時のイベントを扱うリスナーです。
 * @author p10090
 */
public interface ConfigListener extends EventListener {
	/**
	 * コンフィグが少しでも変更された時の処理です。
	 * @param e イベントオブジェクト
	 */
	void configChanged(ConfigEvent e);
}
