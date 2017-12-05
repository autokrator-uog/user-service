package uk.ac.gla.sed.clients.userservice.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class User {
    private String username;
    private List<Integer> accounts;

    public User() {}

    public User(String username, List<Integer> accounts) {
        this.username = username;
        this.accounts = accounts;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public List<Integer> getAccounts() {
        return accounts;
    }
}
