package com.moeen.Newcafe.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
@NamedQuery(name = "Product.getAllProduct",query = "select new com.moeen.Newcafe.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")
@NamedQuery(name="Product.updateStatus",query = "update Product p set p.status=:status where p.id=:id")
@NamedQuery(name = "Product.getBCategory",query = "select new com.moeen.Newcafe.wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")
@NamedQuery(name = "Product.getProductById",query = "select new com.moeen.Newcafe.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.category.id=:id")

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product implements Serializable {
    public static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk",nullable = false)
    private Category category;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Integer price;
    @Column(name="status")
    private String status;

}
