package com.orbis.ssgascraper.model;

import java.time.LocalDateTime;
import java.util.UUID;
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

}
