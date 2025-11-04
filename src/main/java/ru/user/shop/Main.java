package ru.user.shop;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;

public class Main {

	private static Database database;

	public static void main(String[] args) {
		// этот кусок кода разрывает соединение с бд в случае отключения программы
		Runtime.getRuntime().addShutdownHook(new Thread(() -> closeDatabase()));

		Options options = new Options();

		options.addRequiredOption("h", "host", true, "Hostname");
		options.addRequiredOption("p", "port", true, "Port");
		options.addRequiredOption("d", "database", true, "Database");
		options.addRequiredOption("u", "username", true, "Username");
		options.addRequiredOption("ps", "password", true, "Password");

		// TODO дописать описание
		options.addOption("l", "list", true, "Lists tables, customers, employees, shops, warehouses, ...");
		options.addOption("i", "insert", true, "Insert customer, employee, ...");

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = HelpFormatter.builder().get();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			try {
				formatter.printHelp("shop", "", options, "", false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(1);
			return;
		}

		String host = cmd.getOptionValue("host");
		int port;
		try {
			port = Integer.parseInt(cmd.getOptionValue("port"));
		} catch (NumberFormatException e) {
			System.out.println("Неправильно введен порт");
			System.exit(1);
			return;
		}
		String databaseName = cmd.getOptionValue("database");
		String username = cmd.getOptionValue("username");
		String password = cmd.getOptionValue("password");

		try {
			database = new Database(host, port, databaseName, username, password);
			System.out.println("Успешное соединение с БД");
		} catch (SQLException e) {
			System.out.println("Не удалось подключиться к БД, проверьте введенные данные на правильность");
			e.printStackTrace();
			System.exit(1);
			return;
		}
		executeCommand(cmd);
		System.out.println("Конец работы программы");
	}

	public static void executeCommand(CommandLine cmd) {
		if (cmd.hasOption("list")) {
			// break не нужен, тк case "" -> {} сам ставит его, при case "": { } нужен
			switch (cmd.getOptionValue("list")) {
			case "tables" -> {
				List<String> tables = database.getTables();
				if (tables.isEmpty()) {
					System.out.println("Таблиц нет");
				} else {
					System.out.println("Таблицы:");
					tables.forEach((table) -> {
						System.out.println(table);
					});
				}
			}
			case "customers" -> {
				List<Customer> customers = database.getAllCustomers();
				if (customers.isEmpty()) {
					System.out.println("Покупателей нет");
				} else {
					System.out.println("Покупатели:");
					customers.forEach((customer) -> {
						System.out.println(customer.toString());
					});
				}
			}
			case "employees" -> {
				List<Employee> employees = database.getAllEmployees();
				if (employees.isEmpty()) {
					System.out.println("Сотрудников нет");
				} else {
					System.out.println("Сотрудники:");
					employees.forEach((employee) -> {
						System.out.println(employee.toString());
					});
				}
			}
			case "shops" -> {
				List<Shop> shops = database.getAllShops();
				if (shops.isEmpty()) {
					System.out.println("Магазинов нет");
				} else {
					System.out.println("Магазины:");
					shops.forEach((shop) -> {
						System.out.println(shop.toString());
					});
				}
			}
			case "warehouses" -> {
				List<Warehouse> warehouses = database.getAllWarehouses();
				if (warehouses.isEmpty()) {
					System.out.println("Складов нет");
				} else {
					System.out.println("Склады:");
					warehouses.forEach((warehouse) -> {
						System.out.println(warehouse.toString());
					});
				}
			}
			default -> {
				System.out.println("Такого параметра нет");
			}
			}
			return;
		}

		if (cmd.hasOption("insert")) {
			Scanner scanner = new Scanner(System.in, Charset.forName("Windows-1251"));
			switch (cmd.getOptionValue("insert")) {
			case "customer" -> {
				insertCustomer(scanner);
			}
			case "employee" -> {
				insertEmployee(scanner);
			}
			case "shop" -> {
				insertShop(scanner);
			}
			case "warehouse" -> {
				insertWarehouse(scanner);
			}
			default -> {
				System.out.println("Такого параметра нет");
			}
			}
			scanner.close();
			return;
		}
	}

	public static void insertCustomer(Scanner scanner) {
		System.out.println("Введите ФИО");
		String fullName = scanner.nextLine();
		System.out.println("Введите почту");
		String email = scanner.nextLine();
		System.out.println("Введите номер телефона");
		long phoneNumber = getPhoneNumberFromInput(scanner);
		if (database.insertCustomer(new Customer(fullName, email, phoneNumber))) {
			System.out.println("Покупатель добавлен в базу");
		} else {
			System.out.println("Покупатель не добавлен в базу");
		}
	}

	public static void insertEmployee(Scanner scanner) {
		System.out.println("Введите ФИО");
		String fullName = scanner.nextLine();
		System.out.println("Введите почту");
		String email = scanner.nextLine();
		System.out.println("Введите номер телефона");
		long phoneNumber = getPhoneNumberFromInput(scanner);
		System.out.println("Введите дату рождения в формате (YYYY-MM-DD)");
		Date birthdayDate = getDateFromInput(scanner);
		System.out.println("Введите дату трудоустройства в формате (YYYY-MM-DD)");
		Date dateOfEmployment = getDateFromInput(scanner);
		System.out.println("Введите статус");
		String status = scanner.nextLine();
		System.out.println("Введите ID магазина, где работает сотрудник");
		int shopId = getIntFromInput(scanner);
//TODO тут должно запрашивать фото, пока пропустим
		if (database.insertEmployee(
				new Employee(fullName, email, phoneNumber, birthdayDate, dateOfEmployment, status, shopId, "PHOTO"))) {
			System.out.println("Сотрудник добавлен в базу");
		} else {
			System.out.println("Сотрудник не добавлен в базу");
		}
	}

	public static void insertShop(Scanner scanner) {
		System.out.println("Введите адрес");
		String address = scanner.nextLine();
		if (database.insertShop(new Shop(address))) {
			System.out.println("Магазин добавлен в базу");
		} else {
			System.out.println("Магазин не добавлен в базу");
		}
	}

	public static void insertWarehouse(Scanner scanner) {
		System.out.println("Введите адрес");
		String address = scanner.nextLine();
		System.out.println("Введите ID магазина, к которому принадлежит склад");
		int shopId = getIntFromInput(scanner);
		if (database.insertWarehouse(new Warehouse(address, shopId))) {
			System.out.println("Склад добавлен в базу");
		} else {
			System.out.println("Склад не добавлен в базу");
		}
	}

	public static void closeDatabase() {
		if (database != null) {
			try {
				database.close();
			} catch (SQLException e) {
				System.out.println("Не удалось закрыть соединение с БД");
				e.printStackTrace();
			}
		}
	}

	private static int getIntFromInput(Scanner scanner) {
		int a;
		while (true) {
			try {
				a = Integer.parseInt(scanner.nextLine());
				break;
			} catch (NumberFormatException e) {
				System.out.println("Неправильно введено число, повторите попытку");
			}
		}
		return a;
	}

	private static long getPhoneNumberFromInput(Scanner scanner) {
		long phoneNumber;
		while (true) {
			try {
				phoneNumber = Long.parseLong(scanner.nextLine());
				break;
			} catch (NumberFormatException e) {
				System.out.println("Неправильно введен номер, повторите попытку");
			}
		}
		return phoneNumber;
	}

	private static Date getDateFromInput(Scanner scanner) {
		Date date;
		while (true) {
			try {
				String input = scanner.nextLine();
				String[] split = input.split("-");
				date = new Date(Integer.parseInt(split[0]) - 1900, Integer.parseInt(split[1]) + 1,
						Integer.parseInt(split[2]));
				break;
			} catch (Exception e) {
				System.out.println("Неправильно введена дата, повторите попытку");
			}
		}
		return date;
	}

}
