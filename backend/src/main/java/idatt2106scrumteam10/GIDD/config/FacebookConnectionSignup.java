package idatt2106scrumteam10.GIDD.config;

import idatt2106scrumteam10.GIDD.models.User;
import idatt2106scrumteam10.GIDD.repos.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        System.out.println(connection.getDisplayName());
        user.setEmail(connection.getDisplayName());
        user.setPassword(passwordEncoder.encode(RandomString.make(8)));
        userRepository.save(user);
        return user.getUsername();
    }
}
