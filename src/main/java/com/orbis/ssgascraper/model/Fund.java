package com.orbis.ssgascraper.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fund")
public class Fund {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "fund_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "domicile")
    private String domicile;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "link")
    private String link;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "modified")
    private LocalDateTime modified;

}
