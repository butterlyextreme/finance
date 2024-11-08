package com.learn.finance.model.producer;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bank {

    String bankOrInstitution;
    String city;
    String swiftCode;
    @JsonProperty("cached_on_system")
    boolean cached;
}
