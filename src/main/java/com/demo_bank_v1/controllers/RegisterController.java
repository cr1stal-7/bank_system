package com.demo_bank_v1.controllers;

import com.demo_bank_v1.helpers.HTML;
import com.demo_bank_v1.helpers.Token;
import com.demo_bank_v1.mailMessenger.MailMessenger;
import com.demo_bank_v1.models.User;
import com.demo_bank_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Random;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public ModelAndView getRegister(){
        ModelAndView getRegisterPage = new ModelAndView("register");
        System.out.println("In Register Page Controller");
        getRegisterPage.addObject("PageTitle", "Register");
        return getRegisterPage;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute("registerUser")User user,
                                 BindingResult result,
                                 @RequestParam("first_name") String first_name,
                                 @RequestParam("last_name") String last_name,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirm_password") String confirm_password) throws MessagingException {

        ModelAndView registrationPage = new ModelAndView("register");

        // Проверка поля подтверждения
        if(result.hasErrors() && confirm_password.isEmpty()){
            registrationPage.addObject("confirm_pass", "Поле обязательно");
            return registrationPage;
        }

        // проверка паролей на совпадение
        if(!password.equals(confirm_password)){
            registrationPage.addObject("passwordMisMatch", "Пароли не совпадают");
            return registrationPage;
        }

        // получение токена
        String token = Token.generateToken();

        // рандомный код
        Random rand = new Random();
        int bound = 123;
        int code = bound * rand.nextInt(bound);


        String emailBody = HTML.htmlEmailTemplate(token, code);
        // bcrypt включает соль прямо в хеш
        // В процессе проверки, пароль подаётся на вход функции вместе с исходным хешем,
        // и bcrypt автоматически извлекает из него соль для сравнения.
        String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());

        // регистрация пользователя
        userRepository.registerUser(first_name, last_name, email, hashed_password, token, code);

        // отправка сообщения-подтверждения
        MailMessenger.htmlEmailMessenger("#input_mail", email, "Verify Account", emailBody);

        // возвращение к странице регистрации
        String successMessage = "Аккаунт успешно зарегистрирован, Проверьте почту и подтвердите аккаунт!";
        registrationPage.addObject("success", successMessage);
        return registrationPage;
    }


}
