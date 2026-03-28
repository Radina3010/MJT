package bg.sofia.uni.fmi.mjt.taskmanager.model.entity;

public record User(String username, String encodedPassword) {

    private static final String UNSUCCESSFUL_MESSAGE =
            "Username and password can not be null or blank";

    public User {
        if (username == null || username.isBlank() || encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException(UNSUCCESSFUL_MESSAGE);
        }
    }

}
