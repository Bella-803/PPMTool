package com.learnspringboot.ppmtool.services;

import com.learnspringboot.ppmtool.domain.Backlog;
import com.learnspringboot.ppmtool.domain.Project;
import com.learnspringboot.ppmtool.domain.User;
import com.learnspringboot.ppmtool.exceptions.ProjectIdException;
import com.learnspringboot.ppmtool.exceptions.ProjectNotFoundException;
import com.learnspringboot.ppmtool.repositories.BacklogRepository;
import com.learnspringboot.ppmtool.repositories.ProjectRepository;
import com.learnspringboot.ppmtool.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdate(Project project, String username) {
    	
    	if(project.getId() != null) {
    		Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
    		
    		if(existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
    			throw new ProjectNotFoundException("Project Not Found in your account");
    			
    		}else if(existingProject == null) {
    			throw new ProjectNotFoundException("Project with : "+project.getProjectIdentifier()+"can not be updated because it doesn t exist");
    		}
    	}
    	
    	String identifier = project.getProjectIdentifier().toUpperCase();
        try {
        	User user = userRepository.findByUsername(username);
        	project.setUser(user);
        	project.setProjectLeader(user.getUsername());
        	
            project.setProjectIdentifier(identifier);
            
            if(project.getId() == null) {
            	Backlog backlog = new Backlog();
            	project.setBacklog(backlog);
            	backlog.setProject(project);
            	backlog.setProjectIdentifier(identifier);
            }
            
            if(project.getId() != null) {
            	project.setBacklog(backlogRepository.findByProjectIdentifier(identifier));
            }
            
            return projectRepository.save(project);
            
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "'already exist");
        }
    }

    public Project findProjectById(String projectId, String username){
    	//Return only the project if the user looking for it is the owner
    	
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project ID: '"+projectId+"' does not exist");
        }
        
        if(!project.getProjectLeader().equals(username)) {
        	throw new ProjectNotFoundException("Project not found in your account");
        }
        
        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }
    

    public void deleteProject(String projectId, String username){
    	
        Project project = findProjectById(projectId, username);

        projectRepository.delete(project);
    }
}
