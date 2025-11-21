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
		b2.addItem("Склад");
		b2.addItem("Поставщик");
		b2.addItem("Чек");
		b2.addItem("Поставка");
		b2.addItem("Продукт");
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

				String action = (String) b1.getSelectedItem();
				String entity = (String) b2.getSelectedItem();
				String option = (String) b3.getSelectedItem();

				if (action.equals("Вывести")) {
					switch (entity) {
					case "Покупатель" -> {
						switch (option) {
						case "Всех" -> {
							setCustomerInTable(table, database.getAllCustomers());
						}
						case "По ФИО" -> {
							setCustomerInTable(table, database.searchCustomerByFullName(area.getText()));
						}
						}
					}
					case "Сотрудник" -> {
						switch (option) {
						case "Всех" -> {
							setEmployeeInTable(table, database.getAllEmployees());
						}
						case "По ФИО" -> {
							setEmployeeInTable(table, database.searchEmployeeByFullName(area.getText()));
						}
						case "По ID магазина" -> {
							try {
								setEmployeeInTable(table,
										database.searchEmployeeByShopId(Integer.parseInt(area.getText())));
							} catch (NumberFormatException e1) {

							}
						}
						}
					}
					case "Магазин" -> {
						switch (option) {
						case "Всех" -> {
							setShopInTable(table, database.getAllShops());
						}
						}
					}
					case "Склад" -> {
						switch (option) {
						case "Всех" -> {
							setWarehouseInTable(table, database.getAllWarehouses());
						}
						}
					}
					case "Поставщик" -> {
						switch (option) {
						case "Всех" -> {
							setSupplierInTable(table, database.getAllSuppliers());
						}
						case "По названию" -> {
							setSupplierInTable(table, database.searchSupplierByName(area.getText()));
						}
						}
					}
					case "Чек" -> {
						switch (option) {
						case "Всех" -> {
							setCheckInTable(table, database.getAllChecks());
						}
						case "По ID покупателя" -> {
							try {
								setCheckInTable(table,
										database.searchCheckByCustomerId(Integer.parseInt(area.getText())));
							} catch (NumberFormatException e1) {

							}
						}
						}
					}
					case "Поставка" -> {
						switch (option) {
						case "Всех" -> {
							setSupplyInTable(table, database.getAllSupplies());
						}
						case "По ID поставщика" -> {
							try {
								setSupplyInTable(table,
										database.searchSupplyBySupplierId(Integer.parseInt(area.getText())));
							} catch (NumberFormatException e1) {

							}
						}
						}
					}
					case "Продукт" -> {
						switch (option) {
						case "Всех" -> {
							setProductInTable(table, database.getAllProducts());
						}
						case "По названию" -> {
							setProductInTable(table, database.searchProductByName(area.getText()));
						}
						case "По ID поставки" -> {
							try {
								setProductInTable(table,
										database.searchProductBySupplyId(Integer.parseInt(area.getText())));
							} catch (NumberFormatException e1) {

							}
						}
						}
					}
					}

				}

				if (action.equals("Обновить")) {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					if (model.getRowCount() == 0) {
						return;
					}

					Object[] tableFieldNames = new Object[model.getColumnCount()];
					for (int i = 0; i < model.getColumnCount(); i++) {
						tableFieldNames[i] = model.getColumnName(i);
					}

					Class<?> clazz = null; // TODO перенести в enum/hashmap?
					switch (entity) {
					case "Покупатель" -> {
						clazz = Customer.class;
					}
					case "Сотрудник" -> {
						clazz = Employee.class;
					}
					case "Магазин" -> {
						clazz = Shop.class;
					}
					case "Склад" -> {
						clazz = Warehouse.class;
					}
					case "Поставщик" -> {
						clazz = Supplier.class;
					}
					case "Чек" -> {
						clazz = Check.class;
					}
					case "Поставка" -> {
						clazz = Supply.class;
					}
					case "Продукт" -> {
						clazz = Product.class;
					}
					}
					if (clazz == null) {
						return;
					}
					Object[] classFieldNames = getObjectFieldNames(clazz);
					if (tableFieldNames.length != classFieldNames.length) {
						return;
					}
					boolean flag = true;
					for (int i = 0; i < tableFieldNames.length; i++) {
						if (!tableFieldNames[i].equals(classFieldNames[i])) {
							flag = false;
							break;
						}
					}
					if (!flag) {
						return;
					}
					// TODO сюда добавить обновление объекта
					// TODO будет баг если поля у двух разных классов будут одинаковые
					System.out.println("!#!@#");
				}
			}
		});

		onAction(b1, b2, b3);// нужно для первичного запуска
		setVisible(true);
	}

	/**
	 * Меняет параметры у b3 (опции) при изменении выбора b1 (действие) или b2
	 * (сущность)
	 * 
	 * @param b1
	 * @param b2
	 * @param b3
	 */
	private void onAction(JComboBox<String> b1, JComboBox<String> b2, JComboBox<String> b3) {
		b3.removeAllItems();

		switch ((String) b1.getSelectedItem()) {
		case "Вывести" -> {
			switch ((String) b2.getSelectedItem()) {
			case "Покупатель" -> {
				b3.addItem("Всех");
				b3.addItem("По ФИО");
			}
			case "Сотрудник" -> {
				b3.addItem("Всех");
				b3.addItem("По ФИО");
				b3.addItem("По ID магазина");
			}
			case "Магазин" -> {
				b3.addItem("Всех");
			}
			case "Склад" -> {
				b3.addItem("Всех");
			}
			case "Поставщик" -> {
				b3.addItem("Всех");
				b3.addItem("По названию");
			}
			case "Чек" -> {
				b3.addItem("Всех");
				b3.addItem("По ID покупателя");
			}
			case "Поставка" -> {
				b3.addItem("Всех");
				b3.addItem("По ID поставщика");
			}
			case "Продукт" -> {
				b3.addItem("Всех");
				b3.addItem("По названию");
				b3.addItem("По ID поставки");
			}
			}
		}
		}
	}

	private void setCustomerInTable(JTable table, List<Customer> customers) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Customer.class, customers),
				getObjectFieldNames(Customer.class));
		table.setModel(model);
	}

	private void setEmployeeInTable(JTable table, List<Employee> employees) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Employee.class, employees),
				getObjectFieldNames(Employee.class));
		table.setModel(model);
	}

	private void setShopInTable(JTable table, List<Shop> shops) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Shop.class, shops),
				getObjectFieldNames(Shop.class));
		table.setModel(model);
	}

	private void setWarehouseInTable(JTable table, List<Warehouse> warehouses) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Warehouse.class, warehouses),
				getObjectFieldNames(Warehouse.class));
		table.setModel(model);
	}

	private void setSupplierInTable(JTable table, List<Supplier> suppliers) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Supplier.class, suppliers),
				getObjectFieldNames(Supplier.class));
		table.setModel(model);
	}

	private void setCheckInTable(JTable table, List<Check> checks) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Check.class, checks),
				getObjectFieldNames(Check.class));
		table.setModel(model);
	}

	private void setSupplyInTable(JTable table, List<Supply> supplies) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Supply.class, supplies),
				getObjectFieldNames(Supply.class));
		table.setModel(model);
	}

	private void setProductInTable(JTable table, List<Product> products) {
		DefaultTableModel model = new DefaultTableModel(getObjectFieldsValues(Product.class, products),
				getObjectFieldNames(Product.class));
		table.setModel(model);
	}

	/**
	 * Получает все имена полей объекта
	 * 
	 * @param clazz Класс, в котором ищем поля
	 * @return Имена
	 */
	private Object[] getObjectFieldNames(Class<?> clazz) {
		return Stream.of(clazz.getDeclaredFields()).map(x -> x.getName()).toArray();
	}

	/**
	 * Преобразует список из объектов в двумерный массив, в котором содержатся
	 * значения полей объектов
	 * 
	 * @param clazz   Класс, который используется в списке
	 * @param objects Объекты класса
	 * @return Двумерный массив из значений полей объектов
	 */
	private Object[][] getObjectFieldsValues(Class<?> clazz, List<?> objects) {
		if (objects.isEmpty()) {
			return new Object[][] {};
		}
		int fieldCount = clazz.getDeclaredFields().length;
		Object[][] data = new Object[objects.size()][fieldCount];
		for (int i = 0; i < data.length; i++) {
			Object object = objects.get(i);
			for (int j = 0; j < fieldCount; j++) {
				Field field = object.getClass().getDeclaredFields()[j];
				try {
					field.setAccessible(true);
					data[i][j] = field.get(object);
					field.setAccessible(false);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

}
