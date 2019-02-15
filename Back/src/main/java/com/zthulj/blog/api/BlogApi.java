package com.zthulj.blog.api;

import com.google.common.base.Charsets;
import com.zthulj.blog.dto.Article;
import com.zthulj.blog.dto.Card;
import com.zthulj.blog.dto.Image;
import com.zthulj.blog.exception.BlogException;
import com.zthulj.blog.referential.SecurityConfig;
import com.zthulj.blog.service.BlogService;
import com.zthulj.blog.service.StorageService;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

@RestController
public class BlogApi {

    private Logger logger = LoggerFactory.getLogger(BlogApi.class);

    @Autowired
    BlogService blogService;

    @Autowired
    StorageService storageService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = SecurityConfig.PUBLIC_API + "/blog/{link}")
    public Article getArticle(@PathVariable("link") String link) {
        return blogService.getArticleByLink(link);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = SecurityConfig.SECURED_API + "/blog/full/{link}")
    public Article getFullArticle(@PathVariable("link") String link) {
        return blogService.getFullArticleByLink(link);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = SecurityConfig.PUBLIC_API + "/blog/search/{keywords}/{page}")
    public Page<Card> search(@PathVariable("keywords") String keywords, @PathVariable("page") int page) {
        return blogService.search(keywords, page);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = SecurityConfig.SECURED_API + "/blog/searchAdmin/{keywords}/{page}")
    public Page<Card> searchAdmin(@PathVariable("keywords") String keywords, @PathVariable("page") int page ) {
        return blogService.searchAdmin(keywords, page);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = SecurityConfig.PUBLIC_API + "/blog/list")
    public Collection<Card> list() {
        return blogService.listAllPublished();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = SecurityConfig.PUBLIC_API + "/blog/list/{cat}")
    public Collection<Card> list(@PathVariable("cat") String cat) {
        return blogService.listByCategory(cat);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = SecurityConfig.SECURED_API + "/blog/push")
    public ResponseEntity<?> pushArticle(@RequestBody Article article) {
        try {
            return new ResponseEntity<>(blogService.saveArticle(article),HttpStatus.OK);
        } catch (BlogException e) {
            return new ResponseEntity<>(new Pair<>("error : ", "Error while saving the article : " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = SecurityConfig.SECURED_API + "/blog/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        try {
            var result = storageService.storeImage(file);
            return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
