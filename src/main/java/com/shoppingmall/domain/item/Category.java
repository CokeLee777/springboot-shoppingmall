package com.shoppingmall.domain.item;

import com.shoppingmall.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "parent_id")
    private Category category;

    @OneToMany(mappedBy = "category")
    private List<Category> categories = new ArrayList<>();
}
