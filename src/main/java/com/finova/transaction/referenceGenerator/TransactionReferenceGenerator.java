package com.finova.transaction.referenceGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TransactionReferenceGenerator {

    private TransactionReferenceGenerator() {
    }

    public static String generate() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String uniquePart = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        return String.format(
                "TXN-%s-%s",
                date,
                uniquePart
        );
    }
}
