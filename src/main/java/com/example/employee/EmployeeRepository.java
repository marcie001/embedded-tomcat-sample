package com.example.employee;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import com.example.employee.infra.JdbcTemplate;

/**
 * Employee のリポジトリ
 * 
 * @author marcie
 *
 */
public class EmployeeRepository {

	private final JdbcTemplate<Employee> jdbcTemplate;

	public EmployeeRepository() throws NamingException {
		jdbcTemplate = new JdbcTemplate<>();
	}

	public EmployeeRepository(JdbcTemplate<Employee> jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Employee> findAll() throws SQLException {

		String sql = "SELECT * FROM employees ORDER BY full_name";
		return jdbcTemplate.queryForList(sql, rs -> new Employee(rs.getString("id"), rs.getString("full_name"),
				rs.getDate("birth_day").toLocalDate(), rs.getInt("salary")));
	}

	private int add(Employee employee) throws SQLException {
		return jdbcTemplate.update("INSERT INTO employees (id, full_name, birth_day, salary) values (?, ?, ?, ?)",
				Arrays.asList(employee.getId(), employee.getFullName(), Date.valueOf(employee.getBirthDay()),
						employee.getSalary()));
	}

	private int set(Employee employee) throws SQLException {
		return jdbcTemplate.update("UPDATE employees SET full_name = ?, birth_day = ?, salary = ? WHERE id = ?",
				Arrays.asList(employee.getFullName(), Date.valueOf(employee.getBirthDay()), employee.getSalary(),
						employee.getId()));
	}

	public int delete(String id) throws SQLException {
		return jdbcTemplate.update("DELETE FROM employees WHERE id = ?", Arrays.asList(id));
	}

	public int save(Employee employee) throws SQLException {
		if (employee.getId() == null || employee.getId().isEmpty()) {
			employee.setId(UUID.randomUUID().toString());
			return add(employee);
		}
		return set(employee);
	}
}
