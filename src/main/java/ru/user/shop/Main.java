package ru.user.shop;

import java.io.IOException;
import java.nio.charset.Charset;
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
		options.addOption("l", "list", true, "Lists tables, customers, ...");
		options.addOption("i", "insert", true, "Insert customer, ...");

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
		work(cmd);
		System.out.println("Конец работы программы");
	}

	public static void work(CommandLine cmd) {
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
			default -> {
				System.out.println("Такого параметра нет");
			}
			}
			return;
		}

		if (cmd.hasOption("insert")) {
			switch (cmd.getOptionValue("insert")) {
			case "customer" -> {
				Scanner scanner = new Scanner(System.in, Charset.forName("Windows-1251"));
				System.out.println("Введите ФИО");
				String fullName = scanner.nextLine();
				System.out.println("Введите почту");
				String email = scanner.nextLine();
				System.out.println("Введите номер телефона");
				long phoneNumber;
				while (true) {
					try {
						phoneNumber = Long.parseLong(scanner.nextLine());
						break;
					} catch (NumberFormatException e) {
						System.out.println("Неправильно введен номер, повторите попытку");
					}
				}
				scanner.close();
				if (database.insertCustomer(new Customer(fullName, email, phoneNumber))) {
					System.out.println("Покупатель добавлен в базу");
				} else {
					System.out.println("Покупатель не добавлен в базу");
				}
			}
			}

			return;
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

}
