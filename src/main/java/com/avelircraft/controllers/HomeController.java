package com.avelircraft.controllers;

import com.avelircraft.models.News;
import com.avelircraft.models.Role;
import com.avelircraft.models.User;
import com.avelircraft.services.NewsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(path = "/")
public class HomeController {

    @Autowired
    NewsDataService newsDataService;

    @RequestMapping(path = {"/", "/index.html", "/index"})
    public String indexPage(Model model) {
        List<News> news =  Stream.concat(newsDataService.findAll()
                .stream(), Stream.generate(News::new))
                .limit(10)
                .collect(Collectors.toList());
        model.addAttribute("news", news);
        return "index";
    }

    @RequestMapping(path = {"/news.html", "/news"})
    public String newsPage(@RequestParam Integer id, Model model, HttpSession session) {
        Optional<News> news = newsDataService.findById(id);
        if(news.isEmpty())
            news = Optional.of(new News());
        else
            newsDataService.incrementViews(news.get());
        model.addAttribute("news", news.get());


        User user = (User) session.getAttribute("user");
        List<Role> roles = new ArrayList<>();
        if (user != null)
            roles.addAll(user.getRoles());
        model.addAttribute("roles", roles);
        return "news";
    }

    @RequestMapping(path = {"/support.html", "/support"})
    public String supportPage(Model model) {
        return "support";
    }

    @RequestMapping(path = {"/lk.html", "/lk"})
    public String lkPage(Model model) {
        return "lk";
    }

    @RequestMapping(path = {"/adminpanel.html", "/adminpanel"})
    public String adminPage(Model model) { return "adminpanel"; }

    @RequestMapping(path = {"/donate.html", "/donate"})
    public String donatePage(Model model) { return "donate"; }

    @RequestMapping(path = {"/guid.html", "/guid"})
    public String guidPage(Model model) { return "guid"; }

    @RequestMapping(path = {"/guidmenu.html", "/guidmenu"})
    public String guidmenuPage(Model model) { return "guidmenu"; }

    @RequestMapping(path = {"/nolog.html", "/nolog"})
    public String nologPage(Model model) { return "nolog"; }

    @RequestMapping(path = {"/openticket.html", "/openticket"})
    public String openticketPage(Model model) { return "openticket"; }

    @RequestMapping(path = {"/polssogl.html", "/polssogl"})
    public String polssoglPage(Model model) { return "polssogl"; }

    @RequestMapping(path = {"/register.html", "/register"})
    public String registerPage(Model model) { return "register"; }
}
