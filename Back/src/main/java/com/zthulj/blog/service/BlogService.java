package com.zthulj.blog.service;

import com.zthulj.blog.dto.Article;
import com.zthulj.blog.dto.Card;
import com.zthulj.blog.exception.BlogException;
import com.zthulj.blog.repository.BlogRepository;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Service
public class BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    ArticleValidator validator;

    @Autowired
    MardownService mdService;

    @Autowired
    MongoTemplate mongoTemplate;

    FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");

    public Article getArticleByLink(String link) {
       Article a = blogRepository.findByLinkIgnoreCaseAndPublished(link, true);
       if(a != null){
            a.getValue().setContentHtml(mdService.convertMardownToHtml(a.getValue().getContentMD()));
            a.getValue().setContentMD(null); // Set to null to improve network data load
           a.setFormattedDate(fastDateFormat.format(a.getPublishDate()));
           a.setPublishDate(null); // Set to null to improve network data load
       }
       return a;
    }

    public Article getFullArticleByLink(String link) {
        return blogRepository.findByLink(link);
    }

    public Article saveArticle(Article article) throws BlogException {

        validator.Validate(article);

        // Permet la génération d'un nouvel id (ne marche pas si on reçoit un id 'vide'
        article.setId(article.getId() != null && article.getId().isEmpty() ? null : article.getId());
        if (null == article.getId()
                && getArticleByLink(article.getLink()) != null) {
            throw new BlogException("Article with this link already exist");
        }

        if(article.getPublishDate() == null){
            article.setPublishDate(Calendar.getInstance().getTime());
        }

        return blogRepository.save(article);
    }

    public Collection<Card> search(String keywords) {
        List<Article> articles = blogRepository.findPublishedByKeyword(keywords);
        List<Card> cards = new ArrayList<>();
        articles.forEach(e -> cards.add(cardFromArticle(e)));
        return cards;
    }

    public Collection<Card> searchAdmin(String keywords) {
        List<Article> articles = blogRepository
                .findByValue_ContentMDContainingIgnoreCaseOrTitleContainingIgnoreCase(keywords, keywords);
        List<Card> cards = new ArrayList<>();
        articles.forEach(e -> cards.add(cardFromArticle(e)));
        return cards;

    }

    public Collection<Card> listAllPublished() {
        List<Card> cards = new ArrayList<>();
        blogRepository.findByPublished(true).forEach(e -> cards.add(cardFromArticle(e)));
        return cards;
    }

    public Collection<Card> listAll() {
        List<Card> cards = new ArrayList<>();
        blogRepository.findAll().forEach(e -> cards.add(cardFromArticle(e)));
        return cards;
    }

    public Collection<Card> listByCategory(String cat) {
        List<Card> cards = new ArrayList<>();
        blogRepository.findByCategoryAndPublished(cat, true).forEach(e -> cards.add(cardFromArticle(e)));
        return cards;
    }

    private Card cardFromArticle(Article e) {
        Card c = new Card();
        c.setCategory(e.getCategory());
        c.setDescription(e.getDescription());
        c.setPublishDate(fastDateFormat.format(e.getPublishDate()));
        c.setLink(e.getLink());
        c.setTitle(e.getTitle());
        c.setImageCard(e.getImageCard());
        c.setPublished(e.isPublished());
        return c;
    }


}
