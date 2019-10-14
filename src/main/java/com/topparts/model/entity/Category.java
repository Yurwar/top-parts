package com.topparts.model.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category parentCategory;

    @ElementCollection(targetClass = String.class)
    private Set<String> properties;

    public Category(String ... properties) {
        this.properties = new HashSet<String>(Arrays.asList(properties));
    }

    public Set<String> getProperties() {
        Set<String> result = new HashSet<>(properties);

        if (!Objects.isNull(parentCategory)) {
            result.addAll(parentCategory.getProperties());
        }

        return result;
    }

}
