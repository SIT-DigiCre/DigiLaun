package net.digicre.digilaun.work;

import org.xml.sax.Attributes;

/**
 * 作品のデータクラスです。
 * @author p10090
 *
 */
public class Work implements Comparable<Work> {
	/**
	 * XML ファイルにおける、ルートタグの名前です。
	 */
	public static final String XML_ELEMENT_ROOT = "DigiLaun";
	
	/**
	 * XML ファイルにおける、作品タグの名前です。
	 */
	public static final String XML_ELEMENT_WORK = "work";
	
	/**
	 * XML ファイルにおける、制作年属性の名前です。
	 */
	public static final String XML_ATTRIBUTE_YEAR = "year";
	
	/**
	 * XML ファイルにおける、パス属性の名前です。
	 */
	public static final String XML_ATTRIBUTE_PATH = "path";
	
	/**
	 * XML ファイルにおける、引数属性の名前です。
	 */
	public static final String XML_ATTRIBUTE_ARGS = "args";
	
	/**
	 * XML ファイルにおける、アイコン属性の名前です。
	 */
	public static final String XML_ATTRIBUTE_ICON = "pathIcon";
	
	/**
	 * XML ファイルにおける、絵属性の名前です。
	 */
	public static final String XML_ATTRIBUTE_PICT = "pathPicture";
	
	/**
	 * XML ファイルにおける、テキスト属性の名前です。
	 */
	public static final String XML_ATTRIBUTE_INFO = "pathInfo";
	
	/**
	 * XML ファイルにおける、入力属性の名前です。
	 */
	public static final String XML_ATTRIBUTE_IDEV = "inputDevice";

	/** 最新バージョンの DTD */
    private static String latestDTD = null;
    /** 作品タイトル */
    private String name;
    /** 開くファイルの相対パス */
    private String path;
    /** コマンドライン引数 */
    private String args;
    /** タイトル画像の相対パス */
    private String pict;
    /** アイコン画像の相対パス */
    private String icon;
    /** クレジットの相対パス */
    private String copy;
    /** 入力デバイス */
    private String idev;
    /** リリース年度 */
    private short year;

    /**
     * 最新バージョンの XML データの DTD を取得します。
     * @return 出力する XML ファイルの DTD
     */
    public static String getLatestDTD() {
        // 要求に応じて文字列オブジェクトを作成する
        if (latestDTD == null)
            latestDTD =
            "<!ELEMENT "+XML_ELEMENT_ROOT+
            " ("+XML_ELEMENT_WORK+"*)>" +
            "<!ELEMENT "+XML_ELEMENT_WORK+" (#PCDATA)>" +
            "<!ATTLIST "+XML_ELEMENT_WORK+" " +
            XML_ATTRIBUTE_YEAR+" CDATA #REQUIRED " +
            XML_ATTRIBUTE_PATH+" CDATA #REQUIRED " +
            XML_ATTRIBUTE_ARGS+" CDATA '' " +
            XML_ATTRIBUTE_ICON+" CDATA '' " +
            XML_ATTRIBUTE_PICT+" CDATA '' " +
            XML_ATTRIBUTE_INFO+" CDATA '' " +
            XML_ATTRIBUTE_IDEV+" CDATA ''>";
        return latestDTD;
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
    public String getPath() {
    	return path;
    }
    
    /**
     * 作品へのコマンドライン引数を取得します。
     * コマンドライン引数は、作品がアプリケーションであるときに使用します。
     * @return 作品へのコマンドライン引数
     */
    public String getArguments() {
    	return args;
    }
    
    /**
     * 作品を説明する画像へのパスを取得します。
     * @return 作品を説明する画像へのパス
     */
    public String getPicturePath() {
    	return pict;
    }
    
    /**
     * 作品ボタンに使用するアイコン画像へのパスを取得します。
     * @return 作品ボタンに使用するアイコン画像へのパス
     */
    public String getIconPath() {
    	return icon;
    }
    
    /**
     * 作品の情報テキストファイルへのパスを取得します。
     * @return 作品の情報テキストファイルへのパス
     */
    public String getTextPath() {
    	return copy;
    }
    
    /**
     * 作品の入力デバイスの種類をあらわす文字列を取得します。
     * @return 作品の入力デバイスの種類
     */
    public String getInputDeviceName() {
    	return idev;
    }
    
    /**
     * 作品の制作年度を取得します。
     * @return 作品の制作年度
     */
    public short getYear() {
    	return year;
    }

    /**
     * このオブジェクトと指定されたオブジェクトの順序を比較します。
     * @see Comparable#compareTo(Object)
     */
	@Override
	public int compareTo(Work that) {
		// 制作年度が違えば、新しい方が先
		if (this.year != that.year)
			return that.year - this.year;
		// 制作年度が同じなら名前順
		else
			return this.name.compareTo(that.name);
	}

	/**
	 * XML 要素の属性リストから、作品データを読み取ります。
	 * @param atts 属性リスト
	 */
	public void setPropertiesByXMLAtts(Attributes atts) {
		year = Short.parseShort(atts.getValue(XML_ATTRIBUTE_YEAR));
		path = getPropertyFromXMLAttr(atts, XML_ATTRIBUTE_PATH);
		args = getPropertyFromXMLAttr(atts, XML_ATTRIBUTE_ARGS);
		icon = getPropertyFromXMLAttr(atts, XML_ATTRIBUTE_ICON,
				"icon");
		pict = getPropertyFromXMLAttr(atts, XML_ATTRIBUTE_PICT,
				"pict");
		copy = getPropertyFromXMLAttr(atts, XML_ATTRIBUTE_INFO,
				"info", "crdt", "copy");
		idev = getPropertyFromXMLAttr(atts, XML_ATTRIBUTE_IDEV,
				"idev");
	}

	/**
	 * XML 要素の属性リストから、指定した属性の値を取得します。
	 * @param atts 属性リスト 
	 * @param attNames 取得する属性 (優先順)
	 * @return 要素の属性値、属性がなければ <code>null</code>
	 */
	private String getPropertyFromXMLAttr(Attributes atts, String... attNames) {
		for(String att : attNames)
			if(atts.getType(att) != null)
				return atts.getValue(att);
		return null;
	}

	/**
	 * 作品名を設定します。
	 * SAX を使って XML からデータを読み込むときに呼び出されます。
	 * @param valueOf 設定する名前
	 */
	void setName(String valueOf) {
		name = valueOf.replaceAll("^?\\s+$?", "");
	}
}
