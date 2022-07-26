package com.nttdata.mstransfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "schema_suscribe.transfer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    @Id
    private String id;
    private String idOrigin;
    private String idDestination;
    private BigDecimal amount;
}
