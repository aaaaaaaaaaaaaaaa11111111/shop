package ru.user.shop;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class Main {

	private static Database database;

	public static void main(String[] args) throws Exception {
		database = new Database("192.168.56.101", 5432, "shop", "admin", "123456");// да простят меня все программисты
																					// за логин и пароль в коде
		Connection connection = database.getConnection();
		DatabaseMetaData metaData = connection.getMetaData();

		ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });

		System.out.println("Tables in the database:");
		while (tables.next()) {
			String tableName = tables.getString("TABLE_NAME");
			System.out.println(tableName);
		}

		tables.close();
	}
}
