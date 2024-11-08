package com.learn.finance.model.producer;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
  String id;
  @JsonProperty("creation_time")
  Date creationTime;
  String text;
}
