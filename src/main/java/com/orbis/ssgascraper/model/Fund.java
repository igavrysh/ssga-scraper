package com.orbis.ssgascraper.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.orbis.ssgascraper.dto.WeightDto;
import lombok.*;
import org.hibernate.annotations.Type;

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
    @Type(type="uuid-char")
    @Column(name="fund_id", insertable = false, updatable = false)
    private UUID id;

    private String name;

    private String ticker;

    private String domicile;

    @Lob
    private String description;

    private String link;

    private LocalDateTime created;

    private LocalDateTime modified;

    @OneToMany
    private List<Weight> countryWeights;

    @OneToMany
    private List<Weight> sectorWeights;

    @OneToMany
    private List<Weight> holdingWeights;

}
