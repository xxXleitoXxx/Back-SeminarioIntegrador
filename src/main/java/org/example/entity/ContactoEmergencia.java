package org.example.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contactoemergencia")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoEmergencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NroContactoEmergencia;
    private String direccionContacto;
    private String nombreContacto;
    private long telefonoContacto;

}
