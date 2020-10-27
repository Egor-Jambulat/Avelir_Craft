package com.avelircraft.controllers;

import com.avelircraft.models.Comment;
import com.avelircraft.models.Image;
import com.avelircraft.models.News;
import com.avelircraft.models.User;
import com.avelircraft.services.CommentsDataService;
import com.avelircraft.services.ImagesDataService;
import com.avelircraft.services.NewsDataService;
import com.avelircraft.services.UsersDataService;
import jdk.jfr.ContentType;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.expression.Strings;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = "/upload")
public class UploadController {

    File dir;

    @Autowired
    UsersDataService usersDataService;

    @Autowired
    ImagesDataService imagesDataService;

    @Autowired
    NewsDataService newsDataService;

    @Autowired
    CommentsDataService commentsDataService;

    public UploadController() {
        dir = new File("C:/Server_files");
        if (!dir.exists()) dir.mkdir();
    }

    @RequestMapping(path = "/image", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@RequestParam(required = false) boolean profile,
                                           @RequestParam("id") String imgInfo) {
        File foundFile = Arrays.stream(Objects.requireNonNull(dir
                .listFiles()))
                .filter(f -> f.getName().equals(imgInfo))
                .findFirst()
                .orElse(null);
        Image img = new Image();
        try {
            img.setImg(new FileInputStream(foundFile).readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
        String[] info = imgInfo.split("\\.");
        img.setType("image/" + info[1]);

        return ResponseEntity.ok()
                .contentLength(img.getImg().length)
                .contentType(MediaType.parseMediaType(img.getType()))
                .body(img.getImg());
    }

    @RequestMapping(path = "/user/icon", method = RequestMethod.POST)
    public String saveProfilePic(@RequestParam("image") MultipartFile image, HttpSession session) {
        if (!image.isEmpty()) {
            if (!image.getContentType().startsWith("image"))
                return "error";
            User user = (User) session.getAttribute("user");
            Image img;
            try {
                img = new Image(user.getId(), image.getContentType(), image.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
            imagesDataService.save(img);
            usersDataService.addIcon(user);
        }
        return "redirect:/lk";
    }

    @RequestMapping(path = "/user/icon", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getProfilePic(@RequestParam("id") String id) {
        Image img;
        try {
            img = imagesDataService.findByUserId(Integer.parseInt(id)).get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentLength(img.getImg().length)
                .contentType(MediaType.parseMediaType(img.getType()))
                .body(img.getImg());
    }

    @RequestMapping(path = "/news", method = RequestMethod.POST)
    public String createNews(@RequestParam(defaultValue = "") String header,
                             @RequestParam("description") String description,
                             @RequestParam("message") String message,
                             @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                // ---------------- Сохраняем изображение ----------------
                Long imgId = System.currentTimeMillis();
                byte[] bytes = image.getBytes();
                String[] type = image.getContentType().split("/");
                if (!type[0].equals("image"))
                    throw new Exception("Файл не является изображением");
                String imgName = imgId.toString() + "." + type[1];
                File file = new File(dir, imgName);
                file.createNewFile();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(file));
                stream.write(bytes);
                stream.close();
                // ----------------- Сохраняем новость -----------------
                News news = new News(header, description, message, imgName);
                newsDataService.save(news);
                return "redirect:/";
            } catch (Exception e) {
                System.out.println("createNews " + e);
                return "error";
            }
        } else {
            News news = new News(header, description, message);
            newsDataService.save(news);
            return "redirect:/";
        }
    }

    @RequestMapping(path = "/news/comment", method = RequestMethod.POST)
    public RedirectView commentNews(@RequestParam("news_id") Integer nId,
                              @RequestParam("message") String message,
                              HttpSession session,
                              RedirectAttributes attr) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return new RedirectView("/error");
        Optional<News> news = newsDataService.findById(nId);
        if (news.isEmpty())
            return new RedirectView("/error");
        Comment comment = new Comment(user, news.get(), message);
        if (commentsDataService.save(comment) == null)
            return new RedirectView("/error");
        attr.addAttribute("id", nId);
        return new RedirectView("/news");
    }
}