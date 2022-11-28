package com.example.shop.domain.item.category;

import com.example.shop.common.exception.InvalidParamException;
import com.example.shop.domain.AbstractEntity;
import com.example.shop.util.TokenGenerator;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "category")
public class Category extends AbstractEntity {
    private static final String CATEGORY_PREFIX = "ctg_";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String categoryToken;

    @Setter
    private String categoryName;
    @Setter
    private String description;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Category parent;
//
    @OneToMany(fetch = LAZY, mappedBy = "parent", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @OneToMany( fetch = LAZY, mappedBy = "category")
    private List<CategoryItem> categoryItemList = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum Level {
        PRIMARY_CATEGORY(1, "1차 카테고리"),
        SECONDARY_CATEGORY(2, "2차 카데고리");

        private final Integer depth;
        private final String description;
    }

    //==연관관계 메서드==//

    private void setParent(Category parent) {
        if(this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;
        parent.getChildren().add(this);
    }

    @Builder(builderClassName = "categoryBuilder" ,builderMethodName = "categoryBuilder")
    public Category (String categoryName, String description) {
        if (categoryName == null) throw new InvalidParamException("Category.categoryName");
        if (description == null) throw new InvalidParamException("Category.description");

        this.categoryToken = TokenGenerator.randomCharacterWithPrefix(CATEGORY_PREFIX);
        this.categoryName = categoryName;
        this.description =  description;
        this.level = Level.PRIMARY_CATEGORY;
    }

    @Builder(builderClassName = "categoryWithParentBuilder" ,builderMethodName = "categoryWithParentBuilder")
    public Category (Category parent, String categoryName, String description) {
        if (parent == null) throw new InvalidParamException("Category.parent");
        if (categoryName == null) throw new InvalidParamException("Category.categoryName");
        if (description == null) throw new InvalidParamException("Category.description");

        this.categoryToken = TokenGenerator.randomCharacterWithPrefix(CATEGORY_PREFIX);
        this.categoryName = categoryName;
        this.description =  description;
        this.level = Level.SECONDARY_CATEGORY;

        setParent(parent);
    }
    //==비즈니스 로직==//
    public void changeInfo(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }


    //==조회 로직==//
    public Long getParentId() {
        if (this.parent == null)
            return null;

        return this.parent.getId();
    }
}