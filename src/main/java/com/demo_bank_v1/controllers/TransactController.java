package com.demo_bank_v1.controllers;

import com.demo_bank_v1.models.User;
import com.demo_bank_v1.repository.AccountRepository;
import com.demo_bank_v1.repository.PaymentRepository;
import com.demo_bank_v1.repository.TransactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TransactRepository transactRepository;

    User user;
    double currentBalance;
    double newBalance;
    LocalDateTime currentDateTime = LocalDateTime.now();

    @PostMapping("/deposit")
    public String deposit(@RequestParam("deposit_amount")String depositAmount,
                          @RequestParam("account_id") String accountID,
                          HttpSession session,
                          RedirectAttributes redirectAttributes){

        //Проверка на пустые строки:
        if(depositAmount.isEmpty() || accountID.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Сумма депозита или номер счета не могут быть пустыми");
            return "redirect:/app/dashboard";
        }
        //Получить вошедшего пользователя:
        user = (User)session.getAttribute("user");

        //Получить текущий баланс счета:
        int acc_id = Integer.parseInt(accountID);

        double depositAmountValue = Double.parseDouble(depositAmount);

        //Проверка, является ли сумма депозита равной 0:
        if(depositAmountValue == 0){
            redirectAttributes.addFlashAttribute("error", "Сумма депозита не может быть равной 0");
            return "redirect:/app/dashboard";
        }

        //Обновление баланса:
        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), acc_id);

        newBalance = currentBalance + depositAmountValue;

        //Обновление счета:
        accountRepository.changeAccountBalanceById(newBalance, acc_id);

        //Запись успешной транзакции:
        transactRepository.logTransaction(acc_id, "deposit", depositAmountValue, "online", "success", "Deposit Transaction Successful",currentDateTime);

        redirectAttributes.addFlashAttribute("success", "Amount Deposited Successfully");
        return "redirect:/app/dashboard";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("transfer_from") String transfer_from,
                           @RequestParam("transfer_to") String transfer_to,
                           @RequestParam("transfer_amount")String transfer_amount,
                           HttpSession session,
                           RedirectAttributes redirectAttributes){
        //Установить значение сообщения об ошибке:
        String errorMessage;

        //Проверка на пустые поля:
        if(transfer_from.isEmpty() || transfer_to.isEmpty() || transfer_amount.isEmpty()){
             errorMessage = "Номера счетов для перевода и сумма не могут быть пустыми!";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Конвертация переменных:
        int transferFromId = Integer.parseInt(transfer_from);
        int transferToId = Integer.parseInt(transfer_to);
        double transferAmount = Double.parseDouble(transfer_amount);

        //Проверка на перевод на тот же счет:
        if(transferFromId == transferToId){
            errorMessage = "Нельзя перевести деньги на тот же счет. Пожалуйста, выберите другой счет для перевода.";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Проверка на значение 0
        if(transferAmount == 0){
            errorMessage = "Нельзя перевести сумму равную 0. Пожалуйста, введите значение больше 0.";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Получить текущего пользователя:
        user = (User)session.getAttribute("user");

        //Получить текущий баланс счета, с которого происходит перевод:
        double currentBalanceOfAccountTransferringFrom  = accountRepository.getAccountBalance(user.getUser_id(), transferFromId);

        //Проверка на достаточность средств для перевода:
        if(currentBalanceOfAccountTransferringFrom < transferAmount){
            errorMessage = "У вас недостаточно средств для выполнения этого перевода!";
            //Запись неудачной транзакции:
            transactRepository.logTransaction(transferFromId, "Transfer", transferAmount, "online", "failed", "Insufficient Funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        double  currentBalanceOfAccountTransferringTo = accountRepository.getAccountBalance(user.getUser_id(), transferToId);

        //Установить новый баланс
        double newBalanceOfAccountTransferringFrom = currentBalanceOfAccountTransferringFrom - transferAmount;

        double newBalanceOfAccountTransferringTo = currentBalanceOfAccountTransferringTo + transferAmount;

        //Изменить баланс счета, с которого происходит перевод:
        accountRepository.changeAccountBalanceById( newBalanceOfAccountTransferringFrom, transferFromId);

        //Изменить баланс счета, на который происходит перевод:
        accountRepository.changeAccountBalanceById(newBalanceOfAccountTransferringTo, transferToId);

        //Запись успешной транзакции:
        transactRepository.logTransaction(transferFromId, "Transfer", transferAmount, "online", "success", "Transfer Transaction Successful",currentDateTime);

        String successMessage = "Amount Transferred Successfully!";
        redirectAttributes.addFlashAttribute("success", successMessage);
        return "redirect:/app/dashboard";
    }


    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("withdrawal_amount")String withdrawalAmount,
                           @RequestParam("account_id")String accountID,
                           HttpSession session,
                           RedirectAttributes redirectAttributes){

        String errorMessage;
        String successMessage;

        //Проверка на пустые значения:
        if(withdrawalAmount.isEmpty() || accountID.isEmpty()){
            errorMessage = "Сумма снятия и номер счета не могут быть пустыми ";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }
        //Конвертация переменных:
        double withdrawal_amount = Double.parseDouble(withdrawalAmount);
        int account_id = Integer.parseInt(accountID);

        //Проверка на нулевое значение:
        if (withdrawal_amount == 0){
            errorMessage = "Сумма снятия не может быть равна 0, пожалуйста, введите значение больше 0";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Получить авторизованного пользователя:
        user = (User) session.getAttribute("user");

        //Получить текущий баланс:
        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), account_id);

        //Проверка на достаточность средств для снаятия:
        if(currentBalance < withdrawal_amount){
            errorMessage = "У вас недостаточно средств для выполнения этого снятия!";
            // Log Failed Transaction:
            transactRepository.logTransaction(account_id, "Withdrawal", withdrawal_amount, "online", "failed", "Insufficient Funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Установить новый баланс
        newBalance = currentBalance - withdrawal_amount;

        //Обновить баланс счета
        accountRepository.changeAccountBalanceById(newBalance, account_id);

        //Запись успешной транзакции
        transactRepository.logTransaction(account_id, "Withdrawal", withdrawal_amount, "online", "success", "Withdrawal Transaction Successful",currentDateTime);

        successMessage = "Снятие прошло успешно!";
        redirectAttributes.addFlashAttribute("success", successMessage);
        return "redirect:/app/dashboard";
    }

    @PostMapping("/payment")
    public String payment(@RequestParam("beneficiary")String beneficiary,
                          @RequestParam("account_number")String account_number,
                          @RequestParam("account_id")String account_id,
                          @RequestParam("reference")String reference,
                          @RequestParam("payment_amount")String payment_amount,
                          HttpSession session,
                          RedirectAttributes redirectAttributes){

        String errorMessage;
        String successMessage;

        //Проверка на пустые значения:
        if(beneficiary.isEmpty() || account_number.isEmpty() || account_id.isEmpty() || payment_amount.isEmpty()){
            errorMessage = "Получатель, Номер счета, Номер счета для оплаты и Сумма платежа не могут быть пустыми!";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Конвертация переменных:
        int accountID = Integer.parseInt(account_id);
        double paymentAmount = Double.parseDouble(payment_amount);

        //Проверка на нулевые значения суммы платежа:
        if(paymentAmount == 0){
            errorMessage = "Сумма платежа не может быть равна 0, введите значение больше 0 ";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Получить авторизованного пользователя:
        user = (User) session.getAttribute("user");

        //Получить текущий баланс:
        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), accountID);

        //Проверка что сумма платежа не больше текущего баланса
        if(currentBalance < paymentAmount){
            errorMessage = "У вас недостаточно средств для выполнения этого платежа";
            String reasonCode = "Не удалось обработать платеж из-за недостатка средств!";
            paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "failed", reasonCode, currentDateTime);
            // Log Failed Transaction:
            transactRepository.logTransaction(accountID, "Payment", paymentAmount, "online", "failed", "Insufficient Funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/app/dashboard";
        }

        //Установить новый баланс для счета оплаты:
        newBalance = currentBalance - paymentAmount;

        //Совершить платеж:
        String reasonCode = "Платеж успешно обработан!";
        paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "success", reasonCode, currentDateTime);

        //Обновить баланс счета оплаты:
        accountRepository.changeAccountBalanceById(newBalance, accountID);

        //Запись успешной транзакции:
        transactRepository.logTransaction(accountID, "Payment", paymentAmount, "online", "success", "Payment Transaction Successful",currentDateTime);

        successMessage = reasonCode;
        redirectAttributes.addFlashAttribute("success", successMessage);
        return "redirect:/app/dashboard";
    }

}
