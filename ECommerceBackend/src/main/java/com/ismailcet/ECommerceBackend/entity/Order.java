package com.ismailcet.ECommerceBackend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_date")
    private Date createdDate = new Date();
    @Column(name = "status")
    private CargoStatus cargoStatus = CargoStatus.PENDING;
    @Column(name = "address")
    private String address;
    @Column(name = "amount")
    private double amount;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
