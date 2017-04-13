package com.franzwong.app.dropwizard.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.franzwong.app.dropwizard.data.TaskRepository;
import com.franzwong.app.dropwizard.model.Task;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang3.StringUtils;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
	
	@Inject
	private TaskRepository taskRepository;

	@GET
	public Response getAllTasks() {

		// pardon any format faux pas - doing this in vi
		List<com.franzwong.app.dropwizard.data.Task> entities = taskRepository.getAll();

		// could be cleaner map
		List<Task> tasks = entities.stream().map((entity) -> {
			Task task = new Task();
			task.setId(entity.getId());
			task.setContent(entity.getContent());
			task.setUserName(entity.getUserName());
			return task;
		}).collect(Collectors.toList());

		return Response.ok().entity(tasks).build();
	}

	@GET
	@Path("{userName}")
	public Response getTasks(@PathParam("userName") String userName) {

		// pardon any format faux pas - doing this in vi
 		List<com.franzwong.app.dropwizard.data.Task> entities = taskRepository.findByUserName(userName);

		// could be cleaner map		
		List<Task> tasks = entities.stream().map((entity) -> {
			Task task = new Task();
            task.setId(entity.getId());
			task.setContent(entity.getContent());
			task.setUserName(entity.getUserName());
			return task;
		}).collect(Collectors.toList());
		
		return Response.ok().entity(tasks).build();
	}
	

	@POST
	@Valid
	@Transactional
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addTask(@NotNull Task task) {
		com.franzwong.app.dropwizard.data.Task entity = new com.franzwong.app.dropwizard.data.Task();
		entity.setContent(task.getContent());
		entity.setUserName(task.getUserName());
		
		com.franzwong.app.dropwizard.data.Task persistedEntity = taskRepository.addTask(entity);
		
		Task persistedTask = new Task();
		persistedTask.setId(persistedEntity.getId());
		persistedTask.setContent(persistedEntity.getContent());
		persistedTask.setUserName(persistedEntity.getUserName());
		
		return Response.ok().entity(persistedTask).build();
	}
}
