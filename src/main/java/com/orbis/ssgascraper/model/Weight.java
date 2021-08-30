package com.orbis.ssgascraper.model;

import com.orbis.ssgascraper.enums.WeightType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "weight")
public class Weight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    @Column(name="weight_id", insertable = false, updatable = false)
    private UUID id;

    private String name;

    private WeightType type;

    private LocalDate date;

    private Double value;

    private LocalDateTime created;

    private LocalDateTime modified;

    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;

}
