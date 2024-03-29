package com.ismailcet.ECommerceBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "products")
public class Product {

    //private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Integer id;
    @Column(name="photo_url")
    private String photoUrl;
    @Column(name="name")
    private String name;
    @Column(name="price")
    private Double price;
    @Column(name="color")
    private String color;
    @Column(name="stock")
    private Integer stock;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_size",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name = "size_id")
    )
    private Set<Size> sizesProduct;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categoriesProduct = new HashSet<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductImage> productImageList;
}
