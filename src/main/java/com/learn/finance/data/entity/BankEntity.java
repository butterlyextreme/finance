package com.learn.finance.data.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(name = "bank")
@Entity(name = "bank")
public class BankEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;
    private String bankName;
    private String city;
    private String bic;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    @Fetch(value = FetchMode.JOIN)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OrderBy("creation_time DESC")
    private Set<CommentEntity> commentEntities = new HashSet<>();


}
