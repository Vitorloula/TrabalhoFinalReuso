package com.sebastian.inventory_management.mapper;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utilitário genérico para conversão de Page de entidades para Page de DTOs.
 * Elimina a necessidade do método toDTOPage() repetido em cada Mapper.
 * 
 * Padrão aplicado: Extract Class + Generics
 * 
 * Uso:
 * 
 * <pre>
 * Page<ProductResponseDTO> dtos = PageMapperUtil.toPage(entitiesPage, productMapper::toDTO);
 * </pre>
 */
public final class PageMapperUtil {

    private PageMapperUtil() {
        // Classe utilitária - não instanciável
    }

    /**
     * Converte uma Page de entidades para uma Page de DTOs usando a função de
     * mapeamento fornecida.
     * 
     * @param <E>    Tipo da entidade de origem
     * @param <D>    Tipo do DTO de destino
     * @param page   Page de entidades a ser convertida
     * @param mapper Função de mapeamento (ex: productMapper::toDTO)
     * @return Page de DTOs
     */
    public static <E, D> Page<D> toPage(Page<E> page, Function<E, D> mapper) {
        List<D> dtoList = page.getContent().stream()
                .map(mapper)
                .toList();
        return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
    }
}
