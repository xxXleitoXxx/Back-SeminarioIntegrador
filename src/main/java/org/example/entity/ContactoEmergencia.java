package org.example.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "contactoemergencia")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoEmergencia extends BaseEntity{
    private String direccionContacto;
    private String nombreContacto;
    private long telefonoContacto;

}
