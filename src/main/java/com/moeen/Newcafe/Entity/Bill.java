package com.moeen.Newcafe.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
@NamedQuery(name="Bill.getAllBills",query = "select b from Bill b order by b.id desc")
@NamedQuery(name="Bill.getBillByName",query = "select b from Bill b where b.createdBy=:currentUser order by b.id desc")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
public class Bill implements Serializable {
    public static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="uuid")
    private String uuid;
    @Column(name="name")
    private String name;
    @Column(name="email")
    private String email;
    @Column(name="contractnumber")
    private String contractNumber;
    @Column(name="paymentmethod")
    private String paymentMethod;
    @Column(name="total")
    private Integer total;
    @Column(name = "productdetails",columnDefinition = "json")
    private String productDetails;
    @Column(name="createdby")
    private String createdBy;
}
