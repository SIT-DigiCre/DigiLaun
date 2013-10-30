package net.digicre.digilaun.config;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import net.digicre.digilaun.config.regworks.*;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

@SuppressWarnings("serial")
public class RegWorks extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable worksTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegWorks dialog = new RegWorks();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RegWorks() {
		setBounds(100, 100, 800, 646);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.WEST);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, BorderLayout.CENTER);
				scrollPane.setPreferredSize(new Dimension(353, 23));
				{
					RegisterPanel registerPanel = new RegisterPanel();
					GridBagLayout gridBagLayout = (GridBagLayout) registerPanel.getLayout();
					gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
					gridBagLayout.rowHeights = new int[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15};
					gridBagLayout.columnWeights = new double[]{1.0, 0.0};
					gridBagLayout.columnWidths = new int[]{450, 0};
					scrollPane.setViewportView(registerPanel);
				}
			}
			{
				JPanel panel_2 = new JPanel();
				panel.add(panel_2, BorderLayout.SOUTH);
				panel_2.setLayout(new BorderLayout(0, 0));
				{
					JButton btnr = new JButton("登録(R)");
					btnr.setFont(btnr.getFont().deriveFont(23f));
					btnr.setMnemonic('R');
					panel_2.add(btnr, BorderLayout.NORTH);
				}
				{
					JPanel panel_1 = new JPanel();
					panel_2.add(panel_1, BorderLayout.SOUTH);
					{
						JButton btno = new JButton("上書き(O)");
						btno.setMnemonic('O');
						panel_1.add(btno);
					}
				}
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				worksTable = new JTable();
				worksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				worksTable.setModel(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"\u540D\u524D", "\u5236\u4F5C\u5E74\u5EA6", "\u30D1\u30B9", "\u30B3\u30DE\u30F3\u30C9\u30E9\u30A4\u30F3\u5F15\u6570", "\u5165\u529B", "\u30A2\u30A4\u30B3\u30F3", "\u6982\u8981\u30A4\u30E1\u30FC\u30B8", "\u8A73\u7D30\u30C6\u30AD\u30B9\u30C8"
					}
				) {
					Class[] columnTypes = new Class[] {
						String.class, Short.class, String.class, String.class, String.class, String.class, String.class, String.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				worksTable.getColumnModel().getColumn(0).setPreferredWidth(150);
				worksTable.getColumnModel().getColumn(2).setPreferredWidth(300);
				worksTable.getColumnModel().getColumn(4).setPreferredWidth(150);
				worksTable.getColumnModel().getColumn(5).setPreferredWidth(300);
				worksTable.getColumnModel().getColumn(6).setPreferredWidth(300);
				worksTable.getColumnModel().getColumn(7).setPreferredWidth(300);
				scrollPane.setViewportView(worksTable);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				buttonPane.add(panel);
				{
					JButton btnr_1 = new JButton("登録(R)");
					panel.add(btnr_1);
				}
			}
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				buttonPane.add(panel);
				{
					JButton okButton = new JButton("OK");
					panel.add(okButton);
					okButton.setActionCommand("OK");
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Cancel");
					panel.add(cancelButton);
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
				}
			}
		}
	}
}
