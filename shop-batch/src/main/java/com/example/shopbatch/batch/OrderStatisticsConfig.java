package com.example.shopbatch.batch;

import com.example.shopbatch.domain.order.Order;
import com.example.shopbatch.domain.order.OrderStatistics;
import com.example.shopbatch.repositiory.order.OrderRepository;
import com.example.shopbatch.repositiory.order.OrderStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class OrderStatisticsConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final OrderRepository orderRepository;
    private final OrderStatisticsRepository orderStatisticsRepository;

    /*public OrderStatisticsConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }*/

    @Bean
    public Job orderStatisticsJob() throws Exception {
        return this.jobBuilderFactory.get("orderStatistics")
                .incrementer(new RunIdIncrementer())
                .start(taskBaseStep())
                .build();
    }

    @Bean
    public Step taskBaseStep() {
        return stepBuilderFactory.get("taskBaseStep")
                .tasklet(this.tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public Tasklet tasklet(@Value("#{jobParameters[date]}") String date) {

        return (contribution, chunkContext) -> {
            final LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            final LocalDateTime from =  LocalDateTime.of(localDate, LocalTime.MIN);
            final LocalDateTime to = LocalDateTime.of(localDate, LocalTime.MAX);

            List<Order> orders = orderRepository.findByCreatedAtBetween(from, to);
            long calculateTotalAmount = orders.stream()
                    .mapToLong(Order::calculateTotalAmount)
                    .sum();

            long calculateTotalCount = orders.stream()
                    .mapToLong(Order::calculateTotalCount)
                    .sum();

            Optional<OrderStatistics> orderStatistics = orderStatisticsRepository.findByDate(localDate);
            if(orderStatistics.isPresent()){
                OrderStatistics newOrderStatistics = orderStatistics.get();
                newOrderStatistics.change(calculateTotalAmount, calculateTotalCount);
            } else {
                OrderStatistics newOrderStatistics = new OrderStatistics(calculateTotalAmount, calculateTotalCount, date);
                orderStatisticsRepository.save(newOrderStatistics);
            }

            return RepeatStatus.FINISHED;
        };
    }
}
