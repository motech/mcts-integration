package mcts.integration.stub.interceptor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import mcts.integration.stub.domain.ServiceUpdates;
import mcts.integration.stub.domain.Update;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(RequestLoggingInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("Request Url: %s?%s",
				request.getRequestURI(), request.getQueryString()));

		stringBuilder.append("\n Request Headers: ");
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			stringBuilder.append(String.format("\n %s: %s", headerName,
					headerValue));
		}
		String inputStream = IOUtils.toString(request.getInputStream());
		int index = inputStream.indexOf("serviceupdates");
		String serviceUpdatesXML = inputStream.substring(index-1);
		stringBuilder.append(String.format("\n Request Body: %s",
				inputStream));
		LOGGER.info(stringBuilder.toString());
		JAXBContext jaxbContext = JAXBContext.newInstance(ServiceUpdates.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(serviceUpdatesXML);
		ServiceUpdates serviceUpdates = (ServiceUpdates) unmarshaller
				.unmarshal(reader);
		List<Update> updates = serviceUpdates.getAllUpdates();
		List<String[]> failures = new ArrayList<String[]>();
		for (Update update : updates) {
			if (update.verifyUpdate() != null) {
				failures.add(update.verifyUpdate());
			}
		}
		if (failures.size()>0)
			{for (String[] message : failures)
			{
				LOGGER.error(String.format("Error for mctsId: %s in the field: %s", message[0], message[1]));
			}
			return false;
			}
		return true;
	}
	
/*	public static void main(String[] args)
	{
		String s ="<hjj><ujhkjhajk>";
		System.out.println(s.indexOf('>'));
	}*/
}
