package com.learnspringboot.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnspringboot.ppmtool.domain.Backlog;
import com.learnspringboot.ppmtool.domain.Project;
import com.learnspringboot.ppmtool.domain.ProjectTask;
import com.learnspringboot.ppmtool.exceptions.ProjectNotFoundException;
import com.learnspringboot.ppmtool.repositories.BacklogRepository;
import com.learnspringboot.ppmtool.repositories.ProjectRepository;
import com.learnspringboot.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
	
			//PTS to be a specific project, project!=null, bcklog exist
			Backlog backlog = projectService.findProjectById(projectIdentifier, username).getBacklog(); //backlogRepository.findByProjectIdentifier(projectIdentifier);
			
			//set the bl to pt
			projectTask.setBacklog(backlog);
			//we want out project sequence to be like this : IDPRO-1,IDPRO-2, ...
			Integer backlogSequence = backlog.getPTSequence();
		
			//update the BL SEQUENCE
			backlogSequence++;
			backlog.setPTSequence(backlogSequence);
			
			//Add sequence to PT
			projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			
			//Initial priority when priority is null
			if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
				projectTask.setPriority(3);
			}
			
			//Initial status when status is null
			
			if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO-DO");
			}
			
			 return projectTaskRepository.save(projectTask);
			 
	}

	public Iterable<ProjectTask> findBacklogById(String id, String username){
		
		projectService.findProjectById(id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String sequence, String username) {
		
		//make sure you are searching on the right backlog
		Backlog backlog = projectService.findProjectById(backlog_id, username).getBacklog(); //backlogRepository.findByProjectIdentifier(projectIdentifier);
		
		//make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task : "+sequence+" does not exist");
		}
		//make sure that backlog/project id in the path corresponds to the right project
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task : "+sequence+" does not exist in project :"+backlog_id);
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
			
	}
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
		
		projectTaskRepository.delete(projectTask);
	}

}
