package com.sebastian.inventory_management.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.sebastian.inventory_management.DTO.Category.CategoryRequestDTO;
import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO toDTO(Category category);

    List<CategoryResponseDTO> toDTOList(List<Category> categories);

    default Page<CategoryResponseDTO> toDTOPage(Page<Category> page) {
        List<CategoryResponseDTO> dtoList = toDTOList(page.getContent());
        return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CategoryRequestDTO dto, @MappingTarget Category category);

}
