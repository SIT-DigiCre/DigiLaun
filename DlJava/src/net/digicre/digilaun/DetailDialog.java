package net.digicre.digilaun;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.digicre.digilaun.work.Work;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 作品の詳細を表示し、体験開始機能を提供します。
 * @author p10090
 *
 */
@SuppressWarnings("serial")
class DetailDialog extends JDialog {
	/**
	 * ファイルを実行もオープンもできなかったときのエラーメッセージです。
	 */
	private static final String OPENING_ERROR_MESSAGE =
			"このさくひんは たいけんできないよ";

	/**
	 * はう２ぷれいラベルに表示するテキストです。
	 * 体験にどんなデバイスが必要かを記します。
	 */
	private static final String H2P_LABEL_TEXT = "この作品では, %sを使います.";
	
	/**
	 * 強制終了ボタンのテキストです。
	 */
	private static final String KILL_BUTTON_TEXT = "KILL";

	private Work work;
	private Process process;
	private ProcessLogger logger;
	private final JPanel contentPanel = new JPanel();
	private JLabel h2pLabel;
	private JButton startButton;
	private JButton cancelButton;

	/**
	 * プロセスの終了を待ってウィンドウを閉じるスレッドです。
	 * @author p10090
	 */
	private class ProcessWaitingThread extends Thread {
		@Override
		public void run() {
			putLog(ProcessLogger.OpenStatus.Started);
			try {
				DetailDialog.this.process.waitFor();
			} catch (InterruptedException e) {
			}
			if(DetailDialog.this.process != null)
				putLog(DetailDialog.this.process.exitValue() != 0
					? ProcessLogger.OpenStatus.ExitFailed
					: ProcessLogger.OpenStatus.ExitSuccessful);
			DetailDialog.this.dispatchEvent(new WindowEvent(
					DetailDialog.this, WindowEvent.WINDOW_CLOSING));
		}
	}

	/**
	 * Create the dialog.
	 */
	private DetailDialog() {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(process != null) {
					// プロセスが終了済みなら閉じる
					try {
						process.exitValue();
						DetailDialog.this.dispose();
					}
					// プロセスが実行中なら何もしない
					catch(IllegalThreadStateException e) {
					}
				}
				// プロセスが未実行なら閉じる
				else {
					DetailDialog.this.dispose();
				}
			}
			@Override
			public void windowClosed(WindowEvent arg0) {
				synchronized(DetailDialog.this) {
					if(DetailDialog.this.logger != null)
						DetailDialog.this.logger.close();
				}
			}
		});
		setTitle("作品詳細");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(10);
		borderLayout.setHgap(10);
		getContentPane().setLayout(borderLayout);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(5, 5));
			{
				startButton = new JButton("はじめる!");
				startButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(DetailDialog.this.process == null) {
							DetailDialog.this.startWorkTrial();
							((javax.swing.AbstractButton)arg0.getSource()
									).setEnabled(false);
						}
					}
				});
				startButton.setFont(startButton.getFont().deriveFont(48f));
				startButton.setActionCommand("OK");
				buttonPane.add(startButton, BorderLayout.CENTER);
				getRootPane().setDefaultButton(startButton);
			}
			{
				cancelButton = new JButton("もどる");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// プロセスが実行中なら
						if(process != null) {
							try {
								process.exitValue();
							}
							catch(IllegalThreadStateException ex) {
								synchronized(DetailDialog.this) {
									// 強制終了
									DetailDialog.this.process.destroy();
									DetailDialog.this.process = null;
								}
								DetailDialog.this.putLog(
										ProcessLogger.OpenStatus.Killed);
							}
						}
						// 閉じる
						DetailDialog.this.processWindowEvent(
								new java.awt.event.WindowEvent(
										DetailDialog.this,
										java.awt.event.WindowEvent.
										WINDOW_CLOSING));
					}
				});
				cancelButton.setFont(cancelButton.getFont().deriveFont(32f));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton, BorderLayout.EAST);
			}
			{
				JPanel infoPanel = new JPanel();
				buttonPane.add(infoPanel, BorderLayout.SOUTH);
				{
					JButton btni = new JButton("もっと詳しい情報(I)");
					btni.setMnemonic('I');
					infoPanel.add(btni);
				}
			}
			{
				h2pLabel = new JLabel("遊びかたは, かかりいんにおたずねください.");
				h2pLabel.setHorizontalAlignment(SwingConstants.CENTER);
				buttonPane.add(h2pLabel, BorderLayout.NORTH);
			}
		}
	}
	
	public DetailDialog(Work work) {
		this();
		// イラストが 640x480 になるようサイズ調整
		this.pack();
		this.setSize(
				this.getWidth()  - this.contentPanel.getWidth()  + 640,
				this.getHeight() - this.contentPanel.getHeight() + 480);
		// 作品データを読んでコンポーネントに反映
		this.work = work;
		this.setTitle(work.getName());
		{
			final String idev = work.getInputDeviceName();
			if(idev != null && !idev.isEmpty()) {
				this.h2pLabel.setText(String.format(H2P_LABEL_TEXT, idev));
			}
		}
		//TODO イラスト画像貼ったり、 Desktop 作ったり…
	}

	/**
	 * 作品の体験を始めます。
	 * <br>実行ファイルとしての実行を試み、失敗したらファイルとして開きます。
	 */
	private void startWorkTrial() {
		// 情報を準備
		final File file = new File(work.getPath());
		File pdir = file.getParentFile();
		final ProcessBuilder pb = new ProcessBuilder(
				work.getPath(), work.getArguments());
		pb.directory(pdir != null ? pdir : new File("."));
		try {
			logger = new ProcessLogger();
		} catch (IOException e2) {
			e2.printStackTrace();
			logger = null;
		}
		// 実行
		try {
			this.process = pb.start();
			new ProcessWaitingThread().start();
			this.cancelButton.setText(DetailDialog.KILL_BUTTON_TEXT);
		}
		// 失敗したら
		catch (IOException e) {
			// 開く
			Desktop d = Desktop.getDesktop();
			try {
				d.open(file);
				putLog(ProcessLogger.OpenStatus.Opened);
				DetailDialog.this.dispose();
			} catch (Exception e1) {
				// エラーを表示
				putLog(ProcessLogger.OpenStatus.CannotOpen);
				javax.swing.JOptionPane.showMessageDialog(
						DetailDialog.this, String.format("%s\n\n%s\n\n%s",
								DetailDialog.OPENING_ERROR_MESSAGE,
								e .getMessage(),
								e1.getMessage()));
			}
		}
	}

	/**
	 * 起動ステータスをログに書き込みます。
	 * @param status ステータス
	 */
	synchronized private void putLog(ProcessLogger.OpenStatus status) {
		try {
			this.logger.writeWorkStatus(this.work, status);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(NullPointerException e) {
		}
	}
}
