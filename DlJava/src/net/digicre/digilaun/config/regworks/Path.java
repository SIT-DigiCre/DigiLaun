package net.digicre.digilaun.config.regworks;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 相対パス構築機能などを提供します。
 * @author p10090
 *
 */
public class Path {
	private LinkedList<String> pathList;

	private Path() {
		this.pathList = new LinkedList<String>();
	}

	/**
	 * 新しい <code>Path</code> を構築します。
	 * @param pathStr パス文字列
	 */
	public Path(String pathStr) {
		this.pathList = separatePath(pathStr);
	}

	/**
	 * 既存の <code>Path</code> と同じパスを示す、新しい
	 * <code>Path</code> を構築します。
	 * @param that 元の <code>Path</code>
	 */
	public Path(Path that) {
		this.pathList = new LinkedList<String>(that.pathList);
	}

	/**
	 * パス文字列を分割して <code>{@link LinkedList}</code> を作成します。
	 * @param path パス文字列
	 * @return <code>path</code> のリスト表現
	 */
	private static LinkedList<String> separatePath(String path) {
		final String splitRegEx = File.separatorChar == '\\' ? "\\\\" :
			String.format("(?!(?:\\\\\\\\)*+)\\u%04X",
					new Integer(File.separator));
		LinkedList<String> result = new LinkedList<String>();

		for(String filename : path.split(splitRegEx)) {
			if(filename.equals("."))
				continue;
			if(filename.equals("..")) {
				result.removeLast();
				continue;
			}
			result.addLast(filename);
		}

		return result;
	}

	/**
	 * 既存の <code>Path</code> と同じパスを示す、新しい
	 * <code>Path</code> を構築します。
	 * @return この <code>Path</code> と同じパスを示す、新しい
	 * <code>Path</code>
	 */
	public Path clone() {
		return new Path(this);
	}

	/**
	 * この <code>Path</code> を基準とする相対パスを取得します。
	 * @param that 相対パスを取得したいパス
	 * @return <code>that</code> の相対パス
	 */
	public Path relativize(Path that) {
		// 既に相対パスならそのまま返す
		if(!that.isAbsolute())
			return that;
		// このパスが相対なら絶対パスを作成して相対化
		if(!this.isAbsolute()) {
			Path absolutePath = new Path(
					new File(this.toString()).getAbsolutePath());
			
			return absolutePath.relativize(that);
		}
		// ルートディレクトリが違えばそのまま返す
		if(!this.pathList.getFirst().equals(that.pathList.getFirst()))
			return that;
		// ルートディレクトリの次が違えば "/" で始まるパスを返す
		if(!this.pathList.get(1).equals(that.pathList.get(1))) {
			Path it = new Path(that);

			it.pathList.set(0, "");
			return it;
		}

		// 相対パス (結果)
		Path relativePath = new Path();

		// 比較
		ListIterator<String> i = that.pathList.listIterator();
		ListIterator<String> j = this.pathList.listIterator();
		while(i.hasNext() && j.hasNext()) {
			if(!i.next().equals(j.next())) {
				i.previous();
				j.previous();
				break;
			}
		}

		// 構築
		while(j.hasNext()) {
			j.next();
			relativePath.pathList.addLast("..");
		}
		while(i.hasNext()) {
			relativePath.pathList.addLast(i.next());
		}

		return relativePath;
	}

	/**
	 * この <code>Path</code> が絶対かどうかを返します。
	 * @return この <code>Path</code> が絶対なら真
	 */
	public boolean isAbsolute() {
		return new File(this.toString()).isAbsolute();
	}

	/**
	 * @return この <code>Path</code> の文字列表現
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for(String filename : this.pathList) {
			if(first)
				first = false;
			else
				sb.append(File.separatorChar);
			sb.append(filename);
		}

		return sb.toString();
	}
}
