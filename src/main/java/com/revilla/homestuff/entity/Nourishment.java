package com.revilla.homestuff.entity;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;

/**
 * Nourishment
 * @author Kirenai
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "category"})
@Entity
@Table(name = "nourishments", uniqueConstraints = {
    @UniqueConstraint(name = "unq_name", columnNames = {"name"})
})
public class Nourishment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nourishment_id")
    private Long nourishmentId;

    @Column(name = "name", nullable = false, length = 35)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_id")
    )
    private User user;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_category_id")
    )
    private Category category;

    @OneToMany(mappedBy = "nourishment", cascade = CascadeType.ALL)
    private Collection<Consumption> consumptions;

    @OneToOne(mappedBy = "nourishment", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AmountNourishment amountNourishment;

}
