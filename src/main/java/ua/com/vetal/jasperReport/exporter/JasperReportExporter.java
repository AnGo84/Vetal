package ua.com.vetal.jasperReport.exporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public abstract class JasperReportExporter {
	protected JasperReportExporterType type;
	protected JasperPrint jasperPrint;
	protected String name;
	protected HttpServletResponse response;

	public abstract void exportToStream() throws JRException, IOException;

	public OutputStream getOutputStream() throws IOException {
		response.setHeader("Content-Disposition", "inline; filename=" + prepareOutputFileName());
		response.setContentType(type.getMediaType().getMediaType());
		//response.setContentLength(outputStream.size());
		return response.getOutputStream();
	}

	private String prepareOutputFileName() {
		return name + "." + type.getFileExtension();
	}
}
