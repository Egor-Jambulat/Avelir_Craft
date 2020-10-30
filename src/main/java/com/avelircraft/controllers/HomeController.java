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
        model.addAttribute("comments", news.get().getComments());

        User user = (User) session.getAttribute("user");
        boolean deleteAccess = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("owner|fakeowner|admin|moder"));
        model.addAttribute("delete_access", deleteAccess);
        model.addAttribute("user", user);
        return "news";
    }

    @RequestMapping(path = {"/support.html", "/support"})
    public String supportPage(Model model) {
        return "support";
    }

    @RequestMapping(path = {"/lk.html", "/lk"})
    public String lkPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        boolean panelAccess = user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("owner|fakeowner|admin"));
        model.addAttribute("panel_access", panelAccess);
        model.addAttribute("user", user);
        return "lk";
    }

    @RequestMapping(path = {"/adminpanel.html", "/adminpanel"})
    public String adminPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean pageAccess = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("admin"));
        if (pageAccess)
            return "adminpanel";
        else
            return "error";
    }

    @RequestMapping(path = {"/donate.html", "/donate"})
    public String donatePage(Model model) { return "donate"; }

    @RequestMapping(path = {"/guid.html", "/guid"})
    public String guidPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean deleteAccess = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("owner|fakeowner|admin|moder|helper"));
        model.addAttribute("delete_access", deleteAccess);
        return "guid"; }

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
