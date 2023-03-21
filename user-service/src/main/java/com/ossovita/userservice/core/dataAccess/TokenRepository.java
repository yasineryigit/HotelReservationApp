package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.security.auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {

    Token findTokenByToken(String token);

    void deleteTokensByUser_UserPk(long userPk);

    List<Token> findTokensByUser_UserPk(long userPk);
}
