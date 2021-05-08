package by.dach.app.service;

import by.dach.app.model.Car;
import by.dach.app.model.CarDTO;
import by.dach.app.model.CarServiceInfo;
import by.dach.app.model.Transmission;
import by.dach.app.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car saveCar(CarDTO carDTO) {
        Car car = new Car();
        car.setYear(carDTO.getYear());
        car.setModel(carDTO.getModel());
        car.setVolume(carDTO.getVolume());
        car.setTransmission(carDTO.getTransmission());
        car.setPrice(carDTO.getPrice());
        car.setBodyType(carDTO.getBodyType());
        car.setVin(carDTO.getVin());
        car.setCarServiceInfo(new CarServiceInfo(LocalDateTime.now(), carDTO.getModel() + carDTO.getBodyType()));

        return carRepository.save(car);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public List<Car> findAfterYear(int year) {
        return carRepository.findCarByYearAfter(year);
    }

    public List<Car> findByYearIntervalAndModel(int firstYear, int secondYear, String model) {
        return carRepository.findCarByYearBetweenAndModelEquals(firstYear, secondYear, model);
    }

    public List<Car> findByPartModelName(String partOfName) {
        return carRepository.findCarByName(partOfName);
    }

    public List<Car> findYoungerYear(int year) {
        return carRepository.findCarYoungerYear(year);
    }

    //получаем авто опред г.в. с помощью stream api (хоть это и неуместно здесь)
    public List<Car> findByYear(int year) {
        Map<Integer, List<Car>> map = carRepository.findAll().stream()
                .collect(Collectors.groupingBy(Car::getYear));
        return map.get(year);
    }

    //все авто с сортировкой по г.в. от старшего
    public List<Car> findAllSortedByYear() {
        return carRepository.findAll().stream()
                .sorted((c1, c2) -> c2.getYear() - c1.getYear()).collect(Collectors.toList());
    }


    public Map<Transmission, Integer> findPriceAllByTransmission() {
        return carRepository.findAll().stream()
                .collect(Collectors.groupingBy(Car::getTransmission, Collectors
                        .reducing(0, Car::getPrice, Integer::sum)));
//        for (Map.Entry<Transmission, Integer> temp : map.entrySet()) {
//            System.out.println(temp.getKey() + " " + temp.getValue());
//        }
    }

    public Map<Transmission, List<Car>> findByTransmissionType() {
        return carRepository.findAll().stream()
                .collect(Collectors.groupingBy(Car::getTransmission));
    }

    public Map<Integer, List<Car>> findByVolume() {
        Map<Integer, List<Car>> map = carRepository.findAll().stream()
                .collect(Collectors.groupingBy(Car::getVolume));
        return map;
    }

    public List<Car> findByTransmissionTypeWithNativeQuery(String tr_type) {
        return carRepository.findCarByTransmissionType(tr_type);
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