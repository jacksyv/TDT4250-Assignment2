package assignment2.converter.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;

import assignment2.converter.api.ConversionInterface;
import assignment2.converter.api.Converter;
import assignment2.converter.api.ConverterResult;
import assignment2.converter.api.Unit;

@Component
@HttpWhiteboardServletPattern("/convert/*")
public class ConversionServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	
	private static final String VALUE_PARAM = "v";
	private static final String FROM_PARAM = "from";
	private static final String TO_PARAM = "to";


	@Reference(
			cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC,
			bind = "addConversion",
			unbind = "removeConversion"
	)
	public void addConversion(ConversionInterface conversion) {
		Converter.addConversion(conversion);
	}
	public void removeConversion(ConversionInterface conversion) {
		Converter.removeConversion(conversion);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (!parameterMap.containsKey(VALUE_PARAM)
				|| !parameterMap.containsKey(FROM_PARAM)
				|| !parameterMap.containsKey(TO_PARAM)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Request must contain all three parameters: "
							+ VALUE_PARAM + ", " + FROM_PARAM + " and " + TO_PARAM);
			return;
		}

		String value = request.getParameter(VALUE_PARAM);
		Unit startUnit = Converter.getUnit(request.getParameter(FROM_PARAM));
		Unit endUnit = Converter.getUnit(request.getParameter(TO_PARAM));
		if (startUnit == null || endUnit == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Request must contain valid units");
			return;
		}

		ConverterResult result = Converter.convertUnit(startUnit.getSymbol(), endUnit.getSymbol(), value);
		sendResponse(result, response, startUnit.getUnit(), endUnit.getUnit(), value);	
	}
	
	
	private static void sendResponse(ConverterResult result, HttpServletResponse response, String startUnit, String endUnit, String value)
			throws ServletException, IOException {

		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		writer.println("--- Conversion from " + startUnit + " to " + endUnit + " ---");
		writer.println("\t\t" + result.getMessage());
	}
	

}