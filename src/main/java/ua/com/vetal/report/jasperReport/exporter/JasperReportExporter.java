package ua.com.vetal.report.jasperReport.exporter;

import lombok.Getter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import ua.com.vetal.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Getter
public abstract class JasperReportExporter {
	public static final String DEFAULT_FILE_NAME = "Export";
	protected JasperReportExporterType type;
	protected JasperPrint jasperPrint;

	public abstract void exportToResponseStream(String outputFileName,
												HttpServletResponse response) throws JRException, IOException;

	public abstract ByteArrayOutputStream exportToStream() throws JRException, IOException;

	public String getMediaType() {
		return type.getMediaType().getMediaType();
	}

	public OutputStream getOutputStream(String outputFileName, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition", "inline; filename=" + prepareOutputFileName(outputFileName));
		response.setContentType(type.getMediaType().getMediaType());
		//response.setContentLength(outputStream.size());
		return response.getOutputStream();
	}

	private String prepareOutputFileName(String outputFileName) {
		if (StringUtils.isEmpty(outputFileName)) {
			outputFileName = DEFAULT_FILE_NAME;
		}
		return outputFileName + "." + type.getFileExtension();
	}

}
