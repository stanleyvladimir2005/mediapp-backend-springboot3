package com.mitocode.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name ="menu")
public class Menu {

    @Id
    private Integer idMenu;

    @Column(length = 20)
    private String icon;

    @Column(length = 20)
    private String name;

    @Column(length = 50)
    private String url;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "menu_role", joinColumns = @JoinColumn(name = "id_menu", referencedColumnName = "idMenu"),
               inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "idRol"))
    private List<Rol> roles;
}