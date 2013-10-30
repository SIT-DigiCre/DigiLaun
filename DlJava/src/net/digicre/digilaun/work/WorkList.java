package net.digicre.digilaun.work;

import java.io.IOException;
import java.util.LinkedList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 作品集のクラスです。
 * このクラスは<code>{@link net.digicre.digilaun.work.Work}</code>オブジェクトの
 * 双方向リスト (<code>{@link LinkedList}</code>) を実装し、また XML
 * ドキュメントから作品データを読み込むメソッドを提供します。
 * @author p10090
 * @see Work
 */
public class WorkList extends LinkedList<Work> {
	private static final long serialVersionUID = 5127279267726729893L;

	/**
	 * 作品データ XML のパーサークラスです。
	 * @author p10090
	 *
	 */
	private class SAXHandler extends DefaultHandler {
		/**
		 * 作成中の作品オブジェクトです。
		 */
		private Work work;

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if(work != null)
				work.setName(String.valueOf(ch, start, length));
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(qName == Work.XML_ELEMENT_WORK &&
					work != null) {
				WorkList.this.add(work);
				work = null;
			}
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			if(qName == Work.XML_ELEMENT_WORK)
				(work = new Work()).setPropertiesByXMLAtts(atts);
		}
	}

	/**
	 * 既定の読み込み元 XML ファイル名です。
	 */
	public static final String DEFAULT_INPUT_PATH = "works.xml";

	/**
	 * 新しい作品集を作成します。
	 */
	public WorkList() {
		super();
	}

	/**
	 * 既定の読み込み元から作品データを読み込み、このコレクションに追加します。
	 * @throws SAXException SAX 読み込み例外
	 * @throws IOException 入出力例外
	 */
	public void readFromXMLDocument() throws SAXException, IOException {
		readFromXMLDocument(new InputSource(DEFAULT_INPUT_PATH));
	}

	/**
	 * XML ドキュメントから作品データを読み込み、このコレクションに追加します。
	 * @param in 入力元
	 * @throws SAXException SAX 読み込み例外
	 * @throws IOException 入出力例外
	 */
	public void readFromXMLDocument(InputSource in)
			throws SAXException, IOException {
		XMLReader reader = XMLReaderFactory.createXMLReader();
		SAXHandler handler = new SAXHandler();

		reader.setEntityResolver(handler);
		reader.setDTDHandler(handler);
		reader.setContentHandler(handler);
		reader.setErrorHandler(handler);
		reader.parse(in);
	}
}
