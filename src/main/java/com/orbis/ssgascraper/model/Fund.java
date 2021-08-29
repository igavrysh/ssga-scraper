package com.orbis.ssgascraper.model;

import java.time.LocalDate;
import java.util.Date;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private LocalDate created;

    @Column(name = "modified")
    private LocalDate modified;

}
