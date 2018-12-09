package com.example.springLoan_18112018.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "customer", schema = "public")
@Getter @Setter
@ToString (exclude = {"products"})
public class Customer implements Serializable {

    @Id
    @Column(columnDefinition = "id")
    private Integer id;

    @Column(columnDefinition = "name")
    private String name;

    @OneToMany(mappedBy = "customer")
    Set<Product> products;

}