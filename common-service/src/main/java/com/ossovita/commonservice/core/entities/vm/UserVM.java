package com.ossovita.commonservice.core.entities.vm;

import com.ossovita.commonservice.core.entities.User;
import lombok.Data;

@Data
public class UserVM {

    private long userPk;

    private String userEmail;

    private String userFirstName;

    private String userLastName;


    public UserVM(User inDB) {
        this.userPk = inDB.getUserPk();
        this.userEmail = inDB.getUserEmail();
        this.userFirstName = inDB.getUserFirstName();
        this.userLastName = inDB.getUserLastName();
    }
}
