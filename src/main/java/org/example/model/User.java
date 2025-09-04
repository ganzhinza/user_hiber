package org.example.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    private Integer age;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public User(){
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, String email, Integer age, LocalDateTime createdAt){
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format(
                """
                        User #%d
                           ├─ Name: %s
                           ├─ Email: %s
                           ├─ Age: %s
                           └─ Created: %s
                           └─ Type: %s""",
                id,
                name,
                email,
                age != null ? age + " years" : "not specified",
                createdAt.format(formatter),
                this.getClass().getSimpleName()
        );
    }

    public void setId(Long id) {
        this.id = id;
    }
}
