package com.topparts.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category parentCategory;

    @ElementCollection(targetClass = String.class)
    private Set<String> properties;

    public Set<String> getAllProperties() {
        Set<String> result = new HashSet<>(properties);

        if (!Objects.isNull(parentCategory)) {
            result.addAll(parentCategory.getAllProperties());
        }

        return result;
    }

}
