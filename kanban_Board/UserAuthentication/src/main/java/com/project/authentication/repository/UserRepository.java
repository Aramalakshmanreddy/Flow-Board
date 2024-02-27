package com.project.authentication.repository;

import com.project.authentication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String>
{
    public User findByEmailAndPassword(String email, String password);
    public User findByUserNameAndPassword(String userName, String Password);
   // public User findByUserNameAndCreatorEmail(String userName, String creatorEmail);
    public User findByUserName(String userName);
}
