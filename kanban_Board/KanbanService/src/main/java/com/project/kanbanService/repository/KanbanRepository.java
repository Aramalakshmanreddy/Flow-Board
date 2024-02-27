package com.project.kanbanService.repository;

import com.project.kanbanService.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanbanRepository extends MongoRepository<User, String> {
    public User findByUserName(String userName);
}
