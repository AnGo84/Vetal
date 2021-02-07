package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractContragentController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.service.ClientServiceImpl;

import java.util.Map;

@Controller
@RequestMapping("/clients")
@Slf4j
public class ClientController extends AbstractContragentController<Client, ClientServiceImpl> {
	public ClientController(ClientServiceImpl service, Map<String, ViewFilter> viewFilterMap) {
		super(Client.class, ControllerType.CLIENT, service, viewFilterMap);
	}
}
