package ua.com.vetal.handler;

import ua.com.vetal.entity.Task;

public class TaskHandler {

	public Task copy(Task fromTask) {

		System.out.println("Copy From task:" + fromTask);
		Task task = new Task();
		/*
		 * if (fromTask != null) { task.manager = fromTask.manager;
		 * task.workName = fromTask.workName; task.fileName = fromTask.fileName;
		 * task.contractor = fromTask.contractor; task.production =
		 * fromTask.production; task.dateBegin = fromTask.dateBegin;
		 * task.dateEnd = fromTask.dateEnd; task.client = fromTask.client;
		 * task.stock = fromTask.stock; task.printing = fromTask.printing;
		 * task.chromaticity = fromTask.chromaticity; task.format =
		 * fromTask.format; task.laminate = fromTask.laminate; task.paper =
		 * fromTask.paper; task.cringle = fromTask.cringle; task.carving =
		 * fromTask.carving; task.bending = fromTask.bending; task.assembly =
		 * fromTask.assembly; task.cutting = fromTask.cutting; task.note =
		 * fromTask.note; task.amount = fromTask.amount; }
		 * System.out.println("COpy Return task:" + task);
		 */
		return task;
	}
}
