package com.chernik.internetprovider.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pageable {
    private int pageNumber;
    private int pageSize;
}
