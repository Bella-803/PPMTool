package com.learnspringboot.ppmtool.repositories;

import com.learnspringboot.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
    public Project findByProjectIdentifier(String projectId);

    public Iterable<Project> findAllByProjectLeader(String username);    
    

}
