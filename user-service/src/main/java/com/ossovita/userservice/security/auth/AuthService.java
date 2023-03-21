package com.ossovita.userservice.security.auth;

import com.ossovita.userservice.core.dataAccess.TokenRepository;
import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.entities.vm.UserVM;
import com.ossovita.userservice.core.utilities.error.AuthException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    TokenRepository tokenRepository;


    public AuthResponse<Object> authenticateUser(Credentials credentials) {
        User inDB = checkUserFromDB(credentials);

        AuthResponse<Object> response = new AuthResponse<>();
        if (inDB.isAccountNonLocked()) {
            UserVM userVM = new UserVM(inDB);
            String token = generateRandomToken();
            Token tokenEntity = new Token();
            tokenEntity.setToken(token);
            tokenEntity.setUser(inDB);
            tokenRepository.save(tokenEntity);
            response.setUser(userVM);
            response.setToken(token);
        } else {
            throw new AuthException("Your account has been locked. Please contact with our support team");
        }

        return response;
    }

    public UserDetails getUserDetails(String token) {
        Optional<Token> optionalToken = tokenRepository.findById(token);
        return optionalToken.<UserDetails>map(Token::getUser).orElse(null);//return UserDetails if available
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");//remove - symbol from the uuid
    }

    private User checkUserFromDB(Credentials credentials) {

        User inDB = userRepository.findUserByUserEmail(credentials.getUserEmail());

        if (inDB == null) {
            throw new AuthException("Please check your email and password");//if there is no user belongs to that user email
        }

        boolean matches = passwordEncoder.matches(credentials.getUserPassword(), inDB.getUserPassword());
        if (!matches) {
            throw new AuthException("Please check your email and password");//if the password is incorrect
        }

        return inDB;
    }


    public void clearToken(String tokenString) {
        Token token = tokenRepository.findTokenByToken(tokenString);
        System.out.println("token:" + token.getToken() + " tokenleri silinecek user: " + token.getUser().getUserPk());
        tokenRepository.deleteTokensByUser_UserPk(token.getUser().getUserPk());

    }
}
