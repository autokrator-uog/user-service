package uk.ac.gla.sed.clients.userservice.rest.internal;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
    private final String digest;

    private PasswordHash(final String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public boolean checkPassword(final String passwordToCheck) {
        return BCrypt.checkpw(passwordToCheck, digest);
    }

    public static PasswordHash fromDigest(final String digest) {
        return new PasswordHash(digest);
    }

    public static PasswordHash generateFromPassword(final String password) {
        final String salt = BCrypt.gensalt(5);
        return PasswordHash.fromDigest(BCrypt.hashpw(password, salt));
    }
}
