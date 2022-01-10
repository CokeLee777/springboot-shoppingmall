package com.shoppingmall.domain;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.exception.NotEnoughStockException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.ItemRequestDto.ItemUpdateForm;
import static com.shoppingmall.dto.ItemResponseDto.ItemInfo;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Item extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private Integer stockQuantity;

    @Enumerated(value = STRING)
    private ItemStatus itemStatus;

    @Column
    private String itemImg;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    public List<CartItem> cartItems = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_category_id")
    public ItemCategory itemCategory;

    @Builder
    private Item(String name, Integer price, Integer stockQuantity, String itemImg, ItemCategory itemCategory){
        this.itemStatus = ItemStatus.SALE;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemImg = itemImg;
        setItemCategory(itemCategory);
    }

    /**
     * 연관관계 메서드
     */
    private void setItemCategory(ItemCategory itemCategory){
        if(this.itemCategory !=  null){
            this.itemCategory.getItems().remove(this);
        }
        this.itemCategory = itemCategory;
        itemCategory.getItems().add(this);
    }


    public void updateItem(ItemUpdateForm itemUpdateForm, String itemImg){
        this.name = itemUpdateForm.getName();
        this.price = itemUpdateForm.getPrice();
        this.stockQuantity = itemUpdateForm.getStockQuantity();
        this.itemStatus = itemUpdateForm.getItemStatus();
        this.itemImg = itemImg;
    }

    /**
     * 비즈니스 로직
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;

        if(restStock < 0){
            throw new NotEnoughStockException("재고가 부족합니다.");
        }
        //품절이라면
        if(restStock == 0) this.itemStatus = ItemStatus.SOLDOUT;

        this.stockQuantity = restStock;
    }

    public ItemInfo toItemInfo(){
        return ItemInfo.builder()
                .id(id)
                .itemCategoryId(itemCategory.getId())
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .itemStatus(itemStatus)
                .itemImg(itemImg)
                .build();
    }

    public ItemUpdateForm toItemUpdateForm(MultipartFile itemImg){
        return ItemUpdateForm.builder()
                .categoryId(itemCategory.getId())
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .itemStatus(itemStatus)
                .itemImg(itemImg)
                .build();
    }
}
