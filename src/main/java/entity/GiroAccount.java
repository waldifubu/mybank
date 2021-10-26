package entity;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity(name = "giro")
public class GiroAccount extends AbstractAccount {
    private BigDecimal fee;

    private BigDecimal overdraft;

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.overdraft = overdraft;
    }
}
