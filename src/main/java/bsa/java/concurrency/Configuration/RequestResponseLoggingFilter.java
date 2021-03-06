package bsa.java.concurrency.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@Order(2)
public class RequestResponseLoggingFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        log.info("REQUEST [{} {}]", req.getMethod(), req.getRequestURI());
        chain.doFilter(request, response);

        if (res.getStatus() != HttpStatus.NO_CONTENT.value() &&
            res.getStatus() != HttpStatus.CREATED.value() &&
                res.getContentType() == null) {
            log.info("RESPONSE [Error]");
            return;
        }

        log.info("RESPONSE [{}]", res.getContentType());
    }

}
