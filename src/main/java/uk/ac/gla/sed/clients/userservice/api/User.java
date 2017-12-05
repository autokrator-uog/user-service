package uk.ac.gla.sed.clients.Usersservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private Integer userId;

    public User() {}

    public User(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty
    public Integer getUserId() {
        return userId;
    }


}
