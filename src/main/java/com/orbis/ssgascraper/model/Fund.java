package com.orbis.ssgascraper.model;

import java.util.Date;
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
    private Long id;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    private Date modified;

}
