package eu.deltasource.library.entities.models;

import eu.deltasource.library.entities.enums.Gender;
import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class SignUpModel {
    private final String name;
    private final int age;
    private final Gender gender;
    private final String email;
    private final String address;
    private final String city;
    private final String country;
    private final String username;
    private final String password;
}
