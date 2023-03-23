package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.utilities.error.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {

    Token findTokenByToken(String token);

    void deleteTokensByUser_UserPk(long userPk);

    List<Token> findTokensByUser_UserPk(long userPk);
}
