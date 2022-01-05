package com.shoppingmall.domain.review;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class ItemReview extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_review_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * 연관관계 메서드
     */
    public void setItem(Item item){
        if(this.item != null){
            this.item.getItemReviews().remove(this);
        }
        this.item = item;
        item.getItemReviews().add(this);
    }

    public void setUser(User user){
        if(this.user != null){
            this.user.getItemReviews().remove(this);
        }
        this.user = user;
        user.getItemReviews().add(this);
    }
}
