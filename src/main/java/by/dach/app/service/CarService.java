package by.dach.app.service;

import by.dach.app.mappers.EntityMapper;
import by.dach.app.model.*;
import by.dach.app.model.dto.CarFormDto;
import by.dach.app.model.dto.CarListDto;
import by.dach.app.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final EntityMapper entityMapper;

    @Autowired
    public CarService(CarRepository carRepository, EntityMapper entityMapper) {
        this.carRepository = carRepository;
        this.entityMapper = entityMapper;
    }

    public Car saveCar(CarFormDto carFormDto) {
        //Car car = CarMapper.INSTANCE.carDtoToCar(carDTO);
        Car car = entityMapper.carDtoToCar(carFormDto);
        car.setCarServiceInfo(new CarServiceInfo(LocalDateTime.now(), carFormDto.getModel() + carFormDto.getBodyType()));
        return carRepository.save(car);
    }

    public List<CarListDto> findAll() {
        return carRepository.findAll().stream().map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }

    public List<CarListDto> findAfterYear(int year) {
        return carRepository.findCarByYearAfter(year).stream().map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }

    public List<CarListDto> findByYearIntervalAndModel(int firstYear, int secondYear, String model) {
        return carRepository.findCarByYearBetweenAndModelEquals(firstYear, secondYear, model).stream().map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }

    public List<CarListDto> findByPartModelName(String partOfName) {
        return carRepository.findCarByName(partOfName).stream().map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }

    public List<CarListDto> findYoungerYear(int year) {
        return carRepository.findCarYoungerYear(year).stream().map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }

    //получаем авто опред г.в. с помощью stream api (хоть это и неуместно здесь)
    public List<CarListDto> findByYear(int year) {
        Map<Integer, List<Car>> map = carRepository.findAll().stream()
                .collect(Collectors.groupingBy(Car::getYear));
        return map.get(year).stream().map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }

    //все авто с сортировкой по г.в. от старшего
    public List<CarListDto> findAllSortedByYear() {
        return carRepository.findAll().stream()
                .sorted((c1, c2) -> c2.getYear() - c1.getYear()).map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }


    public Map<Transmission, Integer> findPriceAllByTransmission() {
        return carRepository.findAll().stream()
                .collect(Collectors.groupingBy(Car::getTransmission, Collectors
                        .reducing(0, Car::getPrice, Integer::sum)));
    }

    public Map<Transmission, List<CarListDto>> findByTransmissionType() {
        return carRepository.findAll().stream().map(entityMapper::carToCarViewDto)
                .collect(Collectors.groupingBy(CarListDto::getTransmission));
    }

    public Map<Integer, List<CarListDto>> findByVolume() {
        return carRepository.findAll().stream().map(entityMapper::carToCarViewDto)
                .collect(Collectors.groupingBy(CarListDto::getVolume));
    }

    public List<CarListDto> findByTransmissionTypeWithNativeQuery(String tr_type) {
        return carRepository.findCarByTransmissionType(tr_type).stream().map(entityMapper::carToCarViewDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteCarById(int id) {
        carRepository.deleteCarById(id);
    }

    @Transactional
    public void editCarPriceById(int id, int price) {
        carRepository.editCarPriceById(id, price);
    }
}