package ua.com.vetal.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "vkrascoottiskamount")
public class Kraskoottisk {
    @Id
    @Column(name = "amount")
    private double amount;

    public Kraskoottisk() {
    }

    public Kraskoottisk(double amount) {
        this.amount = amount;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Kraskoottisk{");
        sb.append("amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
