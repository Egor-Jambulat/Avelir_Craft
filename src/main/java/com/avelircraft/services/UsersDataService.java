package com.avelircraft.services;

import com.avelircraft.models.User;
import com.avelircraft.repositories.user.CustomizedUsersCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UsersDataService {

    @Autowired
    private CustomizedUsersCrudRepository usersCrudRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<User> findById(Integer id){
        return usersCrudRepository.findById(id);
    }

    public Optional<User> findByName(String name){
        return usersCrudRepository.findByRealname(name);
    }

    public Optional<User> findByNameAndPassword(String name, String password){
        Optional<User> user = usersCrudRepository.findByRealname(name);
        if (user.isEmpty())
            return user;
        if (!bCryptPasswordEncoder.matches(password, user.get().getPassword())){
            Optional<User> notFound = null;
            return notFound;
        }
        return user;
    }

    public Iterable<User> findAll(){
        return usersCrudRepository.findAll();
    }

}
