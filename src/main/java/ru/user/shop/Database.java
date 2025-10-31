package ru.user.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.Getter;

public class Database {

	@Getter
	private final Connection connection;

	public Database(String host, int port, String database, String username, String password) throws SQLException {
		String jdbcURL = "jdbc:postgresql://%s:%d/%s?sslmode=disable".formatted(host, port, database);
		connection = DriverManager.getConnection(jdbcURL, username, password);
	}

	public void close() throws SQLException {
		connection.close();
	}

}
