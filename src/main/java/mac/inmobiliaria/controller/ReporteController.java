package mac.inmobiliaria.controller;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;

@Controller
public class ReporteController {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@GetMapping("/report/proforma")
	public void reporteprueba(@RequestParam("id") String idproforma, HttpServletRequest request, HttpServletResponse response, Authentication auth) throws Exception {

		
		Connection connection = null;
		//String rutaArchivo ="c://miruta_file/proforma_cliente.jrxml"; // 
		try {
			String rutaFile = request.getSession().getServletContext().getRealPath("/proforma_cliente.jrxml");
			String rutaArchivo = request.getSession().getServletContext().getRealPath("");
			JasperReport jasperReport = JasperCompileManager.compileReport(rutaFile);
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("P_ID_PROFORMA", idproforma);
			parametros.put("P_RUTA", rutaArchivo);

			byte[] reporte = null;

			connection = this.jdbcTemplate.getDataSource().getConnection();

			if (connection != null) {
				System.out.println("[CONECTADA] " + connection.getMetaData().getMaxTableNameLength());
				System.out.println("[CONECTADA] " + connection.getMetaData().getDriverName());
				System.out.println("[CONECTADA] " + connection.getMetaData().getDatabaseProductName());
				System.out.println("[CONECTADA] " + connection.getMetaData().getUserName());
				System.out.println("[CONECTADA] " + connection.getMetaData().getMaxTablesInSelect());

				System.out.println("[connection connection] " + connection);
			}

			reporte = JasperRunManager.runReportToPdf(jasperReport, parametros, connection);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=CopiaInformativa.pdf");
			response.setHeader("Cache-Control", "max-age=30");
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setContentLength(reporte.length);
			ServletOutputStream out = response.getOutputStream();
			out.write(reporte, 0, reporte.length);
			out.flush();
			out.close();

		} catch (Exception e) {
			System.out.println("ERROR! reporte propuesta " + e.toString());
			System.out.println("ERROR! reporte propuesta " + e);
			response.setContentType("text/html");
			PrintWriter pw = response.getWriter();
			pw.println("<html>");
			pw.println("<head><title>Pagina de error</title></title>");
			pw.println("<body>");
			pw.println("<h2>Se produjo un error inesperado</h2>");
			pw.println("</body></html>");
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.out.println("ERROR! EN cerrar conexion");
				}
			}
		}

	}


}
