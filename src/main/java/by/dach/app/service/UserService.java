package by.dach.app.service;

import by.dach.app.mappers.EntityMapper;
import by.dach.app.model.BodyType;
import by.dach.app.model.Transmission;
import by.dach.app.model.User;
import by.dach.app.model.dto.UserFormDto;
import by.dach.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, EntityMapper entityMapper) {
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(UserFormDto userFormDto, int id) {
        User user = entityMapper.userFormDtoToUser(userFormDto);
        user.setId(id);
        log.info("Entity User successful update in to database: {}, {}", "name " + user.getName(), "age" + user.getAge());
        return userRepository.save(user);
    }

    @Transactional
    public User saveNewUser(UserFormDto userFormDto) {
        User user = entityMapper.userFormDtoToUser(userFormDto);
        log.info("Entity User successful save in to database: {}, {}", "name " + user.getName(), "age" + user.getAge());
        return userRepository.save(user);
    }

    @Transactional
    public User editUser(UserFormDto userFormDto, int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found User in the database"));
        entityMapper.updateUserFromUserFormDto(userFormDto, user);
//        Car car = new Car();
//        car.setId(userFormDto.getCarId());
//        user.setCar(car);
        log.info("Edit user in to database name: {}, age: {}", user.getName(), user.getAge());
        return userRepository.save(user);
    }


    @Transactional
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
        log.info("Entity User successful delete from database ID: {}", id);
    }

    public UserFormDto findUserById(int id) {
        return entityMapper.userToUserFormDto(userRepository.getOne(id));
    }

    public List<User> findAllUserByCarTransmissionType(Transmission transmissionType) {
        return userRepository.findAllUserByCarTransmissionType(transmissionType);
    }

    public List<User> findAllUserByCarBodyType(BodyType bodyType) {
        return userRepository.findAllUserByCarBodyType(bodyType);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> findAllUserWhereCarVolumeBigger(Pageable pageable, int volume) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return userRepository.findAllUserWhereCarVolumeBigger(pageable, volume);
    }
}
