package by.dach.app.model;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    @Column(name = "creation_time")
    private LocalDateTime creationAt;

    public User(int id, String name, int age, Car car, LocalDateTime creationAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.car = car;
        this.creationAt = creationAt;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDateTime getCreationAt() {
        return creationAt;
    }

    public void setCreationAt(LocalDateTime creationAt) {
        this.creationAt = creationAt;
    }
}


