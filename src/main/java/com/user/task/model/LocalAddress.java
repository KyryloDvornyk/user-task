package com.user.task.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "local_addresses")
public class LocalAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private Long houseNumber;

    public LocalAddress() {
    }

    public LocalAddress(String street, Long houseNumber) {
        this.houseNumber = houseNumber;
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Long houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "LocalAddress{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", houseNumber=" + houseNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalAddress address = (LocalAddress) o;
        return Objects.equals(street, address.street) && Objects.equals(houseNumber, address.houseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, houseNumber);
    }
}
