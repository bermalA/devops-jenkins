package devops.Jenkins.service;

import devops.Jenkins.model.User;
import devops.Jenkins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public User findUserById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        User user = optional.orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
}
