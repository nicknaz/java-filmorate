package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private int id;
    @Email
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]$")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if(name == null){
            this.name = login;
        }
        else{
            this.name = name;
        }
        this.birthday = birthday;
    }
}
