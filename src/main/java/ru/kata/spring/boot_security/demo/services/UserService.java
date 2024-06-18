package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public List<User> getAllUsers() {
        String jpql = "SELECT user FROM User user";
        return entityManager.createQuery(jpql, User.class).getResultList();
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    public boolean addUser(User user) {
        if (userRepository.findByEmail(user.getUsername()) != null) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long id) {
        if (entityManager.find(User.class, id) == null) {
            return false;
        }
        entityManager.remove(entityManager.find(User.class, id));
        return true;
    }

    public boolean changeUser(Long id, String newFirstName, String newLastName, String newAge, String newEmail, String newPassword) {
        if (entityManager.find(User.class, id) == null) {
            return false;
        }
        entityManager.find(User.class, id).setFirstName(newFirstName);
        entityManager.find(User.class, id).setLastName(newLastName);
        entityManager.find(User.class, id).setAge(newAge);
        entityManager.find(User.class, id).setEmail(newEmail);
        entityManager.find(User.class, id).setPassword(newPassword);
        return true;
    }
}
