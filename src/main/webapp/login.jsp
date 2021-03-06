<%--
  Created by IntelliJ IDEA.
  User: emzio
  Date: 16.02.2022
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>

<%@ include file="header.jsp" %>

<section class="dashboard-section">
    <div class="container pt-4 pb-4">
        <div class="border-dashed view-height">
            <div class="container w-25">
                <form action="" method="post" class="padding-small text-center">
                    <h1 class="text-color-darker">Logowanie</h1>

                    <c:if test="${incorrectLoginData== 'true'}">
                        <h3 class="text-color-lighter">Błędne dane</h3>
                    </c:if>

                    <div class="form-group">
                        <input type="text" class="form-control" id="email" name="email" placeholder="podaj adres email">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" id="password" name="password" placeholder="podaj hasło">
                    </div>
                    <button class="btn btn-color rounded-0" type="submit">Zaloguj</button>
                </form>
            </div>
        </div>
    </div>
</section>

<%@ include file="footer.jsp" %>

</html>
