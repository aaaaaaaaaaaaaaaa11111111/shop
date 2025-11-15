package ru.user.shop;

import java.sql.SQLException;

import javax.swing.SwingUtilities;

public class Main {

	private static Database database;

	public static void main(String[] args) {
		// этот кусок кода разрывает соединение с бд в случае отключения программы
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			closeDatabase();
			System.out.println("Конец работы программы");
		}));

		try {
			String host = "192.168.56.101";
			int port = 5432;
			String databaseName = "shop";
			String username = "admin";
			String password = "123456";

			database = new Database(host, port, databaseName, username, password);
			System.out.println("Успешное соединение с БД");

			SwingUtilities.invokeLater(() -> {
				new GUI(database);
			});
		} catch (SQLException e) {
			System.out.println("Не удалось подключиться к БД, проверьте введенные данные на правильность");
			e.printStackTrace();
			System.exit(1);
			return;
		} catch (Exception e) {
			System.out.println("Произошла ошибка");
			e.printStackTrace();
			System.exit(1);
			return;
		}
		System.out.println("Конец main");
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
