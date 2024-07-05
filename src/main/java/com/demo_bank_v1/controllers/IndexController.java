package com.demo_bank_v1.controllers;

import com.demo_bank_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ModelAndView getIndex(){
        ModelAndView getIndexPage = new ModelAndView("index");
        getIndexPage.addObject("PageTitle", "Home");
        System.out.println("In Index Page Controller");
        return getIndexPage;
    }


    @GetMapping("/error")
    public ModelAndView getError(){
        ModelAndView getErrorPage = new ModelAndView("error");
        getErrorPage.addObject("PageTitle", "Errors");
        System.out.println("In Error Page Controller");
        return getErrorPage;
    }

    @GetMapping("/verify")
    public ModelAndView getVerify(@RequestParam("token")String token, @RequestParam("code") String code){
        ModelAndView getVerifyPage;

        // Получение токена из БД
        String dbToken = userRepository.checkToken(token);

        // Проверка токена на валидность
        if(dbToken == null){
            getVerifyPage  = new ModelAndView("error");
            getVerifyPage.addObject("error", "Сессия закрыта");
            return  getVerifyPage;
        }

        // Обновление и верификация аккаунта
        userRepository.verifyAccount(token, Integer.parseInt(code));

        getVerifyPage = new ModelAndView("login");

        getVerifyPage.addObject("success", "Аккаунт успешно подтвержден.");

        System.out.println("In Verify Account Controller");
        return getVerifyPage;
    }
}
