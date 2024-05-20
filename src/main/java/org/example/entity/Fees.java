package org.example.entity;

import com.sun.istack.NotNull;
import org.example.Enum.StatusType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "fees")
public class Fees {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long idFee;
    @ManyToOne
    @JoinColumn(name = "idApartments")
    private Apartments apartments;
    //@Positive
    @NotNull
    private BigDecimal baseAmountPerMeter;
    //@Positive
    @NotNull
    private BigDecimal additionalFee;
    @NotNull
    private BigDecimal AdditionalFeeForPet;
    @Enumerated(EnumType.STRING)
    private StatusType paymentStatus;
    private LocalDate dateOfPayment;
    private BigDecimal totalFee;

    public Fees (){}

    public Fees(Apartments apartments, BigDecimal baseAmountPerMeter, BigDecimal additionalFee, BigDecimal additionalFeeForPet, StatusType paymentStatus) {
        this.apartments = apartments;
        this.baseAmountPerMeter = baseAmountPerMeter;
        this.additionalFee = additionalFee;
        this.AdditionalFeeForPet = additionalFeeForPet;
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getTotalFee() {return totalFee;}

    public void setTotalFee(BigDecimal totalFee) {this.totalFee = totalFee;}

    public LocalDate getDateOfPayment() {return dateOfPayment;}

    public void setDateOfPayment(LocalDate dateOfPayment) {this.dateOfPayment = dateOfPayment;}

    public long getIdFee() {
        return idFee;
    }

    public void setIdFee(long idFee) {
        this.idFee = idFee;
    }

    public Apartments getApartments() {
        return apartments;
    }

    public void setApartments(Apartments apartments) {
        this.apartments = apartments;
    }

    public BigDecimal getBaseAmountPerMeter() {
        return baseAmountPerMeter;
    }

    public void setBaseAmountPerMeter(BigDecimal baseAmountPerMeter) {
        this.baseAmountPerMeter = baseAmountPerMeter;
    }

    public BigDecimal getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(BigDecimal additionalFee) {
        this.additionalFee = additionalFee;
    }

    public BigDecimal getAdditionalFeeForPet() {
        return AdditionalFeeForPet;
    }

    public void setAdditionalFeeForPet(BigDecimal additionalFeeForPet) {
        AdditionalFeeForPet = additionalFeeForPet;
    }

    public StatusType getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(StatusType paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Fees{" +
                "idFee=" + idFee +
                ", baseAmount=" + baseAmountPerMeter +
                ", additionalAmount=" + additionalFee +
                ", paymentStatus=" + paymentStatus +
                ", dateOfPayment=" + dateOfPayment +
                ", totalFee=" + totalFee + '\'' +
                '}';
    }
}
