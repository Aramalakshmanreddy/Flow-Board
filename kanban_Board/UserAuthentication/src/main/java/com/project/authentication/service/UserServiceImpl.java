package com.project.authentication.service;
import com.project.authentication.domain.User;
import com.project.authentication.exception.InvalidCredentialsException;
import com.project.authentication.exception.UserAlreadyExistException;
import com.project.authentication.proxy.EmailProxy;
import com.project.authentication.proxy.KanbanProxy;
import com.project.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService
{
    private UserRepository userRepository;
    private KanbanProxy kanbanProxy;
    private EmailProxy emailProxy;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, KanbanProxy kanbanProxy, EmailProxy emailProxy) {
        this.userRepository = userRepository;
        this.kanbanProxy = kanbanProxy;
        this.emailProxy = emailProxy;
    }

    @Override
    public User registerUser(User user)throws UserAlreadyExistException
    {
        System.out.println("came here" +user);
//        if(userRepository.findById(user.getEmail()).isPresent())
//            throw new UserAlreadyExistException();
        User userCreated=null;
        if (user.getPassword()==null || user.getPassword().isEmpty()) {
            if (userRepository.findByUserName(user.getUserName()) != null){
                user.setEmail(user.getEmail());
                return user;
            }
            User createUser =new User();
            createUser.setUserName(user.getUserName());
            String email = user.getUserName().concat("@gmail.com");
            String password = user.getUserName().concat("*Ps1");
            createUser.setEmail(user.getEmail());
            createUser.setPassword(password);
            emailProxy.confirmRegisterForMember(createUser);
            userCreated =userRepository.save(createUser);
        }else{
//            if (user.getCreatorEmail() == null || user.getCreatorEmail().isEmpty())
//                user.setCreatorEmail(user.getEmail());
            emailProxy.confirmRegisterForCreator(user);
            userCreated= userRepository.save(user);
        }
        if(userCreated!=null)
            user.setEmail(userCreated.getEmail());
             System.out.println(userCreated);
             kanbanProxy.saveUserInMongoDb(user);

        return userCreated;
    }

    @Override
    public User loginUser(String email, String password)throws InvalidCredentialsException
    {
        User loggedInUser = userRepository.findByEmailAndPassword(email, password);
        if(loggedInUser == null)
            loggedInUser = userRepository.findByUserNameAndPassword(email, password);
        if(loggedInUser == null)
            throw new InvalidCredentialsException();

        return loggedInUser;
    }

    @Override
    public void deleteUser(User user) throws InvalidCredentialsException {
//        User deleteUser = userRepository.findByUserNameAndCreatorEmail(user.getUserName(), user.getCreatorEmail());
//        if (deleteUser == null)
//            throw new InvalidCredentialsException();
//
//        emailProxy.memberRemovedConfirmation(deleteUser);
//        userRepository.delete(deleteUser);
    }
}
