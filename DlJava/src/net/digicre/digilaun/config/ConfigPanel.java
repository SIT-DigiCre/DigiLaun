package net.digicre.digilaun.config;

import java.util.LinkedHashSet;
import javax.swing.JPanel;

import net.digicre.digilaun.Config;

/**
 * 環境設定を行うパネルの抽象クラスです。
 * @author p10090
 */
@SuppressWarnings("serial")
abstract public class ConfigPanel extends JPanel {
	/**
	 * このパネルに登録されたコンフィグリスナーです。
	 * @see ConfigListener
	 */
	protected LinkedHashSet<ConfigListener> configListeners =
			new LinkedHashSet<ConfigListener>();

	/**
	 * このパネルにコンフィグリスナーを追加します。
	 * リスナーがすでに追加されていれば何もしません。
	 * @param listener コンフィグリスナー
	 * @see ConfigListener
	 */
	public void addConfigListener(ConfigListener listener) {
		synchronized(this.configListeners) {
			configListeners.add(listener);
		}
	}

	/**
	 * このパネルからコンフィグリスナーを削除します。
	 * リスナーが追加されていなければ何もしません。
	 * @param listener 削除するコンフィグリスナー
	 * @see ConfigListener
	 */
	public void removeConfigListener(ConfigListener listener) {
		synchronized(this.configListeners) {
			configListeners.remove(listener);
		}
	}

	/**
	 * configChanged イベントを発生させ、リスナーに通知します。
	 * コンフィグがこのパネルで少しでも変更された時に呼び出します。
	 * @see ConfigListener
	 */
	protected void fireConfigChanged() {
		synchronized(this.configListeners) {
			for(ConfigListener listener : this.configListeners)
				listener.configChanged(new ConfigEvent(this));
		}
	}

	/**
	 * このパネルでのコンフィグを <code>{@link Config}</code> に適用します。
	 * @param config コンフィグ適用先のオブジェクト
	 */
	abstract public void applyConfig(Config config);
}
