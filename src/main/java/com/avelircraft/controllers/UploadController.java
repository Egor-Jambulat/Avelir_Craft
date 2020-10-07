package com.avelircraft.controllers;

import com.avelircraft.models.Image;
import com.avelircraft.models.News;
import com.avelircraft.services.ImagesDataService;
import com.avelircraft.services.NewsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping(path = "/upload")
public class UploadController {

    @Autowired
    ImagesDataService imagesDataService;

    @Autowired
    NewsDataService newsDataService;

    @RequestMapping(path = "/image", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@RequestParam("id") long id) {
        Optional<Image> img = imagesDataService.findById(id);

        return ResponseEntity.ok()
                .contentLength(img.get().getImg().length)
                .contentType(MediaType.parseMediaType(img.get().getType()))
                .body(img.get().getImg());
    }

    @RequestMapping(path = "/news", method = RequestMethod.POST)
    public String createNews(@RequestParam(defaultValue = "") String header,
                             @RequestParam("description") String description,
                             @RequestParam("message") String message,
                             @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                // ---------------- Создаем изображение ----------------
                Long imgId = System.currentTimeMillis();
                Image img = new Image(imgId, image.getContentType(), image.getBytes());
                // ----------------- Сохраняем новость -----------------
                News news = new News(header, description, message, imgId, img);
                newsDataService.save(news);
                return "redirect:/";
            } catch (Exception e) {
                return "error";
            }
        } else {
            News news = new News(header, description, message);
            newsDataService.save(news);
            return "redirect:/";
        }
    }
}