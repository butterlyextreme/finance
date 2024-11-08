package com.learn.finance.model.producer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
  @JsonProperty("first_name")
  String firstName;

  @JsonProperty("last_name")
  String lastName;

  int score;

}
