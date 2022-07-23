package com.nttdata.mssignatories.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "schema_suscribe.signatories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Signatories {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String docNumber;

    private String accountId;
}
