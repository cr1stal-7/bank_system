package com.demo_bank_v1.controllers;

import com.demo_bank_v1.helpers.GenAccountNumber;
import com.demo_bank_v1.models.User;
import com.demo_bank_v1.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/create_account")
    public String createAccount(@RequestParam("account_name")String accountName,
                                @RequestParam("account_type")String accountType,
                                RedirectAttributes redirectAttributes,
                                HttpSession session){

        // Проверка на пустую строку
        if(accountName.isEmpty() || accountType.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Поле не может быть пустым.");
            return "redirect:/app/dashboard";
        }

        // открытие сессии
        User user = (User)session.getAttribute("user");

        // генерация номера аккаунта
        int setAccountNumber = GenAccountNumber.generateAccountNumber();
        String bankAccountNumber = Integer.toString(setAccountNumber);

        // создание аккаунта
        accountRepository.createBankAccount(user.getUser_id(), bankAccountNumber, accountName, accountType );

        // сообщение об успешном создании аккаунта
        redirectAttributes.addFlashAttribute("success", "Аккаунт успешно создан");
        return "redirect:/app/dashboard";

    }
}
