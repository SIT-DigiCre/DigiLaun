package net.digicre.digilaun;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.GridBagLayout;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JScrollPane indexScrollPane;
	private JLabel headLabel;
	private JPanel indexPanel;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainFrame frame = new MainFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Digi Laun");

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		headLabel = new JLabel((String) null);
		headLabel.setIcon(new ImageIcon("img/Logo.png"));
		headLabel.setPreferredSize(new Dimension(91, 70));
		headLabel.setFont(headLabel.getFont().deriveFont(headLabel.getFont().getStyle() | Font.BOLD, 23f));
		headLabel.setOpaque(true);
		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(headLabel, BorderLayout.NORTH);
		
		indexScrollPane = new JScrollPane();
		indexScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		indexScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(indexScrollPane, BorderLayout.CENTER);
		
		indexPanel = new JPanel();
		indexScrollPane.setViewportView(indexPanel);
		GridBagLayout gbl_indexPanel = new GridBagLayout();
		gbl_indexPanel.columnWidths = new int[]{0};
		gbl_indexPanel.rowHeights = new int[]{0};
		gbl_indexPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_indexPanel.rowWeights = new double[]{Double.MIN_VALUE};
		indexPanel.setLayout(gbl_indexPanel);
	}

}
