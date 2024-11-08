package com.learn.finance.model.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails {

    String bankOrInstitution;
    String city;
    String swiftCode;
}
