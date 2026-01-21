package com.marthina.splitconnect.model;

import com.marthina.splitconnect.model.enums.ServicesType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "services")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ServicesType type;
}
