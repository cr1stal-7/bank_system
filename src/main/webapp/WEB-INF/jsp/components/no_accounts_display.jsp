    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
    <%@ page contentType="text/html;charset=utf-8" %>
    <!-- Container: No Accounts  -->
    <div class="container">
        <!-- Card: No Accounts -->
        <div class="card no-accounts-card">
            <!-- Card Body -->
            <div class="card-body">
                <!-- Card Title -->
                <h1 class="card-title">
                    <i class="fas fa-ban text-danger"></i> Нет зарегистрированных аккаунтов
                </h1>
                 <!-- End Of Card Title -->
                <hr>
                 <!-- Card Text -->
                 <div class="card-text">
                     В настоящее время у вас нет зарегистрированных учетных записей. <br>
                     Пожалуйста, нажмите ниже, чтобы зарегистрировать / добавить новую учетную запись.
                 </div>
                 <!-- End Of Card Text -->
                 <br>
                 <button class="btn btn-primary btn-lg shadow" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">
                    <i class="fa fa-credit-card"></i> Добавить новую учётную запись
                 </button>

            </div>
            <!-- End Of Card Body -->
        </div>
        <!-- End Of Card: No Accounts -->

    </div>
    <!-- End Of Container: No Accounts  -->