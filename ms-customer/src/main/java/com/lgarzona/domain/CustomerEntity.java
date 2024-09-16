package com.lgarzona.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustomerEntity extends PersonEntity implements Serializable {

    private String password;

    private Boolean status;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
