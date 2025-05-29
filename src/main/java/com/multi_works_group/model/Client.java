// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String document;
    private String type;
    private String phone;
    private String email;
    private String address;
    private String status;

    @Column(name = "company")
    private String company;

    public Client() {}

    // Constructor para creación conveniente
    public Client(String name, String document, String type,
                  String phone, String email, String address,
                  String company) {
        this.name = name;
        this.document = document;
        this.type = type;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.company = company;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
}