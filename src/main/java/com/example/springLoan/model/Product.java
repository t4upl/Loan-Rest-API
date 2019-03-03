package com.example.springLoan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "product", schema = "public")
@Getter
@Setter
@ToString (exclude = {"customer", "productType"})
@AllArgsConstructor
public class Product {

    @Id
    @Column(columnDefinition = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="product_type_id")
    private ProductType productType;

    @OneToMany(mappedBy = "product")
    Set<ProductSetting> productSettings;
}