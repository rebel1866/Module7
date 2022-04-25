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
@Table(name = "tags")
public class Tag implements Serializable {
    private static final long SERIAL_VERSION_UID = 654654654564545L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;
    @Column(name = "tag_name")
    private String tagName;
    private String operation;
    private LocalDateTime operationTime;

    @PrePersist
    public void onPrePersist() {
        setOperation("Tag is persisted id DB");
        setOperationTime(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        setOperation("Tag is updated");
        setOperationTime(LocalDateTime.now());
    }
}
