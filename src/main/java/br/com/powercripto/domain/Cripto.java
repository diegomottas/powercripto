package br.com.powercripto.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Cripto.
 */
@Entity
@Table(name = "cripto")
public class Cripto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "quantidade_hashes")
    private Long quantidadeHashes;

    @Column(name = "tempo", precision=15, scale=2)
    private BigDecimal tempo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantidadeHashes() {
        return quantidadeHashes;
    }

    public void setQuantidadeHashes(Long quantidadeHashes) {
        this.quantidadeHashes = quantidadeHashes;
    }

    public BigDecimal getTempo() {
        return tempo;
    }

    public void setTempo(BigDecimal tempo) {
        this.tempo = tempo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cripto cripto = (Cripto) o;
        if(cripto.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cripto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cripto{" +
            "id=" + id +
            ", quantidadeHashes='" + quantidadeHashes + "'" +
            ", tempo='" + tempo + "'" +
            '}';
    }
}
