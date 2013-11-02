package net.digicre.digilaun;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JToggleButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;

import net.digicre.digilaun.work.Work;

import java.awt.GridLayout;
import java.util.Calendar;
import java.util.ListIterator;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * 制作年度ごとの作品ボタンをまとめたパネルです。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
public class YearWorksPanel extends JPanel {
	/**
	 * 最新作である時のラベルテキストです。
	 */
	static final String NEWEST_TEXT = "最新作";

	/**
	 * 昨年度の作品である時のラベルテキストです。
	 */
	static final String LASTYEAR_TEXT = "昨年度の作品";

	/**
	 * 過去作である時のラベルテキストです。
	 */
	static final String OLDER_TEXT = "%d年度の作品";

	/**
	 * 現在年を保持したカレンダーです。
	 */
	private static Calendar calendar = null;

	/**
	 * このパネルに入る作品の制作年度です。
	 */
	private int year;

	private JPanel buttonsPanel;
	private JLabel yearLabel;
	private JToggleButton expandToggleButton;
	private JPanel labelPanel;
	private JSeparator separator;
	private JPanel panel;
	private JLabel label;
	private JLabel label_1;

	/**
	 * Create the panel.
	 */
	private YearWorksPanel() {
		setLayout(new BorderLayout(0, 0));

		labelPanel = new JPanel();
		FlowLayout fl_labelPanel = (FlowLayout) labelPanel.getLayout();
		fl_labelPanel.setAlignment(FlowLayout.LEADING);
		add(labelPanel, BorderLayout.NORTH);

		expandToggleButton = new JToggleButton("");
		expandToggleButton.setSelectedIcon(new ImageIcon("img/CollapseButton.png"));
		expandToggleButton.setIcon(new ImageIcon("img/ExpandButton.png"));
		expandToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				YearWorksPanel.this.updateExpandStatus();
			}
		});
		expandToggleButton.setPreferredSize(new Dimension(70, 70));
		labelPanel.add(expandToggleButton);

		yearLabel = new JLabel("n年度の作品");
		yearLabel.setIcon(null);
		yearLabel.setFont(yearLabel.getFont().deriveFont(48f));
		labelPanel.add(yearLabel);

		buttonsPanel = new JPanel();
		add(buttonsPanel, BorderLayout.CENTER);
		buttonsPanel.setLayout(new GridLayout(0, 1, 5, 5));
		
		panel = new JPanel();
		add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		separator = new JSeparator();
		panel.add(separator);
		separator.setOrientation(SwingConstants.VERTICAL);
		
		label = new JLabel("");
		label.setPreferredSize(new Dimension(5, 0));
		label.setMinimumSize(new Dimension(5, 0));
		panel.add(label, BorderLayout.WEST);
		
		label_1 = new JLabel("");
		label_1.setPreferredSize(new Dimension(5, 0));
		label_1.setMinimumSize(new Dimension(5, 0));
		panel.add(label_1, BorderLayout.EAST);

	}

	/**
	 * 作品ボタンを登録済みの、新しい <code>YearWorksPanel</code>
	 * を作成します。
	 * @param workIterator 登録する作品リストのイテレーター
	 * (処理中に別年度作品の直前か終端まで進めます)
	 */
	YearWorksPanel(ListIterator<Work> workIterator) {
		this();
		synchronized(workIterator) {
			// イテレーターに「前」があるか（最初でないか）どうか
			boolean newest = !workIterator.hasPrevious();
			// イテレーターが指している作品
			Work work = workIterator.next();
			// このパネルが扱う作品の制作年度
			year = work.getYear();

			// ラベルを設定
			{
				String iconPath;

				getCalendarAsNeed();
				if(newest) {
					iconPath = "img/LabelBackNewest.png";
					this.yearLabel.setText(NEWEST_TEXT);
				}
				else {
					iconPath = "img/LabelBackOlder.png";
					this.yearLabel.setText(
							calendar.get(Calendar.YEAR) == work.getYear() ?
									LASTYEAR_TEXT :
									String.format(
											OLDER_TEXT, work.getYear()));
				}
				this.yearLabel.setIcon(new ImageIcon(iconPath));
			}

			// 同じ年度のボタンを列挙
			while(work != null) {
				this.buttonsPanel.add(new WorkButton(work));
				if(!workIterator.hasNext())
					break;
				work = workIterator.next();
				if(work.getYear() != year) {
					workIterator.previous();
					break;
				}
			}

			// エクスパンド状態を初期化
			this.expandToggleButton.setSelected(newest);
			this.updateExpandStatus();
		}
	}

	/**
	 * 現在<b>年度</b>を保持したカレンダーをフィールドに取得します。
	 * すでにあれば、それを返します。
	 */
	synchronized private static void getCalendarAsNeed() {
		if(calendar == null) {
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);
		}
	}

	/**
	 * このパネルに入る作品の制作年度を取得します。
	 * @return このパネルに入る作品の制作年度
	 */
	int getYear() {
		return this.year;
	}

	/**
	 * 引き出しボタンのトグル状態に応じて子コンポーネントを更新します。
	 */
	private synchronized void updateExpandStatus() {
		this.buttonsPanel.setVisible(this.expandToggleButton.isSelected());
	}
}
