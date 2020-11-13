/**
 * 
 */
package net.digicre.digilaun;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 入出力ストリームを接続するコネクターのクラスです。
 * @author p10090
 *
 */
public class StreamConnector {
	/**
	 * バッファーの最小サイズです。
	 */
	public static final int MIN_BUFFER_SIZE = 1;

	/**
	 * バッファーの既定サイズです。
	 */
	public static final int DEFAULT_BUFFER_SIZE = 0x400;

	/**
	 * バッファーの最大サイズです。
	 */
	public static final int MAX_BUFFER_SIZE = 0x100000;

	/**
	 * このオブジェクトのバッファーのサイズです。
	 */
	public final int bufferSize;

	/**
	 * ストリームの入出力を右から左へ流すスレッドです。
	 * @author p10090
	 *
	 */
	private class ConnectionThread extends Thread {
		@Override
		public void run() {
			byte[] buffer = new byte[bufferSize];
			int readBytesNum;
			
			try {
				while((readBytesNum = in.read(buffer)) >= 0) {
					if(readBytesNum != 0)
						out.write(buffer, 0, readBytesNum);
					if(interrupted())
						break;
					yield();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * このオブジェクトが接続している入力ストリームです。
	 */
	public final InputStream in;

	/**
	 * このオブジェクトが接続している出力ストリームです。
	 */
	public final OutputStream out;

	/**
	 * このオブジェクトの接続スレッドです。
	 */
	private ConnectionThread connectionThread = null;

	/**
	 * 指定したストリームを接続する、新しいコネクターを作成します。
	 * @param in 接続する入力ストリーム
	 * @param out 接続する出力ストリーム
	 */
	public StreamConnector(InputStream in, OutputStream out) {
		this(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 指定したストリームを接続する、新しいコネクターを作成します。
	 * @param in 接続する入力ストリーム
	 * @param out 接続する出力ストリーム
	 * @param bufferSize 入出力バッファーのサイズ
	 */
	public StreamConnector(InputStream in, OutputStream out,
			int bufferSize) {
		this.bufferSize = Math.min(Math.max(
				MIN_BUFFER_SIZE, bufferSize), MAX_BUFFER_SIZE);
		this.in  = in;
		this.out = out;
	}

	/**
	 * ガーベッジコレクターにより破棄される前の処理です。
	 * ストリームを切断します。
	 * @see #disconnect()
	 */
	@Override
	protected void finalize() throws Throwable {
		this.disconnect();
	}

	/**
	 * このオブジェクトがストリームを接続中であるかどうかを返します。
	 * @return 接続中なら真
	 */
	public boolean isConnecting() {
		synchronized(this) {
			return this.connectionThread != null &&
					this.connectionThread.isAlive();
		}
	}

	/**
	 * ストリームを接続します。
	 */
	public void connect() {
		synchronized(this) {
			if(this.isConnecting())
				return;
			(this.connectionThread = new ConnectionThread()).start();
		}
	}

	/**
	 * ストリームを切断します。
	 */
	public void disconnect() {
		synchronized(this) {
			if(this.connectionThread != null &&
					this.connectionThread.isAlive())
				this.connectionThread.interrupt();
		}
	}
}
