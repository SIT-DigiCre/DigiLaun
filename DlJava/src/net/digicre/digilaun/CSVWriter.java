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
	 * @see java.io.BufferedWriter#BufferedWriter(Writer)
	 */
	public CSVWriter(Writer out) {
		super(out);
	}

	/**
	 * @see java.io.BufferedWriter#BufferedWriter(Writer, int)
	 */
	public CSVWriter(Writer out, int sz) {
		super(out, sz);
	}

	/**
	 * 文字列の配列を CSV レコードとして書き込みます。
	 * @param fields 書き込むレコード
	 * @throws IOException 入出力例外
	 */
	void writeCSVRecord(String... fields) throws IOException {
		this.writeCSVRecord(Arrays.asList(fields));
	}

	/**
	 * 文字列の配列を CSV レコードとして書き込みます。
	 * @param fields 書き込むレコード
	 * @throws IOException 入出力例外
	 */
	void writeCSVRecord(List<String> fields) throws IOException {
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
