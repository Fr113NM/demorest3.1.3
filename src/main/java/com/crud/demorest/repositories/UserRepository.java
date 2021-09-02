package com.crud.demorest.repositories;

import com.crud.demorest.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select um FROM User um inner JOIN FETCH um.roles where um.username = :uName")
    User findByUsername(@Param("uName") String username);
//    User findByUsername(String username);
}
