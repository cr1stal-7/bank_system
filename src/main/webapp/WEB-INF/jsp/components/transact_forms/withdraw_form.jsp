<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=utf-8" %>
 <!-- Card: Withdraw Card -->
            <div class="card withdraw-card">
                <!-- Card Body -->
                <div class="card-body">

                <!-- Deposit Form -->
                <form action="/transact/withdraw" method="POST" class="deposit-form">
                    <!-- Form Group -->
                    <div class="form-group mb-2">
                        <label for=""> Введите сумму для вывода средств</label>
                        <input type="text" name="withdrawal_amount"class="form-control" placeholder="Введите сумму для вывода средств">
                    </div>
                    <!-- End Of Form Group -->

                    <!-- Form Group -->
                    <div class="form-group">
                        <label for="">Выберите аккаунт</label>
                        <!-- Select Account Option -->
                        <select name="account_id" class="form-control" id="">
                            <option value="">-- Выберите аккаунт --</option>
                            <c:if test="${userAccounts != null}">
                                <c:forEach items="${userAccounts}" var="selectAccount">
                                    <option value="${selectAccount.account_id}">${selectAccount.account_name}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                        <!-- End Select Account Option -->
                    </div>
                    <!-- End Of Group -->


                    <!-- Form Group -->
                    <div class="form-group my-2">
                        <button id="" class="btn btn-md transact-btn">Вывод</button>
                    </div>
                    <!-- End Of Form Group -->
                </form>
                <!-- End Withdrawal Form  -->
                </div>
                <!-- End Of Card Body -->
            </div>
            <!-- End Of Card: Withdraw Card -->