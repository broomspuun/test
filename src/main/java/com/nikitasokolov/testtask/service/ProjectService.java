package com.nikitasokolov.testtask.service;

import com.nikitasokolov.testtask.entity.Project;
import com.nikitasokolov.testtask.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    @Transactional
    public void saveProject(Project project) {
        if (project.getPhotoFile() != null && !project.getPhotoFile().isEmpty()) {
            String originalFilename = project.getPhotoFile().getOriginalFilename();
            String newFilename = "image_" + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf('.'));
            String imagePath = "src/main/resources/static/images/" + newFilename;
            project.setPhotoPath("/images/" + newFilename);
            try {
                Files.copy(project.getPhotoFile().getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }


    @Transactional
    public void updateProject(Long projectId, Project updatedProject) {
        Project existingProject = projectRepository.findById(projectId).orElse(null);
        if (updatedProject.getPhotoFile() != null && !updatedProject.getPhotoFile().isEmpty())
        {
            String originalFilename = updatedProject.getPhotoFile().getOriginalFilename();
            String newFilename = "image_" + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf('.'));
            String imagePath = "src/main/resources/static/images/" + newFilename;
            existingProject.setPhotoPath("/images/" + newFilename);
            existingProject.setPhotoFile(updatedProject.getPhotoFile());
            try {
                Files.copy(updatedProject.getPhotoFile().getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(updatedProject.isDeletePhoto())
        {
            existingProject.setPhotoPath(null);
            existingProject.setPhotoFile(null);
        }
        existingProject.setName(updatedProject.getName());
        existingProject.setDescription(updatedProject.getDescription());
        projectRepository.save(existingProject);
    }

}

