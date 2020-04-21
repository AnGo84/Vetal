package ua.com.vetal.service.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.ExternalResource;

import javax.mail.internet.MimeMessage;

@Slf4j
public class SmtpServerRule extends ExternalResource {

    private GreenMail smtpServer;
    private int port;

    public SmtpServerRule(int port) {
        this.port = port;

    }

    @Override
    protected void before() throws Throwable {

        super.before();
        smtpServer = new GreenMail(new ServerSetup(port, null, "smtp"));
        smtpServer.setUser("username", "secret");
        smtpServer.start();
        log.info("Before. Start SmtpServer");
        log.info("SmtpServer: {}", smtpServer.getSmtp().getServerSetup());
    }

    public MimeMessage[] getMessages() {
        return smtpServer.getReceivedMessages();
    }

    @Override
    protected void after() {
        super.after();
        smtpServer.stop();
        log.info("After. Stop SmtpServer");
    }
}
