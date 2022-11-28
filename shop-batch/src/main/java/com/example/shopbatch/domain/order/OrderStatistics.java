package com.example.shopbatch.domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orderStatistics")
public class OrderStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private Long count;
    private LocalDate date;

    @Builder
    public OrderStatistics(Long amount, Long count, String date) {
        this.amount = amount;
        this.count = count;
        this.date = LocalDate.parse(date);
    }

    public void change(long calculateTotalAmount, long calculateTotalCount) {
        this.amount = calculateTotalAmount;
        this.count = calculateTotalCount;
    }
}
