package com.nikitasokolov.testtask.repository;

import com.nikitasokolov.testtask.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
