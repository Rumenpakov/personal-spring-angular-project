package eu.deltasource.library.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.deltasource.library.entities.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * Contains all personal information about user, such as name age sex location.
 */
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Name name;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    @OneToOne
    private Location location;
    @OneToOne
    private AccountCredentials accountCredentials;

    private String getPassword() {
        return accountCredentials.getPassword();
    }

    public String getUserName() {
        return accountCredentials.getUsername();
    }

    public Name getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Checks if given password matches the password of this user.
     *
     * @param password Given password
     * @return True if matches
     */
    public boolean isPasswordMatching(String password) {
        return getPassword().equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserAccountDetails) {
            UserAccountDetails that = (UserAccountDetails) o;
            return name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UserAccountDetails{" +
                "id=" + id +
                ", name=" + name +
                ", age=" + age +
                ", sex=" + gender +
                ", email='" + email + '\'' +
                ", location=" + location +
                ", accountCredentials=" + accountCredentials +
                '}';
    }
}

