package com.avelircraft.controllers;

import com.avelircraft.models.Role;
import com.avelircraft.models.User;
import com.avelircraft.services.RolesDataService;
import com.avelircraft.services.UsersDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = {"/login", "/login.html"})
public class EntryController {

    @Autowired
    UsersDataService usersDataService;

    @Autowired
    RolesDataService rolesDataService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String loginPage(Model model) { return "login"; }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public String logMe(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("remember_me") Optional<Object> re,
                        HttpSession session) {
        Optional<User> user = usersDataService.findByNameAndPassword(username, password);
        if (user.isEmpty())
            return "Зарегестрируйтесь на сервере";
        //List<Role> roles = rolesDataService.findByUsername(user.get().getUsername());
        //List<Role> roles = user.get().getRoles();
        session.setAttribute("user", user);
        session.setAttribute("log_date", System.currentTimeMillis()); // По истечении суток обновлять объект user
        //session.setAttribute("role", roles);

        return "redirect:/";
    }
}