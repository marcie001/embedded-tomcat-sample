<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Employees</title>
<link rel="stylesheet" href="/css/app.css" />
</head>
<body>
  <h1>Employees</h1>
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>名前</th>
        <th>誕生日</th>
        <th>年収</th>
        <th>&nbsp;</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${ list }" var="e">
      <tr>
        <td><c:out value="${ e.id }" /></td>
        <td><c:out value="${ e.fullName }" /></td>
        <td><c:out value="${ e.birthDay.format(DateTimeFormatter.ISO_LOCAL_DATE) }" /></td>
        <td><fmt:formatNumber pattern="###,###円" value="${ e.salary }" /></td>
        <td><button class="update">修正</button> <button class="delete" data-href="/employees/${ e.id }">削除</button></td>
      </tr>
      <tr style="display: none">
        <td><c:out value="${ e.id }" /><input form="form-${ e.id }" type="hidden" name="id" value="<c:out value="${ e.id }" />" /></td>
        <td><input form="form-${ e.id }" type="text" name="fullName" value="<c:out value="${ e.fullName }" />" /></td>
        <td><input form="form-${ e.id }" type="date" name="birthDay" value="<c:out value="${ e.birthDay.format(DateTimeFormatter.ISO_LOCAL_DATE) }" />" /></td>
        <td><input form="form-${ e.id }" type="number" name="salary" value="${ e.salary }" /></td>
        <td><form id="form-${ e.id }" action="/employees" method="post"><button>登録</button></form></td>
      </tr>
      </c:forEach>
      <tr>
        <td>&nbsp;</td>
        <td><input form="form-new" type="text" name="fullName" /></td>
        <td><input form="form-new" type="date" name="birthDay" /></td>
        <td><input form="form-new" type="number" name="salary" /></td>
        <td><form id="form-new" action="/employees" method="post"><button>新規登録</button></form></td>
      </tr>
    </tbody>
  </table>
　　<script type="text/javascript" src="/js/app.js"></script>
</body>
</html>