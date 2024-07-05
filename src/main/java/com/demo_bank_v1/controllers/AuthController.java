package com.demo_bank_v1.controllers;

import com.demo_bank_v1.helpers.Token;
import com.demo_bank_v1.models.User;
import com.demo_bank_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public ModelAndView getLogin(){
        System.out.println("In Login Page Controller");
        ModelAndView getLoginPage = new ModelAndView("login");
        // генерация токена
        String token = Token.generateToken();
        // добавление токена во View:
        getLoginPage.addObject("token", token);
        getLoginPage.addObject("PageTitle", "Login");
        return getLoginPage;
    }

    @PostMapping("/login")
    public String login(@RequestParam("email")String email,
                        @RequestParam("password") String password,
                        @RequestParam("_token")String token,
                        Model model,
                        HttpSession session){

        // валидация данных
        if(email.isEmpty() || email == null || password.isEmpty() || password == null){
            model.addAttribute("error", "Имя пользователя и пароль не могут быть пустыми");
            return "login";
        }




        // Проверка существования почты
        String getEmailInDatabase = userRepository.getUserEmail(email);


        if(getEmailInDatabase != null ){
            // Получение пароля из БД
            String getPasswordInDatabase = userRepository.getUserPassword(getEmailInDatabase);

            // Проверка пароля
            if(!BCrypt.checkpw(password, getPasswordInDatabase)){
                model.addAttribute("error", "Неверное имя пользователя или пароль");
                return "login";
            }
        }else{
            model.addAttribute("error", "Произошла непредвиденная ошибка.");
            return "error";
        }

        // Проверка верификации аккаунта
        int verified = userRepository.isVerified(getEmailInDatabase);

        if (verified != 1){
            String msg = "Аккаунт ещё не подтвержден, проверьте почту и подтвердите аккаунт.";
            model.addAttribute("error", msg);
            return "login";
        }

        // Регистрация пользователя в системе
        User user = userRepository.getUserDetails(getEmailInDatabase);

        // установка атрибутов открытой сессии
        session.setAttribute("user", user);
        session.setAttribute("token", token);
        session.setAttribute("authenticated", true);

        return "redirect:/app/dashboard";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes){
        session.invalidate();
        redirectAttributes.addFlashAttribute("logged_out", "Вы успешно вышли.");
        return "redirect:/login";
    }

}
