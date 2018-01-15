package de.consol.RestServiceDemo.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.Iterator;


public class MyResponseFilter implements ContainerResponseFilter {
  private final Logger log = LogManager.getLogger(MyResponseFilter.class);

  @Override
  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext)
          throws IOException  {
    log.debug(">>> MyResponseFilter");

    log.debug("StatusCode: " +responseContext.getStatus());
    log.debug("StatusInfo : /{} ", responseContext.getStatusInfo());
    log.debug("Cookies: " +responseContext.getCookies());
    log.debug("Content length path: " +responseContext.getLength());
    logRequestHeader(responseContext);

    //log entity stream...
    String entity = readEntityStream(responseContext);
    if (null != entity && entity.trim().length() > 0) {
      log.debug("Entity Stream : {}", entity);
    }
  }


  private void logRequestHeader(ContainerResponseContext responseContext) {
    Iterator iterator;
    log.debug("----Start Header Section of request ----");
    iterator = responseContext.getHeaders().keySet().iterator();
    while (iterator.hasNext()) {
      String headerName = String.valueOf(iterator.next());
      String headerValue = responseContext.getHeaderString(headerName);
      log.debug("Header Name: {}, Header Value :{} ", headerName, headerValue);
    }
    log.debug("----End Header Section of request ----");
  }

  private String readEntityStream(ContainerResponseContext responseContext) {
    return (responseContext.getEntity()!=null)?String.valueOf(responseContext.getEntity()):null;
  }
}