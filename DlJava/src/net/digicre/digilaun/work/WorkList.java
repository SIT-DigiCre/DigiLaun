package net.digicre.digilaun.work;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import net.digicre.digilaun.Config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
	 * 作品データ XML のパーサークラスです。 (スレッドセーフ)
	 * @author p10090
	 * @deprecated
	 * このクラスは、 Digi Laun バージョン 2.x との互換性のために残されています。
	 */
	private class SAXHandler extends org.xml.sax.helpers.DefaultHandler {
		/**
		 * 作成中の作品オブジェクトです。
		 */
		private Work work;

		@Override
		public void characters(char[] ch, int start, int length)
				throws org.xml.sax.SAXException {
			final Work work = this.work;

			if(work != null)
				work.name = String.valueOf(ch, start, length).
						replaceAll("^\\s+|\\s+$", "");
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws org.xml.sax.SAXException {
			if(qName == Work.XML_ELEMENT_WORK) synchronized(this) {
				if(this.work != null) {
					WorkList.this.add(this.work);
					this.work = null;
				}
			}
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				org.xml.sax.Attributes atts) throws org.xml.sax.SAXException {
			if(qName == Work.XML_ELEMENT_WORK)
				(work = new Work()).setPropertiesByXMLAtts(atts);
		}
	}

	/**
	 * 既定の読み込み元 XML ファイル名です。
	 * @deprecated
	 * このフィールドは、 Digi Laun バージョン 2.x との互換性のために残されています。
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
	 * @deprecated
	 * このメソッドは、 Digi Laun バージョン 2.x との互換性のために残されています。
	 * @throws SAXException SAX 読み込み例外
	 * @throws IOException 入出力例外
	 */
	public void readFromXMLDocument() throws org.xml.sax.SAXException, IOException {
		readFromXMLDocument(new org.xml.sax.InputSource(DEFAULT_INPUT_PATH));
	}

	/**
	 * XML ドキュメントから作品データを読み込み、このコレクションに追加します。
	 * @deprecated
	 * このメソッドは、 Digi Laun バージョン 2.x との互換性のために残されています。
	 * @param in 入力元
	 * @throws SAXException SAX 読み込み例外
	 * @throws IOException 入力例外
	 */
	public void readFromXMLDocument(org.xml.sax.InputSource in)
			throws org.xml.sax.SAXException, IOException {
		org.xml.sax.XMLReader reader =
				org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
		SAXHandler handler = new SAXHandler();

		reader.setEntityResolver(handler);
		reader.setDTDHandler(handler);
		reader.setContentHandler(handler);
		reader.setErrorHandler(handler);
		reader.parse(in);

		this.sortByYear();
	}

	/**
	 * 新しい作品オブジェクトを追加します。
	 * 既に等しい作品オブジェクトを持っている場合は、新しい作品オブジェクトと置換します。
	 * @param n 新しい作品オブジェクトの挿入箇所
	 * @param work 新しい作品オブジェクト
	 * @see Work#equals(Object)
	 */
	@Override
	public void add(int n, Work work) {
		// 重複するなら置換
		synchronized(this) {
			for(ListIterator<Work> i = this.listIterator(); i.hasNext();) {
				final Work iw = i.next();
				if(work.equals(iw)) {
					i.set(work);
					return;
				}
			}
		}
		// 全く新しければセット
		super.add(n, work);
	}

	/**
	 * この作品リストを制作年度の新しい順にソートします。
	 */
	public void sortByYear() {
		Collections.sort(this, new Work.YearComparator());
	}

	/**
	 * DOM 形式で読み込んだ XML 要素からこのリストに作品データを追加します。
	 * @param worksElement DOM 要素
	 */
	public void addFromXMLElements(Element worksElement) {
		for(Node n = worksElement.getFirstChild();
				n != null; n = n.getNextSibling()) {
			if(n.getNodeType() != Node.ELEMENT_NODE ||
					n.getNodeName() != Work.XML_ELEMENT_WORK)
				continue;
			this.add(Work.createFromXMLElement((Element)n));
		}
	}

	/**
	 * このリストに含まれる作品豆の、
	 * 書き換え不可能なコピーをリストに格納して取得します。
	 * @return この作品リストの1段深いコピー
	 * (各要素は {@link Work} に変換される)
	 */
	synchronized public WorkList getCopy() {
		WorkList copy = new WorkList();

		for(Work work : this)
			copy.add(new Work(work));
		return copy;
	}

	/**
	 * このリストに含まれる作品豆の、
	 * 書き換え可能なコピーをリストに格納して取得します。
	 * @return この作品リストの1段深いコピー
	 * (各要素は {@link WritableWork} に変換される)
	 */
	synchronized public WorkList getWritableCopy() {
		WorkList copy = new WorkList();

		for(Work work : this)
			copy.add(new WritableWork(work));
		return copy;
	}

	public synchronized
	Element createXMLElement(Document document) {
		return this.createXMLElement(document, Config.XML_ELEM_WORKS);
	}

	public synchronized
	Element createXMLElement(Document document, String parentElementName) {
		Element parentElement = document.createElement(parentElementName);

		for(Work work : this)
			parentElement.appendChild(work.saveToXMLElement(document));

		return parentElement;
	}
}
