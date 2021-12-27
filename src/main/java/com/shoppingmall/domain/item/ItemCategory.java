package com.shoppingmall.domain.item;

import com.shoppingmall.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategory extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "itemCategory",fetch = LAZY, cascade = ALL)
    private List<Item> items;
}
