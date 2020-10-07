package com.avelircraft.repositories.user;

import com.avelircraft.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomizedUsersCrudRepository extends CrudRepository<User, Integer> {

    Optional<User> findByRealname(String realname);
}
