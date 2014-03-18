package net.digicre.digilaun.work;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 作品のエンティティクラスです。
 * @author p10090
 *
 */
public class Work implements Comparable<Work> {
	/**
	 * 作品オブジェクトを年度で比較する機能を提供します。
	 * @author p10090
	 */
    public static class YearComparator implements Comparator<Work> {
    	/**
    	 * 二つの作品オブジェクトを比較します。
    	 * @param workA 比較対象 A
    	 * @param workB 比較対象 B
    	 * @return A が B より新しければ負の数、同じなら作品名の順
    	 */
    	@Override
    	public int compare(Work workA, Work workB) {
    		// 制作年度が違えば、新しい方が先
    		if (workA.year != workB.year)
    			return workB.year - workA.year;
    		// 制作年度が同じなら名前順
    		else if(workB.name == null)
    			return workA.name == null ? 0 : -1;
    		else if(workA.name == null)
    			return +1;
    		else
    			return workA.name.compareTo(workB.name);
    	}

    	/**
    	 * ほかのオブジェクトがこのコンパレータと「等しい」かどうかを示します。
    	 * @param obj 比較対象の参照オブジェクト
    	 * @return 指定されたオブジェクトもコンパレータであり、それがこのコンパレータと同じ順序付けを行う場合にだけ <code>true</code>
    	 */
    	@Override
    	public boolean equals(Object obj) {
    		return obj instanceof YearComparator;
    	}
    }

	/**
	 * XML ファイルにおける、ルートタグの名前です。
	 */
	public static final String XML_ELEMENT_ROOT = "DigiLaun";

	/**
	 * XML ファイルにおける、作品タグの名前です。
	 */
	public static final String XML_ELEMENT_WORK = "work";

	/**
	 * XML ファイルにおける、引数タグの名前です。
	 */
	public static final String XML_ELEMENT_ARG = "argument";

	/**
	 * XML ファイルにおける、作品名属性の名前です。
	 */
	public static final String XML_ATTR_NAME = "name";

	/**
	 * XML ファイルにおける、制作年属性の名前です。
	 */
	public static final String XML_ATTR_YEAR = "year";

	/**
	 * XML ファイルにおける、パス属性の名前です。
	 */
	public static final String XML_ATTR_PATH = "path";

	/**
	 * XML ファイルにおける、引数属性の名前です。
	 */
	public static final String XML_ATTR_ARG = "args";

	/**
	 * XML ファイルにおける、アイコン属性の名前です。
	 */
	public static final String XML_ATTR_ICON = "pathIcon";

	/**
	 * XML ファイルにおける、絵属性の名前です。
	 */
	public static final String XML_ATTR_PICT = "pathPicture";

	/**
	 * XML ファイルにおける、テキスト属性の名前です。
	 */
	public static final String XML_ATTR_INFO = "pathInfo";

	/**
	 * XML ファイルにおける、入力属性の名前です。
	 */
	public static final String XML_ATTR_IDEV = "inputDevice";

	/**
	 * XML ファイルにおける、コマンドライン引数要素の値属性の名前です。
	 */
	public static final String XML_ATTR_ARG_V = "value";

    /**
	 * 作品タイトルです。
	 */
    String name;
    /**
	 * 開くファイルの相対パスです。
	 */
    File launchedFile;
    /**
	 * コマンドライン引数です。
	 */
    String[] args;
    /**
	 * タイトル画像の相対パスです。
	 */
    File summaryImageFile;
    /**
	 * アイコン画像の相対パスです。
	 */
    File iconFile;
    /**
	 * クレジットの相対パスです。
	 */
    File detailTextFile;
    /**
	 * 入力デバイスです。
	 */
    String inputDeviceName;
    /**
	 * リリース年度です。
	 */
    int year;

    Work() {}

    /**
     * 指定された作品オブジェクトのコピーを作成します。
     * コマンドライン引数の配列まで深くコピーします。
     * @param that コピー元の作品オブジェクト
     */
    public Work(Work that) {
    	this.year             = that.year;
    	this.name             = that.name;
    	this.launchedFile             = that.launchedFile;
    	this.args             = Arrays.copyOf(that.args, that.args.length);
    	this.summaryImageFile = that.summaryImageFile;
    	this.detailTextFile   = that.detailTextFile;
    	this.iconFile         = that.iconFile;
    	this.inputDeviceName  = that.inputDeviceName;
    }

	/**
     * 作品名を取得します。
     * @return 作品名
     */
    public String getName() {
    	return name;
    }

    /**
     * 作品を開くパスを取得します。
     * @return 作品を開くパス
     */
    public File getLaunchedFile() {
    	return new File(launchedFile.getPath());
    }

    /**
     * 作品へのコマンドライン引数を取得します。
     * コマンドライン引数は、作品がアプリケーションであるときに使用します。
     * @return 作品へのコマンドライン引数
     */
    public String[] getArgs() {
    	return Arrays.copyOf(args, args.length);
    }

    /**
     * 作品ボタンに使用するアイコン画像へのパスを取得します。
     * @return 作品ボタンに使用するアイコン画像へのパス
     */
    public File getIconFile() {
    	return new File(iconFile.getPath());
    }

    /**
     * 作品を説明する画像へのパスを取得します。
     * @return 作品を説明する画像へのパス
     */
    public File getSummaryImageFile() {
    	return new File(summaryImageFile.getPath());
    }

    /**
     * 作品の情報テキストファイルへのパスを取得します。
     * @return 作品の情報テキストファイルへのパス
     */
    public File getDetailTextFile() {
    	return new File(detailTextFile.getPath());
    }

    /**
     * 作品の入力デバイスの種類をあらわす文字列を取得します。
     * @return 作品の入力デバイスの種類
     */
    public String getInputDeviceName() {
    	return inputDeviceName;
    }

    /**
     * 作品の制作年度を取得します。
     * @return 作品の制作年度
     */
    public int getYear() {
    	return year;
    }

    /**
	 * XML 要素の属性リストから、作品データを読み取ります。
	 * @deprecated
	 * このメソッドは、 Digi Laun バージョン 2.x との互換性のために残されています。
	 * @param atts SAX 属性リスト
	 */
	public void setPropertiesByXMLAtts(org.xml.sax.Attributes atts) {
		this.year = Integer.parseInt(atts.getValue("year"));
		final String path = getPropertyFromXMLAttr(atts,
				/* 旧名 */ "path");
		final String arg = getPropertyFromXMLAttr(atts,
				/* 旧名 */ "args");
		final String iconPath = getPropertyFromXMLAttr(atts,
				/* 旧名 */ "icon");
		final String summaryImagePath = getPropertyFromXMLAttr(atts,
				/* 旧名 */ "pict");
		final String detailTextPath = getPropertyFromXMLAttr(atts,
				/* 旧名 */ "info", "crdt", "copy");
		this.inputDeviceName = getPropertyFromXMLAttr(atts,
				/* 旧名 */ "idev");
		this.launchedFile     =             path == null ? null : new File(path);
		this.iconFile         =         iconPath == null ? null : new File(iconPath);
		this.summaryImageFile = summaryImagePath == null ? null : new File(summaryImagePath);
		this.detailTextFile   =   detailTextPath == null ? null : new File(detailTextPath);
		this.args = arg == null || arg.isEmpty() ?
				new String[0] : new String[]{arg};
	}

	/**
	 * XML 要素の属性リストから、指定した属性の値を取得します。
	 * @deprecated
	 * このメソッドは、 Digi Laun バージョン 2.x との互換性のために残されています。
	 * @param atts SAX 属性リスト 
	 * @param attNames 取得する属性 (優先順)
	 * @return 要素の属性値、属性がなければ <code>null</code>
	 */
	private static String getPropertyFromXMLAttr(org.xml.sax.Attributes atts,
			String... attNames) {
		String value;

		for(String att : attNames)
			if((value = atts.getValue(att)) != null)
				return value;
		return null;
	}

	/**
	 * XML 要素から、指定した属性の値を取得します。
	 * @param element DOM 要素
	 * @param attNames 取得する属性 (優先順)
	 * @return 要素の属性値、属性がなければ <code>null</code>
	 */
	private static String getPropertyFromXMLAttr
	(org.w3c.dom.Element element, String... attNames) {
		for(String att : attNames)
			if(att != null && element.getAttributeNode(att) != null)
				return element.getAttributeNode(att).getValue();
		return null;
	}

	/**
	 * XML 要素から、指定した属性の値を {@link File} 型で取得します。
	 * @param element DOM 要素
	 * @param attNames 取得する属性 (優先順)
	 * @return 要素の属性値をパスとしたファイル、属性がなければ <code>null</code>
	 */
	private static File getFileFromXMLAttr
	(org.w3c.dom.Element element, String... attNames) {
		final String path = getPropertyFromXMLAttr(element, attNames);

		return path != null ? new File(path) : null;
	}

    /**
     * この作品オブジェクトのハッシュコードを返します。
     * @return この作品オブジェクトのハッシュコード
     */
    @Override
    public int hashCode() {
    	return this.launchedFile.hashCode();
    }

    /**
     * このオブジェクトと指定されたオブジェクトの順序を比較します。
     * <br>作品オブジェクトのパス文字列同士を比較し、その結果を返します。
     * パスが同じなら、コマンドライン引数の配列を比較し、その結果を返します。
     * @param that 比較対象のオブジェクト
     * @return このオブジェクトの方が先なら負の数、あのオブジェクトの方が先なら正の数
     */
    @Override
    public int compareTo(Work that) {
    	synchronized(this) { synchronized(that) {
	    	// パス順
	    	if(!this.launchedFile.equals(that.launchedFile))
	    		return this.launchedFile.compareTo(that.launchedFile);
	    	// パスが同じなら引数順
	    	if(this.args == null)
	    		return that.args != null && that.args.length > 0 ? -1 : 0;
			else if(that.args == null)
				return this.args != null && this.args.length > 0 ? +1 : 0;
	    	for(int i = 0; i < this.args.length; ++i)
	    		if(i >= that.args.length)
	    			return -1;
	    		else if(!this.args[i].equals(that.args[i]))
	    			return this.args[i].compareTo(that.args[i]);
	    	return that.args.length - this.args.length;
    	}}
    }

    /**
     * この作品オブジェクトと別のオブジェクトが等しいかどうかを判定します。
     * 同じパス、同じコマンドライン引数を持っていれば等価と見なします。
     * @param obj 比較対象のオブジェクト
     * @return このオブジェクトと obj が等価なら真
     */
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof Work))
    		return false;
    	
    	final Work that = (Work)obj;
    	return	this.launchedFile.equals(that.launchedFile) &&
    			Arrays.equals(this.args, that.args);
    }

    /**
     * DOM 要素を元に、新しい作品オブジェクトを作成します。
     * @param element DOM 要素
     * @return 新しい作品オブジェクト
     */
	static Work createFromXMLElement(org.w3c.dom.Element element) {
		final Work it = new Work();
		final LinkedList<String> args = new LinkedList<String>();

		// 属性を読み込み
		// 制作年度
		it.year = Integer.parseInt(element.getAttribute(XML_ATTR_YEAR));
		// 名前
		it.name = getPropertyFromXMLAttr(element, XML_ATTR_NAME,
				/* 旧名 */ null);
		// 開くパス
		it.launchedFile = getFileFromXMLAttr(element,
				/* 旧名 */ "path");
		// コマンドライン引数
		args.add(getPropertyFromXMLAttr(element,
				/* 旧名 */ "args"));
		// アイコンパス
		it.iconFile = getFileFromXMLAttr(element, XML_ATTR_ICON,
				/* 旧名 */ "icon");
		// 概要パス
		it.summaryImageFile = getFileFromXMLAttr(element, XML_ATTR_PICT,
				/* 旧名 */ "pict");
		// 詳細パス
		it.detailTextFile = getFileFromXMLAttr(element, XML_ATTR_INFO,
				/* 旧名 */ "info", "crdt", "copy");
		// 入力デバイス名
		it.inputDeviceName = getPropertyFromXMLAttr(element, XML_ATTR_IDEV,
				/* 旧名 */ "idev");

		// 小要素を読み込み
		for(org.w3c.dom.Node n = element.getFirstChild();
				n != null; n = n.getNextSibling()) {
			// コマンドライン引数
			if(n.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE &&
					n.getNodeName() == XML_ELEMENT_ARG)
				args.add(((org.w3c.dom.Element)n).
						getAttribute(XML_ATTR_ARG_V));
			// 開くパス (属性から取得済みなら名前)
			if(n.getNodeType() == org.w3c.dom.Node.CDATA_SECTION_NODE) {
				if(it.launchedFile != null)
					it.name = ((org.w3c.dom.CDATASection)n).getData();
				else
					it.launchedFile =
					new File(((org.w3c.dom.CDATASection)n).getData());
			}
		}
		// コマンドライン引数から null を削除
		for(Iterator<String> i = args.iterator(); i.hasNext();)
			if(i.next() == null)
				i.remove();

		it.args = args.toArray(new String[args.size()]);

		return it;
	}

	/**
	 * 新しい DOM 要素に、この作品オブジェクトのデータを保存します。 
	 * @param document DOM ドキュメント
	 * @return この作品オブジェクトのデータを保持した新しい DOM 要素
	 */
	org.w3c.dom.Element saveToXMLElement(org.w3c.dom.Document document) {
		final org.w3c.dom.Element elmWork =
				document.createElement(XML_ELEMENT_WORK);

		// 各種属性
		elmWork.setAttribute(XML_ATTR_YEAR, Integer.toString(this.year));
		elmWork.setAttribute(XML_ATTR_NAME,                  this.name);
		elmWork.setAttribute(XML_ATTR_IDEV,                  this.inputDeviceName);
		elmWork.setAttribute(XML_ATTR_PATH,                  this.launchedFile.getPath());
		elmWork.setAttribute(XML_ATTR_ICON,                  this.iconFile.getPath());
		elmWork.setAttribute(XML_ATTR_PICT,                  this.summaryImageFile.getPath());
		elmWork.setAttribute(XML_ATTR_INFO,                  this.detailTextFile.getPath());

		// コマンドライン引数
		for(String argv : this.args) {
			if(argv == null)
				continue;

			org.w3c.dom.Element elmArgv =
					document.createElement(XML_ELEMENT_ARG);
			elmArgv.setAttribute(XML_ATTR_ARG_V, argv);

			elmWork.appendChild(elmArgv);
		}

		// 終了
		return elmWork;
	}
}
