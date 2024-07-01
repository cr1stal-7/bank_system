    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
    <%@ page contentType="text/html;charset=utf-8" %>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="css/fontawesome/css/all.css">
    <link rel="stylesheet" href="css/default.css">
    <header class="main-page-header mb-3">
        <div class="container d-flex align-items-center">
            <div class="company-name">
                QBank
            </div>

            <nav class="navigation">
                <li><a href="/app/dashboard">Главная страница</a></li>
                <li><a href="/app/payment_history">История платежей</a></li>
                <li><a href="/app/transact_history">История транзакций</a></li>
            </nav>

            <div class="display-name ms-auto text-white">
                <i class="fa fa-circle text-success me-2"></i> Добро пожаловать, <span>${user.first_name}  ${user.last_name}</span>
            </div>

            <a href="/logout" class="btn btn-sm text-white ms-2">
                <i class="fas fa-sign-out-alt " aria-hidden="true"></i> Выйти
            </a>
        </div>

    </header>