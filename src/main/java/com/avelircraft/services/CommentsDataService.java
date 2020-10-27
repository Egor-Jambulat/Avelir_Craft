package com.avelircraft.services;

import com.avelircraft.models.Comment;
import com.avelircraft.repositories.comment.CommentsCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentsDataService {

    @Autowired
    private CommentsCrudRepository commentsCrudRepository;

    public Comment save(Comment var){
        return commentsCrudRepository.save(var);
    }
}
