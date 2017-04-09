package com.example.employee.infra;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * データベースとのやりとりを行う
 * 
 * @author marcie
 *
 * @param <T>
 */
public class JdbcTemplate<T> {

	private static DataSource ds;

	public JdbcTemplate() throws NamingException {
		if (ds == null) {
			InitialContext initContext = new InitialContext();
			Context envCtx = (Context) initContext.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/db");
		}
	}

	public List<T> queryForList(String sql, RowMapper<T> rowMapper) throws SQLException {
		return queryForList(sql, new ArrayList<T>(), rowMapper);
	}

	public List<T> queryForList(String sql, List<?> parameters, RowMapper<T> rowMapper) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				prepareParameters(stmt, parameters);

				try (ResultSet rs = stmt.executeQuery()) {
					List<T> result = new ArrayList<>();
					while (rs.next()) {
						result.add(rowMapper.mapRow(rs));
					}
					return result;
				}
			}
		}

	}

	public int update(String sql, List<?> parameters) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				prepareParameters(stmt, parameters);
				return stmt.executeUpdate();
			}
		}
	}

	protected void prepareParameters(PreparedStatement stmt, List<?> parameters) throws SQLException {
		for (int i = 0; i < parameters.size(); i++) {
			Object e = parameters.get(i);
			int j = i + 1;
			if (e instanceof String) {
				stmt.setString(j, (String) e);
			} else if (e instanceof Integer) {
				stmt.setInt(j, (Integer) e);
			} else if (e instanceof Long) {
				stmt.setLong(j, (Long) e);
			} else if (e instanceof Short) {
				stmt.setShort(j, (Short) e);
			} else if (e instanceof Double) {
				stmt.setDouble(j, (Double) e);
			} else if (e instanceof Float) {
				stmt.setFloat(j, (Float) e);
			} else if (e instanceof Byte) {
				stmt.setByte(j, (Byte) e);
			} else if (e instanceof BigDecimal) {
				stmt.setBigDecimal(j, (BigDecimal) e);
			} else if (e instanceof Boolean) {
				stmt.setBoolean(j, (Boolean) e);
			} else if (e instanceof Date) {
				stmt.setDate(j, (Date) e);
			} else if (e instanceof Time) {
				stmt.setTime(j, (Time) e);
			} else if (e instanceof Timestamp) {
				stmt.setTimestamp(j, (Timestamp) e);
			} else {
				throw new IllegalArgumentException("インデックス " + i + " のパラメタはサポートしていないデータ型です。" + e.getClass().getName());
			}
		}
	}

}
