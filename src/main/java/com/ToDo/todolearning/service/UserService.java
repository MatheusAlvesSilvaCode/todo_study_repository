package com.ToDo.todolearning.service;

import com.ToDo.todolearning.dto.CreateUserDTO;
import com.ToDo.todolearning.entity.UserEntity;
import com.ToDo.todolearning.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

    public UserEntity CreateUser(CreateUserDTO userDTO) {

        UserEntity user = new UserEntity();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPhoneNumber(userDTO.phone());
        user.setNotifyBySms(userDTO.notifyBySms());
        user.setNotifyByEmail(userDTO.notifyByEmail());
        user = this.userRepository.save(user);// TODO: Criar logs de infos
        return user;
    }

}
