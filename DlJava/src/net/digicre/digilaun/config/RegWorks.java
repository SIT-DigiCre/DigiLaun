package net.digicre.digilaun.config;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.digicre.digilaun.Config;
import net.digicre.digilaun.config.regworks.WorkTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.io.IOException;

import net.digicre.digilaun.config.regworks.RegisterPanel;
import net.digicre.digilaun.work.WorkList;

import javax.swing.JSplitPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import net.digicre.digilaun.config.regworks.WorkTable;

@SuppressWarnings("serial")
public class RegWorks extends JDialog {
	private WorkTable workTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Config config = new Config();
		try {
			config.readFromXMLDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			RegWorks dialog = new RegWorks(config.getWorks());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ })
	public RegWorks() {
		setTitle("作品データベース");
		setBounds(100, 100, 800, 646);
		getContentPane().setLayout(new BorderLayout());
		{
			JSplitPane contentSplitPane = new JSplitPane();
			getContentPane().add(contentSplitPane, BorderLayout.CENTER);
			{
				JPanel panel = new JPanel();
				contentSplitPane.setLeftComponent(panel);
				panel.setLayout(new BorderLayout(0, 0));
				{
					JPanel panel_1 = new JPanel();
					panel.add(panel_1, BorderLayout.SOUTH);
					panel_1.setLayout(new BorderLayout(0, 5));
					{
						JButton btnr = new JButton("登録(R)");
						btnr.setFont(btnr.getFont().deriveFont(btnr.getFont().getSize() + 12f));
						btnr.setMnemonic('R');
						panel_1.add(btnr);
					}
					{
						JButton btno = new JButton("上書き(O)");
						btno.setMnemonic('O');
						panel_1.add(btno, BorderLayout.SOUTH);
					}
				}
				{
					JScrollPane scrollPane_1 = new JScrollPane();
					panel.add(scrollPane_1, BorderLayout.CENTER);
					{
						RegisterPanel registerPanel = new RegisterPanel();
						registerPanel.setPreferredSize(new Dimension(10, 10));
						scrollPane_1.setViewportView(registerPanel);
					}
				}
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				contentSplitPane.setRightComponent(scrollPane);
				{
					workTable = new WorkTable((WorkList) null);
					workTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					workTable.setModel(new WorkTableModel());
					scrollPane.setViewportView(workTable);
				}
			}
			contentSplitPane.setDividerLocation(320);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			{
				JButton okButton = new JButton("OK");
				buttonPane.add(okButton);
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("キャンセル");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
			}
		}
	}

	public RegWorks(WorkList works) {
		this();
		WorkTableModel tm = (WorkTableModel)this.workTable.getModel();
		//tm.setWorkList(works.getWritableCopy());
	}

	static void open() {
		try {
			RegWorks dialog = new RegWorks();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
