package ru.user.shop;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

	private JComboBox<String> entities;
	private JTable table;

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

		entities = new JComboBox<String>();
		gbc.gridx = 0;
		gbc.gridy = 1;

		entities.setPreferredSize(new Dimension(125, 25));
		entities.addItem("Покупатель");
		entities.addItem("Сотрудник");
		entities.addItem("Магазин");
		entities.addItem("Склад");
		entities.addItem("Поставщик");
		entities.addItem("Чек");
		entities.addItem("Поставка");
		entities.addItem("Продукт");
		add(entities, gbc);

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

		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(width - width / 5, height / 2));

		gbc.gridx = 1;
		gbc.gridy = 5;
		add(scrollPane, gbc);

		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onComboBoxSelectItemAction(b1, entities, b3);
			}
		});

		entities.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onComboBoxSelectItemAction(b1, entities, b3);
			}
		});

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String action = (String) b1.getSelectedItem();
				String entity = (String) entities.getSelectedItem();
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
					// делаем проверку, что поля текущей таблицы совпадают с полями текущего класса
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					if (model.getRowCount() == 0) {
						return;
					}

					Object[] tableFieldNames = new Object[model.getColumnCount()];
					for (int i = 0; i < model.getColumnCount(); i++) {
						tableFieldNames[i] = model.getColumnName(i);
					}

					Object[] classFieldNames = getObjectFieldNames(getCurrentEntityClass());
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

					Class<?> clazz = getCurrentEntityClass();
					List<Object> objects = parseTableToObjects();
					for (Object object : objects) {

						if (clazz == Customer.class) {
							database.updateCustomer((Customer) object);
						} else if (clazz == Employee.class) {
							database.updateEmployee((Employee) object);
						} else if (clazz == Shop.class) {
							database.updateShop((Shop) object);
						} else if (clazz == Warehouse.class) {
							database.updateWarehouse((Warehouse) object);
						} else if (clazz == Supplier.class) {
							database.updateSupplier((Supplier) object);
						} else if (clazz == Check.class) {
							database.updateCheck((Check) object);
						} else if (clazz == Supply.class) {
							// database.updateSupply((Supply) object);
						} else if (clazz == Product.class) {
							// database.updateProduct((Product) object);
						}
					}
				}

				if (action.equals("Вставить")) {
					try {
						Class<?> clazz = getCurrentEntityClass();
						List<Object> objects = parseTableToObjects();
						for (Object object : objects) {
							if (clazz == Customer.class) {
								database.insertCustomer((Customer) object);
							} else if (clazz == Employee.class) {
								database.insertEmployee((Employee) object);
							} else if (clazz == Shop.class) {
								database.insertShop((Shop) object);
							} else if (clazz == Warehouse.class) {
								database.insertWarehouse((Warehouse) object);
							} else if (clazz == Supplier.class) {
								database.insertSupplier((Supplier) object);
							} else if (clazz == Check.class) {
								database.insertCheck((Check) object);
							} else if (clazz == Supply.class) {
								database.insertSupply((Supply) object);
							} else if (clazz == Product.class) {
								database.insertProduct((Product) object);
							}
						}
						table.setModel(new DefaultTableModel());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		onComboBoxSelectItemAction(b1, entities, b3);// нужно для первичного запуска
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
	private void onComboBoxSelectItemAction(JComboBox<String> b1, JComboBox<String> b2, JComboBox<String> b3) {
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
		case "Вставить" -> {
			DefaultTableModel model = new DefaultTableModel(
					new Object[1][getCurrentEntityClass().getDeclaredFields().length],
					getObjectFieldNames(getCurrentEntityClass()));
			table.setModel(model);
		}
		}
	}

	/**
	 * Функция узнает, с какой сущностью мы сейчас работаем с помощью ComboBox
	 * entites
	 * 
	 * @return Возвращает класс сущности
	 */
	private Class<?> getCurrentEntityClass() {
		switch ((String) entities.getSelectedItem()) {
		case "Покупатель" -> {
			return Customer.class;
		}
		case "Сотрудник" -> {
			return Employee.class;
		}
		case "Магазин" -> {
			return Shop.class;
		}
		case "Склад" -> {
			return Warehouse.class;
		}
		case "Поставщик" -> {
			return Supplier.class;
		}
		case "Чек" -> {
			return Check.class;
		}
		case "Поставка" -> {
			return Supply.class;
		}
		case "Продукт" -> {
			return Product.class;
		}
		}
		throw new NullPointerException();
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

	private List<Object> parseTableToObjects() {
		List<Object> objects = new ArrayList<Object>();
		try {
			for (int row = 0; row < table.getRowCount(); row++) {
				Object object = getCurrentEntityClass().newInstance();
				for (int column = 0; column < table.getColumnCount(); column++) {
					Field field = object.getClass().getDeclaredFields()[column];
					String value = table.getValueAt(row, column).toString();
					field.setAccessible(true);
					if (field.getName().equals("id")) {
						if (value == null) {
							continue;
						}
						try {
							field.setInt(object, Integer.parseInt(value));
						} catch (NumberFormatException ignored) {

						}
						continue;
					}

					Class<?> fieldType = field.getType();
					if (fieldType == int.class) {
						field.setInt(object, Integer.parseInt(value));
					}
					if (fieldType == long.class) {
						field.setLong(object, Long.parseLong(value));

					}
					if (fieldType == Date.class) {
						LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
						Date sqlDate = Date.valueOf(localDate);
						field.set(object, sqlDate);
					}
					if (fieldType == String.class) {
						field.set(object, value);
					}
				}
				objects.add(object);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return objects;
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
