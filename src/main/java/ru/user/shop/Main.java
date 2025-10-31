package ru.user.shop;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		} catch (SQLException e) {
			System.out.println("Не удалось подключиться к БД, проверьте введенные данные на правильность");
			e.printStackTrace();
			System.exit(1);
			return;
		}

		try (Connection connection = database.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();

			ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });

			System.out.println("Tables in the database:");
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				System.out.println(tableName);
			}

			tables.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
