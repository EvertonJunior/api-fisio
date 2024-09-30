package com.ejunior.fisio_api.services;

import com.ejunior.fisio_api.entities.User;
import com.ejunior.fisio_api.exceptions.UserUniqueViolationException;
import com.ejunior.fisio_api.exceptions.InvalidPasswordException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import com.ejunior.fisio_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new UserUniqueViolationException("Usuário já existe");
        }
    }

    @Transactional(readOnly = true)
    public User findById(long id){
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new NotFoundException("id " + id + " não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<User> findAll(){
        return repository.findAll();
    }

    @Transactional
    public User updatePassword(long id, String currentPassword, String newPassword, String confirmNewPassword){
        User user = findById(id);
        if(!passwordEncoder.matches(currentPassword, user.getPassword())){
            throw new InvalidPasswordException("Senha incorreta");
        }
        if(!newPassword.equals(confirmNewPassword)){
            throw new InvalidPasswordException("Senhas não conferem");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }

    public User findByUsername(String username) {
        Optional<User> user = repository.findByUsername(username);
        return user.orElseThrow(() -> new NotFoundException("Username "+ username + " não encontrado"));
    }

    @Transactional(readOnly = true)
    public User.Role findRoleByUsername(String username) {
        return repository.findRoleByUsername(username);
    }
}
