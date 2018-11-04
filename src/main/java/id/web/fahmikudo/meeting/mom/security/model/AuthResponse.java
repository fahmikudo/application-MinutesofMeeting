package id.web.fahmikudo.meeting.mom.security.model;

public class AuthResponse {

    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public String toString() {
        return "AuthResponse{" + "token=" + token + '}';
    }

}
