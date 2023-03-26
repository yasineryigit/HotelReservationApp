package com.ossovita.commonservice.core.utilities.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ossovita.commonservice.core.entities.User;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {

    @Id
    private String token;

    @ManyToOne
    @JsonIgnore
    private User user;


}
