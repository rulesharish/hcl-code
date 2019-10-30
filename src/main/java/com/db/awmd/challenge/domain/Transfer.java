package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Transfer {

  @NotNull
  @NotEmpty
  private final String fromAccountId;
  private final String toAccountId;

  @NotNull
  @Min(value = 1, message = "Can Not Transfer amount less than 1.")
  private BigDecimal amountToTransfer;

  /*
  public Transfer(String fromAccountId, String toAccountId,BigDecimal amountToTransfer ) {
    this.fromAccountId = fromAccountId;
    this.toAccountId = toAccountId;
    this.amountToTransfer = amountToTransfer;
  }
*/
 
  @JsonCreator
  public Transfer(@JsonProperty("fromAccountId") String fromAccountId, @JsonProperty("toAccountId") String toAccountId,
    @JsonProperty("amountToTransfer") BigDecimal amountToTransfer) {
    this.fromAccountId = fromAccountId;
    this.toAccountId = toAccountId;
    this.amountToTransfer = amountToTransfer;
  } 
}
