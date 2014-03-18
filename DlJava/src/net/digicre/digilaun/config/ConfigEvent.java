package net.digicre.digilaun.config;

import java.util.EventObject;

/**
 * コンフィグイベントのクラスです。
 * @author p10090
 */
@SuppressWarnings("serial")
public class ConfigEvent extends EventObject {
	/**
	 * 新しいコンフィグイベントを作成します。
	 * @param source イベント発生元
	 */
	public ConfigEvent(ConfigPanel source) {
		super(source);
	}

	/**
	 * このイベントの発生元を取得します。
	 * @return イベント発生元
	 */
	@Override
	public ConfigPanel getSource() {
		return (ConfigPanel)super.getSource();
	}
}
