package com.avelircraft.controllers;

import com.avelircraft.models.Comment;
import com.avelircraft.models.News;
import com.avelircraft.models.Role;
import com.avelircraft.models.User;
import com.avelircraft.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Optional;

@Controller
@RequestMapping(path = "/act")
public class ActionController {

    File dir;

    @Autowired
    UsersDataService usersDataService;

    @Autowired
    NewsDataService newsDataService;

    @Autowired
    CommentsDataService commentsDataService;

    @Autowired
    GuidesDataService guidesDataService;

    public ActionController() {
        dir = new File("/Server_files");
        if (!dir.exists()) dir.mkdir();
    }

    @RequestMapping(path = "/news/comment", method = RequestMethod.POST)
    public RedirectView commentNews(@RequestParam("news_id") Integer nId,
                                    @RequestParam("message") String message,
                                    HttpSession session,
                                    RedirectAttributes attr) {
        if (nId == 0)
            return new RedirectView("/");
        User user = (User) session.getAttribute("user");
        if (user == null)
            return new RedirectView("/error");
        Optional<News> news = newsDataService.findById(nId);
        if (news.isEmpty())
            return new RedirectView("/error");
        Comment comment = new Comment(user, news.get(), message);
        System.out.println(comment);
        if (commentsDataService.save(comment) == null)
            return new RedirectView("/error");
        //System.out.println(comment);
        attr.addAttribute("id", nId);
        return new RedirectView("/news");
    }

    @RequestMapping(path = "/news/delete", method = RequestMethod.POST)
    public String deleteNews(@RequestParam("news_id") Integer nId,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean deleteAccess = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("owner|fakeowner|admin|moder"));
        if (!deleteAccess)
            return "error";
        newsDataService.deleteById(nId);
        return "redirect:/";
    }

    @RequestMapping(path = "/guide/delete", method = RequestMethod.POST)
    public String deleteGuide(@RequestParam("guide_id") Integer gId,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean deleteAccess = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("owner|fakeowner|admin|moder"));
        if (!deleteAccess)
            return "error";
        guidesDataService.deleteById(gId);
        return "redirect:/guidmenu";
    }

    @RequestMapping(path = "/news/comment/delete", method = RequestMethod.POST)
    public RedirectView deleteComment(@RequestParam("news_id") Integer nId,
                                      @RequestParam("comment_id") Long cId,
                                      HttpSession session,
                                      RedirectAttributes attr) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return new RedirectView("/error");
        Optional<Comment> com = commentsDataService.findById(cId);
        boolean deleteAccess = com.isPresent() && user.getId() == com.get().getUser().getId() || user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("owner|fakeowner|admin|moder"));
        if (!deleteAccess)
            return new RedirectView("/error");
        commentsDataService.delete(com.get());
        attr.addAttribute("id", nId);
        return new RedirectView("/news");
    }

    @RequestMapping(path = "/admin/privilege", method = RequestMethod.POST)
    public String grantPrivilege(@RequestParam("name") String username,
                                 @RequestParam("priveleg") String privilege,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean access = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("admin"));
        if (!access)
            return "error";
        Optional<User> grantUser = usersDataService.findByName(username.toLowerCase());
        if (grantUser.isEmpty())
            return "error";
        boolean has = grantUser.get().getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches(privilege));
        if (has)
            return "error";
        String uuid = username + " " + privilege + " " + System.currentTimeMillis();
        Role role = new Role(uuid, grantUser.get(), privilege);
        grantUser.get().setRole(role);
        usersDataService.update(grantUser.get());
        return "redirect:/adminpanel";
    }

    @RequestMapping(path = "/admin/delete_user", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("name") String username,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean access = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getRole()
                        .matches("admin"));
        if (!access)
            return "error";
        Optional<User> deleteUser = usersDataService.findByName(username.toLowerCase());
        if (deleteUser.isEmpty())
            return "error";
        usersDataService.delete(deleteUser.get());
        return "redirect:/adminpanel";
    }
}
