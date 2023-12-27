package com.nikitasokolov.testtask.controller;

import com.nikitasokolov.testtask.entity.Project;
import com.nikitasokolov.testtask.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;


@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Стартовая страница
    @GetMapping()
    public String showProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects";
    }

    // Нажали "Создать"
    @GetMapping("/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "new-project";
    }

    // Передали данные для создания с формы создания проекта
    @PostMapping("/new")
    public String saveProject(@ModelAttribute Project project,
                              @RequestParam("photoFile") MultipartFile photoFile) {
        if (!photoFile.isEmpty())
            project.setPhotoFile(photoFile);
        projectService.saveProject(project);
        return "redirect:/projects";
    }


    // Редактирование проекта
    @GetMapping("/{projectId}")
    public String showEditForm(@PathVariable Long projectId, Model model) {
        projectService.getProjectById(projectId).ifPresent(project -> model.addAttribute("project", project));
        return "edit-project";
    }

    // Передали данные для редактирования с формы
    @PostMapping("/{projectId}")
    public String editProject(@ModelAttribute Project project,
                              @RequestParam("photoFile") MultipartFile photoFile,
                              @PathVariable Long projectId) throws FileNotFoundException {
        if(!photoFile.isEmpty())
            project.setPhotoFile(photoFile);
        projectService.updateProject(projectId, project);
        return "redirect:/projects";
    }



    @PostMapping("/delete")
    public String deleteProjects(@RequestParam(value="deleteIds", required = false) List<Long> deleteIds) {
        if(deleteIds!=null) {
            for (Long id : deleteIds)
                projectService.deleteProject(id);
        }
        return "redirect:/projects";
    }
}
