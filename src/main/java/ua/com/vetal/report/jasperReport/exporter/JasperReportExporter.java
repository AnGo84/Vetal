package ua.com.vetal.report.jasperReport.exporter;

import lombok.Getter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import ua.com.vetal.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@Getter
public abstract class JasperReportExporter {
	public static final String DEFAULT_FILE_NAME = "Export";
	public static final String UTF_8 = "UTF-8";
	protected JasperReportExporterType type;
	protected JasperPrint jasperPrint;

	public abstract void exportToResponseStream(String outputFileName,
												HttpServletResponse response) throws JRException, IOException;

	public abstract ByteArrayOutputStream exportToStream() throws JRException, IOException;

	public String getMediaType() {
		return type.getMediaType().getMediaType();
	}

	public OutputStream getOutputStream(String outputFileName, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding(UTF_8);
		response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(prepareOutputFileName(outputFileName), UTF_8));
		response.setContentType(type.getMediaType().getMediaType());
		return response.getOutputStream();
	}

	private String prepareOutputFileName(String outputFileName) {
		if (StringUtils.isEmpty(outputFileName)) {
			outputFileName = DEFAULT_FILE_NAME;
		}
		return outputFileName + "." + type.getFileExtension();
	}

}
