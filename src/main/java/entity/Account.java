package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "konten")
public class Account
{
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private long konto_nr;
    //Kontotyp GIRO SPAR FEST
    private String typ;

    //Erstelldatum
    private String erstellt;

    private BigDecimal kontostand;

    private String kunden_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getKonto_nr() {
        return konto_nr;
    }

    public void setKonto_nr(long konto_nr) {
        this.konto_nr = konto_nr;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getErstellt() {
        return erstellt;
    }

    public void setErstellt(String erstellt) {
        this.erstellt = erstellt;
    }

    public BigDecimal getKontostand() {
        return kontostand;
    }

    public void setKontostand(BigDecimal kontostand) {
        this.kontostand = kontostand;
    }

    public String getKunden_id() {
        return kunden_id;
    }

    public void setKunden_id(String kunden_id) {
        this.kunden_id = kunden_id;
    }
}
