package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "dia")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Dia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codDia;
    private Date fechaBajaDia;
    private String nombreDia;
}
