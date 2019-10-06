package com.naranov.spring_boot_v1.controller;

import com.naranov.spring_boot_v1.model.User;
import com.naranov.spring_boot_v1.model.UserProfile;
import com.naranov.spring_boot_v1.repository.UserProfileRepository;
import com.naranov.spring_boot_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path="/")
@SessionAttributes("roles")
public class GreetingController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
    public String listUsers(ModelMap model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("loggedinuser", getPrincipal());
        return "userlist";
    }

    @RequestMapping(value = { "/newUser" }, method = RequestMethod.GET)
    public String newUser(ModelMap model) {
        User user = new User();

        List<UserProfile> roles = userProfileRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("edit", false);
        model.addAttribute("allRoles", roles);
        model.addAttribute("loggedinuser", getPrincipal());
        return "registration";
    }

    @RequestMapping(value = { "/newUser" }, method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult result,
                           ModelMap model) {

        if (result.hasErrors()) {
            List<UserProfile> roles = userProfileRepository.findAll();

            model.addAttribute("allRoles", roles);
            model.addAttribute("user", user);
            model.addAttribute("edit", false);
            return "registration";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        model.addAttribute("success", "User " + user.getName() + " registered successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }

    @GetMapping("/deleteUser-{id}")
    public String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);

        return "redirect:/list";
    }

    @GetMapping("/editUser-{id}")
    public String showFormEditUser(@PathVariable int id, Model model) throws Exception {
        User user = userRepository.findById(id).orElseThrow(Exception::new);
        List<UserProfile> roles = userProfileRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", roles);
        model.addAttribute("edit", true);
        return "registration"; // <- here changed
    }

    @PostMapping("/editUser-{id}")
    public String editUser(@Valid User user, BindingResult result, Model model, @PathVariable int id){

        if (result.hasErrors()){
            List<UserProfile> roles = userProfileRepository.findAll();

            model.addAttribute("user", user);
            model.addAttribute("allRoles", roles);
            model.addAttribute("edit", true);
            return "registration"; // - here last
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        model.addAttribute("success", "User " + user.getName() + " "+ user.getLogin() + " updated successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }

    private String getPrincipal(){
        String userLogin = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userLogin = ((UserDetails)principal).getUsername();
        } else {
            userLogin = principal.toString();
        }
        return userLogin;
    }

    @GetMapping("/logout")
    public String logoutPage (){
        return "redirect:/login?logout";
    }

    @GetMapping ("/Access_Denied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "accessDenied";
    }
}