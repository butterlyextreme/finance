package com.learn.finance.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(name = "comment")
@Entity(name = "comment")
public class CommentEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;
  private String comment;

  @CreationTimestamp
  @Column(name = "creation_time", updatable = false)
  private Date creationTime;

  @UpdateTimestamp
  @Column(name = "modified_time")
  private Date modifiedTime;

  @ManyToOne
  @JoinColumn(name= "bank_id", referencedColumnName = "id", nullable = false)
  private BankEntity bank;

}
