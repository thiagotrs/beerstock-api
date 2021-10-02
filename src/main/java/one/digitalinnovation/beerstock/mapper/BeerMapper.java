package one.digitalinnovation.beerstock.mapper;

import org.mapstruct.Mapper;

import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.Beer;

@Mapper(componentModel = "spring")
public interface BeerMapper {

    public Beer toModel(BeerDTO beerDTO);

    public BeerDTO toDTO(Beer beer);

}