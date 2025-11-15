package ru.user.shop;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {

	private Database database;

	public GUI(Database database) {
		super("Shop");

		this.database = database;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		width -= width / 5;
		height -= height / 5;

		setSize(width, height);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		JComboBox<String> b1 = new JComboBox<String>();
		b1.setPreferredSize(new Dimension(125, 25));
		gbc.gridx = 0;
		gbc.gridy = 0;
		b1.addItem("Вывести");
		b1.addItem("Вставить");
		b1.addItem("Обновить");
		add(b1, gbc);

		JComboBox<String> b2 = new JComboBox<String>();
		gbc.gridx = 0;
		gbc.gridy = 1;

		b2.setPreferredSize(new Dimension(125, 25));
		b2.addItem("Покупатель");
		b2.addItem("Сотрудник");
		b2.addItem("Магазин");
		add(b2, gbc);

		JComboBox<String> b3 = new JComboBox<String>();
		gbc.gridx = 0;
		gbc.gridy = 2;
		b3.setPreferredSize(new Dimension(125, 25));
		add(b3, gbc);

		JButton button = new JButton("Выполнить");
		gbc.gridx = 0;
		gbc.gridy = 3;
		button.setPreferredSize(new Dimension(125, 25));
		add(button, gbc);

		JTextArea area = new JTextArea("Поле для поиска");

		gbc.gridx = 0;
		gbc.gridy = 4;
		area.setPreferredSize(new Dimension(125, 25));
		add(area, gbc);

		JTable table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(width - width / 5, height / 2));

		gbc.gridx = 1;
		gbc.gridy = 5;
		add(scrollPane, gbc);

		// add(table, gbc);

		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onAction(b1, b2, b3);
			}
		});

		b2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onAction(b1, b2, b3);
			}
		});

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				switch ((String) b1.getSelectedItem()) {
				case "Вывести" -> {
					switch ((String) b2.getSelectedItem()) {
					case "Покупатель" -> {
						switch ((String) b3.getSelectedItem()) {
						case "Всех" -> {
							setCustomerInTable(table, database.getAllCustomers());
						}
						case "По ФИО" -> {
							setCustomerInTable(table, database.searchCustomerByFullName(area.getText()));
						}
						}
					}
					}

				}
				}
			}
		});

		onAction(b1, b2, b3);// нужно для первичного запуска
		setVisible(true);
	}

	private void onAction(JComboBox<String> b1, JComboBox<String> b2, JComboBox<String> b3) {
		b3.removeAllItems();

		switch ((String) b1.getSelectedItem()) {
		case "Вывести" -> {
			switch ((String) b2.getSelectedItem()) {
			case "Покупатель" -> {
				b3.addItem("Всех");
				b3.addItem("По ФИО");
			}
			}
		}
		}
	}

	private void setCustomerInTable(JTable table, List<Customer> customers) {
		Class<?> clazz = Customer.class;
		Field[] fields = clazz.getDeclaredFields();

		Object[][] data = new Object[customers.size()][fields.length];
		for (int i = 0; i < data.length; i++) {
			Customer customer = customers.get(i);
			for (int j = 0; j < fields.length; j++) {
				Field field = customer.getClass().getDeclaredFields()[j];
				try {
					field.setAccessible(true);
					data[i][j] = field.get(customer);
					field.setAccessible(false);
					DefaultTableModel model = new DefaultTableModel(data,
							Stream.of(fields).map(x -> x.getName()).toArray());
					table.setModel(model);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
