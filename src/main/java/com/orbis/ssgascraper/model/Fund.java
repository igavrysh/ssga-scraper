package com.orbis.ssgascraper.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fund")
public class Fund {
    @Id
    @GeneratedValue
    @Column(name = "fund_id")
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
