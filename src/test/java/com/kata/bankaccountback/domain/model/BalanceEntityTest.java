package com.kata.bankaccountback.domain.model;

import com.kata.bankaccountback.domain.model.entity.BalanceEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

public class BalanceEntityTest {

    @Test
    void should_equals_return_true_when_entity_are_equals() {
        BalanceEntity entity1 = createBalanceEntity(1L, LocalDate.now(), BigDecimal.TEN);
        BalanceEntity entity2 = createBalanceEntity(1L, LocalDate.now(), BigDecimal.TEN);

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(entity1).isEqualTo(entity2);
        softAssert.assertThat(entity1).hasSameHashCodeAs(entity2);
        softAssert.assertAll();
    }

    @ParameterizedTest
    @MethodSource("balanceProviderIncorrect")
    void should_equals_return_false_when_entity_are_not_equals(BalanceEntity entityIncorrect) {
        BalanceEntity entity1 = createBalanceEntity(1L, LocalDate.now(), BigDecimal.TEN);

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(entity1).isNotEqualTo(entityIncorrect);
        softAssert.assertThat(entity1).doesNotHaveSameHashCodeAs(entityIncorrect);
        softAssert.assertAll();
    }

    @Test
    void should_equals_return_false_when_one_entity_is_null_or_not_same_class() {
        BalanceEntity entity1 = createBalanceEntity(1L, LocalDate.now(), BigDecimal.TEN);
        BalanceEntity balanceAnonymous = new BalanceEntity() { };


        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(entity1).isNotEqualTo(null);
        softAssert.assertThat(entity1).isNotEqualTo(balanceAnonymous);
        softAssert.assertAll();
    }
    private static Stream<Arguments> balanceProviderIncorrect() {
        Long wrongId = 0L;
        LocalDate wrongDate = LocalDate.now().plusDays(1L);
        BigDecimal wrongBalance = BigDecimal.valueOf(1000);

        return Stream.of(
                Arguments.of(createBalanceEntity(wrongId, LocalDate.now(), BigDecimal.TEN)),
                Arguments.of(createBalanceEntity(1L, wrongDate,BigDecimal.TEN)),
                Arguments.of(createBalanceEntity(1L, LocalDate.now(), wrongBalance))
        );
    }

    private static BalanceEntity createBalanceEntity(long id, LocalDate date, BigDecimal balance) {
        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.setId(id);
        balanceEntity.setDate(date);
        balanceEntity.setBalance(balance);
        return balanceEntity;
    }
}
