<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=utf-8" %>
            <!-- Card: Payment Card -->
            <div class="card payment-card">
                <!-- Card Body -->
                <div class="card-body">
                    <!-- Payment Form -->
                    <form action="/transact/payment" method="POST">

<!-- Form Group -->
                    <div class="form-group mb-2">
                        <label for=""> Владелец счета/ Получатель</label>
                        <input type="text" name="beneficiary" class="form-control" placeholder="Введите владельца счета/ Получателя">
                    </div>
                    <!-- End Of Form Group -->

                    <!-- Form Group -->
                    <div class="form-group mb-2">
                        <label for=""> Номер счета получателя </label>
                        <input type="text" name="account_number"class="form-control" placeholder="Введите номер счета получателя">
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
                    <div class="form-group mb-2">
                        <label for=""> Ссылка</label>
                        <input type="text" name="reference" class="form-control" placeholder="Введите ссылку">
                    </div>
                    <!-- End Of Form Group -->

                    <!-- Form Group -->
                    <div class="form-group mb-2">
                        <label for=""> Введите сумму платежа</label>
                        <input type="text" name="payment_amount"class="form-control" placeholder="Введите сумму платежа">
                    </div>
                    <!-- End Of Form Group -->

                    <!-- Form Group -->
                    <div class="form-group mb-2">
                        <button id="" class="btn btn-md transact-btn">Заплатить</button>
                    </div>
                    <!-- End Of Form Group -->

                    </form>
                    <!-- End Of Payment Form -->
                </div>
                <!-- End Of Card Body -->

            </div>
            <!-- End Of Card: Payment Card -->