package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping()
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userHomepage(Authentication authentication, Model model) {
        model.addAttribute("user", userService.loadUserByUsername(authentication.getName()));
        return "user";
    }

    @GetMapping("admin")
    public String printUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "allUsers";
    }

    @GetMapping("admin/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.listRoles());
        return "addUser";
    }

    @PostMapping("admin/add")
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        return "deleteUser";
    }

    @DeleteMapping("admin/delete{id}")
    public String deleteUser(@ModelAttribute("user") User user, Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/edit")
    public String changeUser(@ModelAttribute("user") User user) {
        return "editUser";
    }

    @PatchMapping("admin/edit{id}")
    public String changeUser(@ModelAttribute("user") User user, Long id, String firstName, String lastName, String age, String email, String password) {
        userService.changeUser(id, firstName, lastName, age, email, password);
        return "redirect:/admin";
    }
}
