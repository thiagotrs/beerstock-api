package one.digitalinnovation.beerstock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerStockExceededException;
import one.digitalinnovation.beerstock.mapper.BeerMapper;
import one.digitalinnovation.beerstock.repository.BeerRepository;

@Service
public class BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    public BeerService(final BeerRepository beerRepository, final BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    public List<BeerDTO> listAll() {
        return beerRepository.findAll().stream().map(beerMapper::toDTO).collect(Collectors.toList());
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer beer = beerRepository.findByName(name).orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toDTO(beer);
    }

    public BeerDTO create(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        if(beerRepository.findByName(beerDTO.getName()).isPresent())
            throw new BeerAlreadyRegisteredException(beerDTO.getName());

        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        beerRepository.findById(id).orElseThrow(() -> new BeerNotFoundException(id));
        beerRepository.deleteById(id);
    }

    public void incrementStock(Long id, Integer quantityToIncrement) throws BeerNotFoundException, BeerStockExceededException {
        Beer beer = beerRepository.findById(id).orElseThrow(() -> new BeerNotFoundException(id));

        if (quantityToIncrement + beer.getQuantity() > beer.getMax()) {
            throw new BeerStockExceededException(id, quantityToIncrement);
        }

        beer.setQuantity(beer.getQuantity() + quantityToIncrement);
        beerRepository.save(beer);
    }

}