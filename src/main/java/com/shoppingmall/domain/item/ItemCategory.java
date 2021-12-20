package com.shoppingmall.domain.item;

import com.shoppingmall.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategory extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "category_id")
    private Category category;
}
