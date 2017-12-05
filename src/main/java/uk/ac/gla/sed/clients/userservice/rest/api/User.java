package uk.ac.gla.sed.clients.userservice.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class User {
    private String username;
    private List<Integer> userAccounts;

    public User() {}

    public User(String username, List<Integer> userAccounts) {
        this.username = username;
        this.userAccounts = userAccounts;
    }

    @JsonProperty
    public String getUserName() {
        return username;
    }

    @JsonProperty
    public List<Integer> getUserAccounts() {
        return userAccounts;
    }
}
