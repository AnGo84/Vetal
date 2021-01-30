package ua.com.vetal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.TaskDAO;
import ua.com.vetal.email.EmailAttachment;
import ua.com.vetal.email.EmailMessage;
import ua.com.vetal.entity.DBFile;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.utils.StringUtils;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service("taskService")
@Transactional
@Slf4j
public class TaskServiceImpl implements SimpleService<Task> {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskDAO taskDAO;
	@Autowired
	private DBFileStorageService dbFileStorageService;

	@Override
	public Task findById(Long id) {
		return taskRepository.getOne(id);
	}

	@Override
	public Task findByName(String name) {
		return null;
	}

	@Override
	public void saveObject(Task task) {
		taskRepository.save(task);
	}

	@Override
	public void updateObject(Task task) {
		saveObject(task);
	}

	public void updateObject(Task task, DBFile dbFile) throws IOException {
		Long oldFileId = null;
		if (task.getDbFile() != null && (StringUtils.isEmpty(task.getFileName()))) {
			oldFileId = task.getDbFile().getId();
			task.setDbFile(null);
		}

		if (dbFile != null && dbFile.getData().length > 0) {
			if (task.getDbFile() != null) {
				oldFileId = task.getDbFile().getId();
			}
			task.setDbFile(dbFile);
		}
		saveObject(task);
		if (oldFileId != null) {
			log.debug("Delete old dbFile by ID= {}", oldFileId);
			dbFileStorageService.deleteById(oldFileId);
		}
	}

	@Override
	public void deleteById(Long id) {
		taskRepository.deleteById(id);
	}

	@Override
	public List<Task> findAllObjects() {
		List<Task> getList = taskRepository.findAll(sortByDateBeginDesc());
		return getList;
	}

	public Task findByAccount(String account) {
		return taskRepository.findByAccount(account);
	}

	public List<Task> findByFilterData(OrderViewFilter orderViewFilter) {
		if (orderViewFilter == null) {
			return findAllObjects();
		}
		List<Task> tasks = taskDAO.findByFilterData(orderViewFilter);
		return tasks;
	}

	@Override
	public boolean isObjectExist(Task task) {
		return findById(task.getId()) != null;
	}

	public boolean isAccountValueExist(Task task) {
		Task findTask = findByAccount(task.getAccount());
		return (findTask != null && findTask.getId() != null && !findTask.getId().equals(task.getId()));
	}

	public Long getMaxID() {
		return taskDAO.getMaxID();
	}

	private Sort sortByDateBeginDesc() {
		return Sort.by(Sort.Direction.DESC, "dateBegin");
	}

	public String taskMailingDeclineReason(Task task) {

		String mailFrom = task.getManager().getEmail();
		if (mailFrom == null || mailFrom.equals("")) {
			return messageSource.getMessage("message.email.miss_from_for_manager",
					null, new Locale("ru")) + " " + task.getContractor().getFullName();
		}

		String mailTo = task.getContractor().getEmail();
		if (mailTo == null || mailTo.equals("")) {
			return messageSource.getMessage("message.email.miss_to_for_contractor",
					null, new Locale("ru")) + " " + task.getManager().getFullName();
		}
		return "";
	}

	public EmailMessage getEmailMessage(Task task) {
		EmailMessage emailMessage = new EmailMessage();
		List<EmailAttachment> attachments = new ArrayList<>();
		String subject = messageSource.getMessage("email.task", null, new Locale("ru"));
		String text = messageSource.getMessage("email.new_task", null, new Locale("ru"));
		if (task.getDbFile() != null) {
			DataSource source = new ByteArrayDataSource(task.getDbFile().getData(), task.getDbFile().getFileType());
			attachments.add(new EmailAttachment(task.getDbFile().getFileName(), source));
		}
		emailMessage.setFrom(task.getManager().getEmail());
		emailMessage.setTo(task.getContractor().getEmail());
		emailMessage.setSubject(subject + task.getNumber());
		emailMessage.setText(text);
		emailMessage.setAttachments(attachments);
		return emailMessage;
	}

}
