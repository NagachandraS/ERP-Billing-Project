package com.example.oilbilling.controller;


import com.example.oilbilling.model.Customers;
import com.example.oilbilling.model.UserSessionStatusType;
import com.example.oilbilling.model.Users;
import com.example.oilbilling.services.UserService;
import com.example.oilbilling.services.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final UserSessionService userSessionService;


    private final UserService userService;

    public HomeController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService=userSessionService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @PostMapping("/home")
    public String validateCred(@ModelAttribute Users users, Model model, HttpSession session, HttpServletRequest request)
    {
        model.addAttribute("users", users);
        if(userService.ValidateAdminUser(users))
        {

            session.setAttribute(
                    "loggedInUser",
                    users.getUserName()
            );
            userSessionService.createSessionLog(
                    users.getUserName(),
                    session,
                    request
            );
            return "home";
        }

      return "redirect:/?error";
    }

    @GetMapping("/home")
    public String goToHomePage(Model model)
    {
        return "home";
    }

    @PostMapping("/logout")
    @ResponseBody
    public String goToLoginPage(HttpSession session)
    {

        userSessionService.updateLogoutSession(session.getId(),UserSessionStatusType.ACTIVE);
        session.invalidate();
        return "Logout success";
    }
}




