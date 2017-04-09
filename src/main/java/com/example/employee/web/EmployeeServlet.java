package com.example.employee.web;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.employee.Employee;
import com.example.employee.EmployeeRepository;

/**
 * todo リストのサーブレット
 * 
 * @author marcie
 *
 */
@WebServlet(urlPatterns = { "/employees/*" }, name = "EmployeeServlet")
public class EmployeeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger.getLogger(EmployeeServlet.class.getName());

	/**
	 * 従業員リストを表示
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		try {
			EmployeeRepository repository = new EmployeeRepository();
			List<Employee> employees = repository.findAll();
			req.setAttribute("list", employees);
		} catch (SQLException | NamingException e) {
			logger.log(Level.SEVERE, "DB アクセス時のエラー", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		req.getRequestDispatcher("/jsp/employees.jsp").forward(req, resp);
	}

	/**
	 * 従業員を登録
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		Employee employee = new Employee(req.getParameter("id"), req.getParameter("fullName"),
				LocalDate.parse(req.getParameter("birthDay")), Integer.parseInt(req.getParameter("salary")));

		try {
			EmployeeRepository repository = new EmployeeRepository();
			repository.save(employee);
		} catch (SQLException | NamingException e) {
			logger.log(Level.SEVERE, "DB アクセス時のエラー", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		resp.sendRedirect("/employees");
	}

	/**
	 * PUT と DELETE は {@link HttpServletRequest#getParameter(String)}
	 * で値を取得できない。<br>
	 * 実装するなら、 {@link HttpServletRequest#getInputStream()} から読み込み自分でパースする。
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPut(req, resp);
	}

	/**
	 * todo を削除。<br>
	 * PUT と DELETE は {@link HttpServletRequest#getParameter(String)}
	 * で値を取得できない。<br>
	 * 実装するなら、 {@link HttpServletRequest#getInputStream()} から読み込み自分でパースする。
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getPathInfo().substring(1);

		try {
			EmployeeRepository repository = new EmployeeRepository();
			repository.delete(id);
		} catch (SQLException | NamingException e) {
			logger.log(Level.SEVERE, "DB アクセス時のエラー", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		resp.sendError(HttpServletResponse.SC_OK);
	}

}
