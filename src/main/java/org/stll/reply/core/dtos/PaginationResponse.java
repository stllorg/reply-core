package org.stll.reply.core.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class PaginationResponse<T> {
    private List<T> data;
    private int currentPage;
    private int perPage;
    private long totalItems;
    private long totalPages;
}