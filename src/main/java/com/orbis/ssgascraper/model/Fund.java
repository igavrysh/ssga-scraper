package com.orbis.ssgascraper.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String description;

    @Column(name = "link")
    private String link;

}
