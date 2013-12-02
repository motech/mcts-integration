package mcts.integration.stub.interceptor;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Request Url: %s?%s", request.getRequestURI(), request.getQueryString()));

        stringBuilder.append("\n Request Headers: ");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            stringBuilder.append(String.format("\n %s: %s", headerName, headerValue));
        }

        stringBuilder.append(String.format("\n Request Body: %s", IOUtils.toString(request.getInputStream())));

        LOGGER.info(stringBuilder.toString());
        return true;
    }
}
