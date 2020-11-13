package net.digicre.digilaun;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import net.digicre.digilaun.work.Work;
import net.digicre.digilaun.work.WorkList;

/**
 * Digi Laun のコンフィグを扱うクラスです。
 * @author p10090
 */
public class Config {
	/**
	 * Digi Laun のモードを表すデータの列挙型です。
	 * @author p10090
	 */
	public static enum Mode {
		/** 頒布モード (時間無制限、普通に終了できる) */
		DISTRIBUTION,
		/** 展示モード (時間制限付、隠しコマンドで終了する) */
		DISPLAY
	}

	/**
	 * 既定の設定ファイル名です。
	 */
	public static final String DEFAULT_INI_PATH = "DigiLaun.xml";

	/**
	 * 出力する XML ファイルの文字コードです。
	 */
	public static final String OUTPUT_ENCODING = "UTF-8";

	/**
	 * XML ファイルにおける、ルート要素の名前です。
	 */
	public static final String XML_ELEM_ROOT = "DigiLaun";

	/**
	 * XML ファイルにおける、モード要素の名前です。
	 * @see #getMode()
	 */
	public static final String XML_ELEM_MODE = "mode";

	/**
	 * XML ファイルにおける、モード要素の値属性の名前です。
	 * @see Mode
	 */
	public static final String XML_ATTR_MODE_VALUE = "value";

	/**
	 * XML ファイルにおける、設定要素の名前です。
	 */
	public static final String XML_ELEM_SETTINGS = "settings";

	/**
	 * XML ファイルにおける、作品集合要素の名前です。
	 * @see #getWorks()
	 */
	public static final String XML_ELEM_WORKS = "works";

	/**
	 * Digi Laun のモードです。
	 * @see Mode
	 */
	private Mode mode = Mode.DISPLAY;

	/**
	 * 作品のコレクションです。
	 * @see WorkList
	 */
	private WorkList works;

	/**
	 * Digi Laun の起動モードを取得します。
	 * @return Digi Laun の起動モード
	 */
	public Mode getMode() {
		return this.mode;
	}

	/**
	 * Digi Laun の起動モードを設定します。
	 * @param value Digi Laun の起動モード
	 */
	public void setMode(Mode value) {
		this.mode = value;
	}

	/**
	 * 作品コレクションを取得します。
	 * @return 作品コレクション
	 * @see WorkList
	 */
	public synchronized WorkList getWorks() {
		if(this.works == null)
			this.works = new WorkList();
		return this.works;
	}

	/**
	 * 既定の XML 設定ファイルから設定を読み込みます。
	 * @throws ParserConfigurationException 内部例外？
	 * @throws SAXException 内部例外？
	 * @throws IOException 入力例外
	 */
	public void readFromXMLDocument() throws ParserConfigurationException, SAXException, IOException {
		this.readFromXMLDocument(DEFAULT_INI_PATH);
	}

	/**
	 * XML 設定ファイルから設定を読み込みます。
	 * @param filename 読み込み元ファイル名
	 * @throws ParserConfigurationException 内部例外？
	 * @throws SAXException 内部例外？
	 * @throws IOException 入力例外
	 */
	public void readFromXMLDocument(String filename) throws
	ParserConfigurationException, SAXException, IOException {
		// 準備
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(filename);
		Element root = doc.getDocumentElement();
		if(root.getNodeName() != Config.XML_ELEM_ROOT)
			throw new RuntimeException("Digi Laun 環境設定 XML ではありません。");

		// 各種ノード処理
		for(Node n = root.getFirstChild();
				n != null; n = n.getNextSibling()) {
			if(n.getNodeType() == Node.ELEMENT_NODE &&
					n.getNodeName() == Config.XML_ELEM_SETTINGS)
				this.setSettingsByXMLElement((Element)n);
			if(n.getNodeType() == Node.ELEMENT_NODE &&
					n.getNodeName() == Config.XML_ELEM_WORKS)
				(this.works = new WorkList()).addFromXMLElements((Element)n);
			// 以下旧バージョン用
			if(n.getNodeType() == Node.ELEMENT_NODE &&
					n.getNodeName() == Work.XML_ELEMENT_WORK) {
				(this.works = new WorkList()).addFromXMLElements(root);
				break;
			}
		}
	}

	/**
	 * 既定の XML 設定ファイルへ設定を書き込みます。
	 * @throws ParserConfigurationException 内部例外？
	 * @throws TransformerException 書き込み例外等
	 */
	public void WriteToXMLDocument() throws
	ParserConfigurationException, TransformerException {
		this.WriteToXMLDocument(DEFAULT_INI_PATH);
	}

	/**
	 * XML 設定ファイルへ設定を書き込みます。
	 * @param filename 書き込み先ファイル名
	 * @throws ParserConfigurationException 内部例外？
	 * @throws TransformerException 書き込み例外等
	 */
	public void WriteToXMLDocument(String filename) throws
	ParserConfigurationException, TransformerException {
		// 準備
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.newDocument();

		// ルート要素を作る
		Element elmRoot = document.createElement(XML_ELEM_ROOT);
		document.appendChild(elmRoot);

		// 設定要素の追加
		elmRoot.appendChild(this.getSettingsAsXMLElement(document));

		// 作品要素の追加
		elmRoot.appendChild(this.works.createXMLElement(document));

		// ファイルへ書き込み
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();

		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.ENCODING, OUTPUT_ENCODING);

		t.transform(new DOMSource(document), new StreamResult(filename));
	}

	/**
	 * このオブジェクトの設定を XML 要素として取得します。
	 * @param document ドキュメント
	 * @return このオブジェクトの設定を保持した XML 要素
	 * @see Config#XML_ELEM_SETTINGS
	 */
	private Element getSettingsAsXMLElement(Document document) {
		final Element elmSettigs = document.createElement(XML_ELEM_SETTINGS);

		{
			final Element elmMode = document.createElement(XML_ELEM_MODE);
			elmMode.setAttribute(XML_ATTR_MODE_VALUE, this.mode.toString());
			elmSettigs.appendChild(elmMode);
		}

		return elmSettigs;
	}

	/**
	 * XML の設定要素をもとに、このオブジェクトを設定します。
	 * @param elmSettings 設定を保持した XML 要素
	 * @see Config#XML_ELEM_SETTINGS
	 */
	private void setSettingsByXMLElement(Element elmSettings) {
		Node node = elmSettings.getFirstChild();
		while((node = node.getNextSibling()) != null) {
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			final Element element = (Element)node;

			// モード要素
			if(element.getNodeName() == XML_ELEM_MODE)
				this.mode = Mode.valueOf(element.getAttribute(XML_ATTR_MODE_VALUE));
		}
	}
}
