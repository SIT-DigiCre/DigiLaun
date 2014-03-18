package net.digicre.digilaun;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * CSV レコードを書き込むためのクラスです。
 * @author p10090
 *
 */
public class CSVWriter extends BufferedWriter {

	/**
	 * デフォルトサイズの出力バッファーでバッファリングされた、
	 * CSV 出力ストリームを作成します。
	 * @see java.io.BufferedWriter#BufferedWriter(Writer)
	 */
	public CSVWriter(Writer out) {
		super(out);
	}

	/**
	 *  指定されたサイズの出力バッファーでバッファリングされた、
	 *  CSV 出力ストリームを新しく作成します。
	 * @see java.io.BufferedWriter#BufferedWriter(Writer, int)
	 */
	public CSVWriter(Writer out, int sz) {
		super(out, sz);
	}

	/**
	 * 文字列の配列を CSV レコードとして書き込みます。
	 * @param fields 書き込むレコード
	 * @throws IOException 出力例外
	 */
	public void writeCSVRecord(String... fields) throws IOException {
		this.writeCSVRecord(Arrays.asList(fields));
	}

	/**
	 * 文字列のコレクションを CSV レコードとして書き込みます。
	 * @param fields 書き込むレコード
	 * @throws IOException 出力例外
	 */
	public synchronized void
	writeCSVRecord(List<String> fields) throws IOException {
		synchronized(fields) {
			Iterator<String> i = fields.iterator();
			while(true) {
				this.append('"');
				this.write(i.next().replace("\"", "\"\""));
				this.append('"');
				if(i.hasNext())
					this.append(',');
				else {
					this.newLine();
					break;
				}
			}
		}
	}

	/**
	 * 文字列の二次元コレクションを CSV として連続で書き込みます。
	 * @param records 書き込むレコード群
	 * @throws IOException 出力例外
	 */
	public synchronized void
	writeCSVRecords(List<List<String>> records) throws IOException {
		synchronized(records) {
			for(List<String> record : records)
				this.writeCSVRecord(record);
		}
	}
}
