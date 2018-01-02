package com.chernik.internetprovider.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Page<E> {
    private List<E> data;
    private int pagesCount;
}
