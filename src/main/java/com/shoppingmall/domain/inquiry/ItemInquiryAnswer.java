package com.shoppingmall.domain.inquiry;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.user.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class ItemInquiryAnswer extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_inquiry_answer")
    private Long id;

    @Column
    private String message;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_inquiry_id")
    private ItemInquiry itemInquiry;

    /**
     * 연관관계 메서드
     */
    public void setUser(User user){
        if(this.user != null){
            this.user.getItemInquiryAnswers().remove(this);
        }
        this.user = user;
        user.getItemInquiryAnswers().add(this);
    }

    public void setItemInquiry(ItemInquiry itemInquiry){
        if(this.itemInquiry != null){
            this.itemInquiry.getItemInquiryAnswers().remove(this);
        }
        this.itemInquiry = itemInquiry;
        itemInquiry.getItemInquiryAnswers().add(this);
    }
}
