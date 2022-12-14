package com.moeen.Newcafe.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
@NamedQuery(name = "User.findByEmailId",query = "select u from User u where u.email=:email")
@NamedQuery(name="User.getAllUserByRole",query = "select new com.moeen.Newcafe.wrapper.UserWrapper(u.id,u.name,u.contractNumber,u.email,u.password,u.Status) from User u where u.role='intern'")
@NamedQuery(name = "User.updateStatus",query = "update User u set u.Status=:status where u.id=:id" )
@NamedQuery(name="User.getAllAdmin",query = "select u.email from User u where u.role='admin'")
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    public static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "contractNumber")
    private  String contractNumber;
    @Column(name="password")
    private  String password;
    @Column(name="Status")
    private  String Status;
    @Column(name="role")
    private  String role;


}
