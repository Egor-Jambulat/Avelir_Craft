package com.avelircraft.services;

import com.avelircraft.models.Guide;
import com.avelircraft.repositories.guide.CustomizedGuidesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GuidesDataService {

    @Autowired
    private CustomizedGuidesCrudRepository guidesCrudRepository;

    public Guide save(Guide var){ return guidesCrudRepository.save(var); };

    public void deleteById(Integer id){
        guidesCrudRepository.deleteById(id);
    }

    public List<Guide> findAll(){
        ArrayList<Guide> guides = new ArrayList<>();
        guidesCrudRepository.findAllByOrderByDate()
                .iterator()
                .forEachRemaining(guides::add);
        return guides;
    }

    public Guide incrementViews(Guide var){
        guidesCrudRepository.incrementGuideViewsById(var.getId());
        return var.incrementViews();
    }
}
