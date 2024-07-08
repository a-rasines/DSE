package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import domain.Transaction;
import remote.ServiceLocator;

/**
 * Window generated using Eclipse's WindowBuilder
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setTitle("Bank Application");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Global");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Audit");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(null, "The total balance is "+ServiceLocator.instance.getService().audit()+"€");
				} catch (HeadlessException | RemoteException e1) {
					JOptionPane.showMessageDialog(null, "There was a problem reaching the server", "Network error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenu mnNewMenu1 = new JMenu("Balance");
		menuBar.add(mnNewMenu1);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 2, 10, 10));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 10));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "From", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_4.add(scrollPane);
		Collection<String> listItems;
		try {
			listItems = ServiceLocator.instance.getService().getHashes();
		} catch (RemoteException e1) {
			JOptionPane.showMessageDialog(null, "There was a problem reaching the server", "Network error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			dispose();
			return;
		}
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		listModel.addAll(listItems);
		JList<String> list = new JList<>(listModel);
		
		scrollPane.setViewportView(list);
		
		JLabel lblNewLabel = new JLabel("Amount to transfer: ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
		panel_1.add(lblNewLabel, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 10));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.SOUTH);
		
		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(10);
		

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "To", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_5.add(scrollPane_1);
		
		DefaultListModel<String> list_1Model = new DefaultListModel<String>();
		list_1Model.addAll(listItems);
		
		JList<String> list_1 = new JList<>(list_1Model);
		scrollPane_1.setViewportView(list_1);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String from = list.getSelectedValue();
				String to = list_1.getSelectedValue();
				if(from == null || to == null) {
					JOptionPane.showMessageDialog(null, "Both lists must have a selected value", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if(from.equals(to)) {
					JOptionPane.showMessageDialog(null, "Can't transfer to account of origin", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					if(ServiceLocator.instance.getService().transfer(new Transaction(from, to, Double.parseDouble(textField.getText())))) {
						JOptionPane.showMessageDialog(null, "Transaction successfully done");
					}
				} catch (HeadlessException | RemoteException e1) {
					JOptionPane.showMessageDialog(null, "There was a problem reaching the server", "Network error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch(NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Transfer amount must be a number", "Syntax error error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(btnNewButton);
		
		JMenuItem mntmNewMenu1Item = new JMenuItem("From");
		mntmNewMenu1Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "You must select a bank account from the 'From' column to view the balance", "Error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					JOptionPane.showMessageDialog(null, "The total balance is "+ServiceLocator.instance.getService().getBalance(list.getSelectedValue())+"€");
				} catch (HeadlessException | RemoteException e1) {
					JOptionPane.showMessageDialog(null, "There was a problem reaching the server", "Network error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu1.add(mntmNewMenu1Item);
		
		JMenuItem mntmNewMenu1Item1 = new JMenuItem("To");
		mntmNewMenu1Item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list_1.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "You must select a bank account from the 'To' column to view the balance", "Error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					JOptionPane.showMessageDialog(null, "The total balance is "+ServiceLocator.instance.getService().getBalance(list_1.getSelectedValue())+"€");
				} catch (HeadlessException | RemoteException e1) {
					JOptionPane.showMessageDialog(null, "There was a problem reaching the server", "Network error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu1.add(mntmNewMenu1Item1);
		
	}

}
