package com.topparts.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date_of_order")
    private LocalDateTime dateOfPurchase;

    @OneToMany(mappedBy = "order")
    private Set<OrderEntry> entries;
}
