package com.moon.aza.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Table(name = "persistent_logins")
@Entity
public class PersistentLogins {
    @Id
    @Column(length = 64)
    private String series;

    @Column(length = 64)
    private String username;

    @Column(length = 64)
    private String token;

    @Column(length = 64, name="last_used")
    private LocalDateTime lastUsed;

}
