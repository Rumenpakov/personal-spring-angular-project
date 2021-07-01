package eu.deltasource.library.entities;

import eu.deltasource.library.exceptions.IllegalInputException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Contains information about location of {@link Author}, such as address, city and country.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity

public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String address;
    private String city;
    private String country;

    public Location(String address, String city, String country) {
        setAddress(address);
        setCity(city);
        setCountry(country);
    }

    private void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalInputException("Address for new location can not be null");
        }
        this.address = address.trim();
    }

    private void setCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalInputException("City for new location can not be null");
        }
        this.city = city.trim();
    }

    private void setCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalInputException("Country for new location can not be null");
        }
        this.country = country.trim();
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
