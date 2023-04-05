package com.ossovita.reservationservice.business.abstracts.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    /*
     * This interceptor provide us to put authorization header for all feign client requests.
     * It takes available authorization header from current context
     * */
    @Override
    public void apply(RequestTemplate template) {
        if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authorization = request.getHeader("Authorization");
            log.info("Reservation service feign header:" + authorization);
            if (StringUtils.isNotBlank(authorization)) {
                template.header("Authorization", authorization);
            }
        }
    }


}
