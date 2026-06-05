package ufps.edu.co.persistence.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ultimocodigoprograma")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UltimocodigoprogramaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "id_programa", nullable = false)
    private Integer idPrograma;

    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @ManyToOne
    @JoinColumn(name = "id_programa", referencedColumnName = "id", insertable = false, updatable = false)
    private ProgramaEntity programa;
}
