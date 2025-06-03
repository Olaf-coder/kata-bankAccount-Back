package com.kata.bankaccountback.domain.model;

import com.kata.bankaccountback.domain.model.entity.TransactionEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

public class TransactionEntityTest {

    @Test
    void should_equals_return_true_when_entity_are_equals() {
        TransactionEntity entity1 = createTransactionEntity(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.TEN);
        TransactionEntity entity2 = createTransactionEntity(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.TEN);

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(entity1).isEqualTo(entity2);
        softAssert.assertThat(entity1).hasSameHashCodeAs(entity2);
        softAssert.assertAll();
    }

    @ParameterizedTest
    @MethodSource("transactionProviderIncorrect")
    void should_equals_return_false_when_entity_are_not_equals(TransactionEntity entityIncorrect) {
        TransactionEntity entity1 = createTransactionEntity(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.TEN);

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(entity1).isNotEqualTo(entityIncorrect);
        softAssert.assertThat(entity1).doesNotHaveSameHashCodeAs(entityIncorrect);
        softAssert.assertAll();
    }

    @Test
    void should_equals_return_false_when_one_entity_is_null_or_not_same_class() {
        TransactionEntity entity1 = createTransactionEntity(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.TEN);
        TransactionEntity transactionAnonymous = new TransactionEntity() { };


        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(entity1).isNotEqualTo(null);
        softAssert.assertThat(entity1).isNotEqualTo(transactionAnonymous);
        softAssert.assertAll();
    }
    private static Stream<Arguments> transactionProviderIncorrect() {
        Long wrongId = 0L;
        LocalDate wrongDate = LocalDate.now().plusDays(1L);
        BigDecimal wrongBalance = BigDecimal.valueOf(1000);
        BigDecimal wrongDeposit = BigDecimal.valueOf(1000);
        BigDecimal wrongWithdraw = BigDecimal.valueOf(1000);

        return Stream.of(
                Arguments.of(createTransactionEntity(wrongId, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.TEN)),
                Arguments.of(createTransactionEntity(1L, wrongDate, BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.TEN)),
                Arguments.of(createTransactionEntity(1L, LocalDate.now(), wrongDeposit, BigDecimal.TEN, BigDecimal.TEN)),
                Arguments.of(createTransactionEntity(1L, LocalDate.now(), BigDecimal.ZERO, wrongWithdraw, BigDecimal.TEN)),
                Arguments.of(createTransactionEntity(1L, LocalDate.now(), BigDecimal.ZERO, BigDecimal.TEN, wrongBalance))
        );
    }

    private static TransactionEntity createTransactionEntity(long id, LocalDate date, BigDecimal depositAmount, BigDecimal withdrawAmount, BigDecimal balance) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(id);
        transactionEntity.setDate(date);
        transactionEntity.setDepositAmount(depositAmount);
        transactionEntity.setWithdrawAmount(withdrawAmount);
        transactionEntity.setBalance(balance);
        return transactionEntity;
    }
}
