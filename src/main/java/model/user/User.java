package model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private Integer idUser;
    private String login;
    private String password;
    private UserRole role;

    @Override
    public String toString() {
        return login;
    }
}
