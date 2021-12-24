package com.shoppingmall.domain.inquiry;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class ItemInquiry extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_inquiry_id")
    private Long id;

    @Column
    private String message;

    @Column
    private Boolean answerState;

    @Column
    private Integer answerCount;

    @OneToMany(mappedBy = "itemInquiry", cascade = ALL)
    private List<ItemInquiryAnswer> itemInquiryAnswers = new ArrayList<>();

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * 연관관계 메서드
     */
    public void setUser(User user){
        if(this.user != null){
            this.user.getItemInquiries().remove(this);
        }
        this.user = user;
        user.getItemInquiries().add(this);
    }

    public void setItem(Item item){
        if(this.item != null){
            this.item.getItemInquiries().remove(this);
        }
        this.item = item;
        item.getItemInquiries().add(this);
    }
}
