package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    private static final long SERIAL_VERSION_UID = 254654654564540L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private Certificate certificate;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    @Column(name = "final_price")
    private Integer finalPrice;
    private String comment;
    private String operation;
    private LocalDateTime operationTime;

    @PrePersist
    public void onPrePersist() {
        setOperation("Order is persisted id DB");
        setOperationTime(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        setOperation("Order is updated");
        setOperationTime(LocalDateTime.now());
    }
}
