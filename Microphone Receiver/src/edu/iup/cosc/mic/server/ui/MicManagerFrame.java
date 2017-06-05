package edu.iup.cosc.mic.server.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import edu.iup.cosc.mic.server.MicReceiver;
import edu.iup.cosc.mic.server.S;

public class MicManagerFrame extends JFrame {
	public static Color IUP_RED = new Color(141, 20, 51);
	private MicReceiver receiver;

	private JLabel askerField = new JLabel();
	private DefaultListModel<MicReceiver> listModel = new DefaultListModel<MicReceiver>();
	private JList<MicReceiver> requests = new JList<MicReceiver>(listModel);

	private QueueReceiver queueReceiver;

	private class QueueReceiver extends Thread {
		private ServerSocket ss;

		public QueueReceiver() throws IOException {
			ss = new ServerSocket(S.Net.PORT);
		}

		public void run() {
			for (;;) {
				try {
					listModel.addElement(new MicReceiver(MicManagerFrame.this,
							ss.accept()));
					repaint();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public MicManagerFrame() {
		try {
			queueReceiver = new QueueReceiver();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(MicManagerFrame.this,
					"ERROR: Port Number already in use.", "Invalid Port",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		setTitle("Mic Manager");
		setLayout(new JvBoxLayout(JvBoxLayout.Y_AXIS));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel ipPanel = new JPanel(new JvBoxLayout(JvBoxLayout.X_AXIS, 5, 5, 5, 5, 5));
		ipPanel.setBorder(BorderFactory.createTitledBorder("Server IP Address:"));


		String ipAddress = "Unknown";

		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();  //sets connecting IP
		} catch (UnknownHostException e) {
		}

		ipPanel.add(new JLabel(ipAddress));
		
		add(ipPanel);
		
		JPanel speakerPanel = new JPanel(new JvBoxLayout(JvBoxLayout.X_AXIS, 5, 5, 5, 5, 5));

		speakerPanel.setBorder(BorderFactory.createTitledBorder("Speaking"));

		askerField = new JLabel();

		speakerPanel.add(askerField, new Double(1));

		JButton endButton = new JButton(new AbstractAction("Cancel") {
			public void actionPerformed(ActionEvent e) {
				if (receiver != null) {
					receiver.kill();
					clearSpeaker(receiver);
				}
			}
		});

		speakerPanel.add(endButton);
		
		add(speakerPanel);

		JButton dequeueButton = new JButton(new AbstractAction("Receive Next") {
			public void actionPerformed(ActionEvent e) {
				if (!listModel.isEmpty()) {
					MicReceiver request = (MicReceiver) listModel.elementAt(0);
					listModel.removeElement(request);
					receive(request);
				}
			}
		});

		add(dequeueButton);

		add(new JScrollPane(requests), new Double(1));

		JButton receiveButton = new JButton(new AbstractAction(
				"Receive Question") {
			public void actionPerformed(ActionEvent event) {
				if (requests.getSelectedIndex() != -1) {
					MicReceiver request = (MicReceiver) requests
							.getSelectedValue();
					listModel.removeElement(request);
					receive(request);
				}
			}
		});

		add(receiveButton);

		JButton deleteButton = new JButton(
				new AbstractAction("Delete Question") {
					public void actionPerformed(ActionEvent event) {
						if (requests.getSelectedIndex() != -1) {
							MicReceiver request = (MicReceiver) requests
									.getSelectedValue();
							listModel.removeElement(request);
							request.kill();
						}
					}
				});

		add(deleteButton);

		queueReceiver.start();

		setLocation(200, 200);
		setSize(350, 500);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MicManagerFrame();
	}

	public void receive(MicReceiver receiver) {
		this.receiver = receiver;
		try {
			receiver.activate();
			askerField.setText(receiver.toString());
			repaint();
		} catch (IOException e) {
			receiver.kill();
		} catch (LineUnavailableException e) {
			receiver.kill();
		}
	}

	public void clearSpeaker(MicReceiver receiver) {
		if (receiver == this.receiver) {
			askerField.setText("");
			repaint();
			this.receiver = null;
		}
	}

	public void removeReceiver(final MicReceiver micReceiver) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				listModel.removeElement(micReceiver);
				repaint();
			}
		});
	}
}
