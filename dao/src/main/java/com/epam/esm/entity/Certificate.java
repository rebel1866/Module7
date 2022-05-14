package com.epam.esm.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gift_certificates")
public class Certificate implements Serializable {
    private static final long SERIAL_VERSION_UID = 5545654654564504L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giftCertificateId;
    @Column(name = "certificate_name")
    private String certificateName;
    private String description;
    private Integer price;
    private Integer duration;
    @ManyToMany
    @JoinTable(name = "cert_tags", joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;
    private String operation;
    private LocalDateTime operationTime;

    @PrePersist
    public void onPrePersist() {
        setOperation("Certificate is persisted in DB");
        setOperationTime(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        setOperation("Certificate is updated");
        setOperationTime(LocalDateTime.now());
    }
}

