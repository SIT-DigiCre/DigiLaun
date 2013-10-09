/**
 * 
 */
package net.digicre.digilaun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;

import net.digicre.digilaun.work.Work;

/**
 * 作品プロセス/ファイルのログをファイルに書き込む機能を提供します。
 * @author p10090
 *
 */
class ProcessLogger {
	/**
	 * 作品の起動ステータスを表す列挙型です。
	 * @author p10090
	 */
	static enum OpenStatus {
		/** 起動成功 */
		Started {
			@Override public String toString() { return "Started"; }
		},
		/** 最後まで正常 */
		ExitSuccessful {
			@Override public String toString() { return "ExitSuccessful"; }
		},
		/** 異常終了 */
		ExitFailed {
			@Override public String toString() { return "ExitFailed"; }
		},
		/** 強制終了 */
		Killed {
			@Override public String toString() { return "Killed"; }
		},
		/** ゲームではない作品を開いた */
		Opened {
			@Override public String toString() { return "Opened"; }
		},
		/** 開けない */
		CannotOpen {
			@Override public String toString() { return "CannotOpen"; }
		};
		
		@Override
		abstract public String toString();
	}

	/**
	 * 作品の体験ログを書き出すファイルへのパスです。
	 * このファイルは体験時に追記モードで開かれ、
	 * 作品起動ファイルのパス、ステータス、そして日時が書き込まれます。<br>
	 * ログに書き込まれるステータスは以下の通りです。
	 * <ul>
	 * <li>アプリ実行開始 <code>({@link OpenStatus#Started})</code></ul>
	 * <li>アプリ強制終了 <code>({@link OpenStatus#Killed})</code></ul>
	 * <li>アプリ正常終了 <code>({@link OpenStatus#ExitSuccessful})</code></ul>
	 * <li>アプリ異常終了 <code>({@link OpenStatus#ExitFailed})</code></ul>
	 * <li>ファイルを開いた <code>({@link OpenStatus#Opened})</code></ul>
	 * <li>体験不可 <code>({@link OpenStatus#CannotOpen})</code></ul>
	 * </ul>
	 * @see OpenStatus
	 */
	static final String LOG_FILE_PATH = "Log.csv";
	
	/**
	 * CSV に書き込む各フィールドの名前です。
	 */
	static final String[] FIELD_NAMES = {
		"作品名",
		"ファイル",
		"ステータス",
		"年",
		"月",
		"日",
		"時",
		"分",
		"秒",
		"グリニッジ標準時からの経過時間 [ms]",
	};
	
	private final CSVWriter writer;
	
	/**
	 * {@link LOG_FILE_PATH} で示されているファイルへログを書き込む、
	 * 新しい <code>ProcessLogger</code> を作成します。
	 * @throws IOException 入出力例外
	 * @see ProcessLogger#ProcessLogger(String), LOG_FILE_PATH
	 */
	ProcessLogger() throws IOException {
		this(LOG_FILE_PATH);
	}

	/**
	 * パスで指定されたファイルへログを書き込む、
	 * 新しい <code>ProcessLogger</code> を作成します。
	 * @param filename ログ先のファイル名
	 * @throws IOException 入出力例外
	 * @see ProcessLogger#ProcessLogger(java.io.File)
	 */
	ProcessLogger(String filename) throws IOException {
		this(new File(filename));
	}
	
	/**
	 * 指定されたファイルへログを書き込む、
	 * 新しい <code>ProcessLogger</code> を作成します。
	 * @param file ログ先のファイル
	 * @throws IOException 入出力例外
	 * @see ProcessLogger#ProcessLogger(java.io.FileOutputStream)
	 */
	ProcessLogger(File file) throws IOException {
		this(new FileOutputStream(file, true));
		if(file.length() == 0L)
			this.writer.writeCSVRecord(FIELD_NAMES);
	}
	
	/**
	 * 新しい <code>ProcessLogger</code> を作成します。
	 * @param out ログの出力先ストリーム
	 * @throws IOException 入出力例外
	 * @see ProcessLogger#ProcessLogger(java.io.Writer)
	 */
	ProcessLogger(OutputStream out) throws IOException {
		this(new OutputStreamWriter(out));
	}
	
	/**
	 * 新しい <code>ProcessLogger</code> を作成します。
	 * @param writer ログの出力に使う <code>{@link java.io.Writer}</code>
	 * @throws IOException 入出力例外
	 */
	ProcessLogger(Writer writer) throws IOException {
		this.writer = new CSVWriter(writer);
	}
	
	/**
	 *  作品のプロセス状態を書き込みます。
	 * @param work 作品
	 * @param status ステータス
	 * @throws IOException 入出力例外
	 * @see OpenStatus
	 */
	void writeWorkStatus(Work work, OpenStatus status) throws IOException {
		Calendar c = Calendar.getInstance();
		this.writer.writeCSVRecord(new String[] {
				work.getName(),
				work.getPath(),
				status.toString(),
				Integer.toString(c.get(Calendar.YEAR)),
				Integer.toString(c.get(Calendar.MONTH)),
				Integer.toString(c.get(Calendar.DATE)),
				Integer.toString(c.get(Calendar.HOUR)),
				Integer.toString(c.get(Calendar.MINUTE)),
				Integer.toString(c.get(Calendar.SECOND)),
				Long.toString(c.getTimeInMillis()),
		});
	}
	
	/**
	 * ログライターを閉じ、ログを終了します。
	 */
	void close() {
		try {
			this.writer.close();
		} catch (IOException e) {
		}
	}
}
