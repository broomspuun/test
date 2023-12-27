package com.nikitasokolov.testtask.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String photoPath;
    @Transient
    private MultipartFile photoFile;
    @Transient
    private boolean deletePhoto;

}