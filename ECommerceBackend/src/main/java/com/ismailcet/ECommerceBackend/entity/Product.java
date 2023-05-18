package com.ismailcet.ECommerceBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private String id;

    @Column(name="photo_url")
    private String photoUrl;
    @Column(name="name")
    private String name;
    @Column(name="price")
    private Double price;
    @Column(name="color")
    private String color;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_size",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name = "format_id")
    )
    private List<Size> sizesProduct;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categoriesProduct;
}
