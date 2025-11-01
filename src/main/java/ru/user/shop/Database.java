package ru.user.shop;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	public List<String> getTables() {
		List<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet result = metaData.getTables(null, null, "%", new String[] { "TABLE" });
			while (result.next()) {
				String tableName = result.getString("TABLE_NAME");
				tables.add(tableName);
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		try {
			ResultSet result = connection.createStatement().executeQuery("SELECT * FROM customers;");
			while (result.next()) {
				customers.add(new Customer(result.getInt("id"), result.getString("full_name"),
						result.getString("email"), result.getLong("phone_number")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;
	}

	public boolean insertCustomer(Customer customer) {
		try (PreparedStatement statement = connection
				.prepareStatement("INSERT INTO customers (full_name, email, phone_number) VALUES (?, ?, ?)")) {
			statement.setString(1, customer.getFullName());
			statement.setString(2, customer.getEmail());
			statement.setLong(3, customer.getPhoneNumber());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
