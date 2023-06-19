package com.ismailcet.ECommerceBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "address")
    private String address;
    @Column(name = "cargoStatus")
    private CargoStatus cargoStatus = CargoStatus.PENDING;

    @OneToOne()
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;


    @ManyToMany()
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns =@JoinColumn(name = "product_id")
    )
    Set<Product> products;

    @Column(name = "amount")
    private double amount;


}
