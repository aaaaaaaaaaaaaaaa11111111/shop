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
		try {
			return resultToCustomers(connection.createStatement().executeQuery("SELECT * FROM customers;"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Employee> getAllEmployees() {
		try {
			return resultToEmployees(connection.createStatement().executeQuery("SELECT * FROM employees;"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Shop> getAllShops() {
		List<Shop> shops = new ArrayList<Shop>();
		try {
			ResultSet result = connection.createStatement().executeQuery("SELECT * FROM shops;");
			while (result.next()) {
				shops.add(new Shop(result.getInt("id"), result.getString("address")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return shops;
	}

	public List<Warehouse> getAllWarehouses() {
		List<Warehouse> warehouses = new ArrayList<Warehouse>();
		try {
			ResultSet result = connection.createStatement().executeQuery("SELECT * FROM warehouses;");
			while (result.next()) {
				warehouses
						.add(new Warehouse(result.getInt("id"), result.getString("address"), result.getInt("shop_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return warehouses;
	}

	public List<Supplier> getAllSuppliers() {
		try {
			return resultToSuppliers(connection.createStatement().executeQuery("SELECT * FROM suppliers;"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Check> getAllChecks() {
		try {
			return resultToChecks(connection.createStatement().executeQuery("SELECT * FROM checks;"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Supply> getAllSupplies() {
		List<Supply> supplies = new ArrayList<Supply>();
		try {
			ResultSet result = connection.createStatement().executeQuery("SELECT * FROM supplies;");
			while (result.next()) {
				supplies.add(new Supply(result.getInt("id"), result.getDouble("price"), result.getDate("delivery_date"),
						result.getInt("supplier_id"), result.getInt("warehouse_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return supplies;
	}

	public List<Product> getAllProducts() {
		try {
			resultToProducts(connection.createStatement().executeQuery("SELECT * FROM products;"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
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

	public boolean insertEmployee(Employee employee) {
		try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO employees (full_name, email, phone_number, birthday_date, date_of_employment, status, shop_id, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
			statement.setString(1, employee.getFullName());
			statement.setString(2, employee.getEmail());
			statement.setLong(3, employee.getPhoneNumber());
			statement.setDate(4, employee.getBirthdayDate());
			statement.setDate(5, employee.getDateOfEmployment());
			statement.setString(6, employee.getStatus());
			statement.setInt(7, employee.getShopId());
			statement.setBytes(8, employee.getPhoto().getBytes());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertShop(Shop shop) {
		try (PreparedStatement statement = connection.prepareStatement("INSERT INTO shops (address) VALUES (?)")) {
			statement.setString(1, shop.getAddress());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertWarehouse(Warehouse warehouse) {
		try (PreparedStatement statement = connection
				.prepareStatement("INSERT INTO warehouses (address, shop_id) VALUES (?, ?)")) {
			statement.setString(1, warehouse.getAddress());
			statement.setInt(2, warehouse.getShopId());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertSupplier(Supplier supplier) {
		try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO suppliers (name, email, phone_number, address, inn, rating) VALUES (?, ?, ?, ?, ?, ?)")) {
			statement.setString(1, supplier.getName());
			statement.setString(2, supplier.getEmail());
			statement.setLong(3, supplier.getPhoneNumber());
			statement.setString(4, supplier.getAddress());
			statement.setLong(5, supplier.getInn());
			statement.setInt(6, supplier.getRating());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Customer> searchCustomerByFullName(String fullName) {
		try (PreparedStatement statement = connection
				.prepareStatement("SELECT * FROM customers WHERE full_name LIKE ?")) {
			statement.setString(1, "%" + fullName + "%");
			return resultToCustomers(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Employee> searchEmployeeByFullName(String fullName) {
		try (PreparedStatement statement = connection
				.prepareStatement("SELECT * FROM employees WHERE full_name LIKE ?")) {
			statement.setString(1, "%" + fullName + "%");
			return resultToEmployees(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Employee> searchEmployeeByShopId(int shopId) {
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees WHERE shop_id = ?")) {
			statement.setInt(1, shopId);
			return resultToEmployees(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Product> searchProductByName(String name) {
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE name LIKE ?")) {
			statement.setString(1, "%" + name + "%");
			return resultToProducts(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Product> searchProductBySupplyId(int supplyId) {
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE supply_id = ?")) {
			statement.setInt(1, supplyId);
			return resultToProducts(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Supplier> searchSupplierByName(String name) {
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM suppliers WHERE name LIKE ?")) {
			statement.setString(1, "%" + name + "%");
			return resultToSuppliers(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	public List<Check> searchCheckByCustomerId(int customerId) {
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM checks WHERE customer_id = ?")) {
			statement.setInt(1, customerId);
			return resultToChecks(statement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	private List<Customer> resultToCustomers(ResultSet result) throws SQLException {
		List<Customer> customers = new ArrayList<Customer>();
		while (result.next()) {
			customers.add(new Customer(result.getInt("id"), result.getString("full_name"), result.getString("email"),
					result.getLong("phone_number")));
		}
		return customers;
	}

	private List<Employee> resultToEmployees(ResultSet result) throws SQLException {
		List<Employee> employees = new ArrayList<Employee>();
		while (result.next()) {
			employees.add(new Employee(result.getInt("id"), result.getString("full_name"), result.getString("email"),
					result.getLong("phone_number"), result.getDate("birthday_date"),
					result.getDate("date_of_employment"), result.getString("status"), result.getInt("shop_id"),
					result.getString("photo")));
		}
		return employees;
	}

	private List<Product> resultToProducts(ResultSet result) throws SQLException {
		List<Product> products = new ArrayList<Product>();
		while (result.next()) {
			products.add(new Product(result.getInt("id"), result.getString("name"), result.getInt("article"),
					result.getInt("amount"), result.getInt("supply_id"), result.getDouble("price")));
		}
		return products;
	}

	private List<Supplier> resultToSuppliers(ResultSet result) throws SQLException {
		List<Supplier> suppliers = new ArrayList<Supplier>();
		while (result.next()) {
			suppliers.add(new Supplier(result.getInt("id"), result.getString("name"), result.getString("email"),
					result.getLong("phone_number"), result.getString("address"), result.getLong("inn"),
					result.getInt("rating")));
		}
		return suppliers;
	}

	private List<Check> resultToChecks(ResultSet result) throws SQLException {
		List<Check> checks = new ArrayList<Check>();
		while (result.next()) {
			checks.add(new Check(result.getInt("id"), result.getInt("shop_id"), result.getInt("customer_id"),
					result.getDate("purchase_date"), result.getDouble("discount")));
		}
		return checks;
	}

}
