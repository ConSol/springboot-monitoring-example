package de.consol.RestServiceDemo.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.internal.ReaderWriter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@PreMatching
public class MyPreMatchingRequestFilter implements ContainerRequestFilter {
  private final Logger log = LogManager.getLogger(MyPreMatchingRequestFilter.class);

  @Override
  public void filter(ContainerRequestContext requestContext) {
    log.debug(">>> MyPreMatchingRequestFilter");

    log.debug("Entering in Resource : /{} ", requestContext.getUriInfo().getPath());
    log.debug("Cookies: " +requestContext.getCookies());
    log.debug("Content length path: " +requestContext.getLength());
    log.debug("Request URL: " +requestContext.getUriInfo().getAbsolutePath());
    logQueryParameters(requestContext);
    logRequestHeader(requestContext);

    //log entity stream...
    String entity = readEntityStream(requestContext);
    if (null != entity && entity.trim().length() > 0) {
      log.debug("Entity Stream : {}", entity);
    }
  }

  private void logQueryParameters(ContainerRequestContext requestContext) {
    Iterator iterator = requestContext.getUriInfo().getPathParameters().keySet().iterator();
    while (iterator.hasNext()) {
      String name = String.valueOf(iterator.next());
      List obj = requestContext.getUriInfo().getPathParameters().get(name);
      String value = null;
      if (null != obj && obj.size() > 0) {
        value = String.valueOf(obj.get(0));
      }
      log.debug("Query Parameter Name: {}, Value :{}", name, value);
    }
  }


  private void logRequestHeader(ContainerRequestContext requestContext) {
    Iterator iterator;
    log.debug("----Start Header Section of request ----");
    log.debug("Method Type : {}", requestContext.getMethod());
    iterator = requestContext.getHeaders().keySet().iterator();
    while (iterator.hasNext()) {
      String headerName = String.valueOf(iterator.next());
      String headerValue = requestContext.getHeaderString(headerName);
      log.debug("Header Name: {}, Header Value :{} ", headerName, headerValue);
    }
    log.debug("----End Header Section of request ----");
  }

  private String readEntityStream(ContainerRequestContext requestContext) {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    final InputStream inputStream = requestContext.getEntityStream();
    final StringBuilder builder = new StringBuilder();
    try {
      ReaderWriter.writeTo(inputStream, outStream);
      byte[] requestEntity = outStream.toByteArray();
      if (requestEntity.length == 0) {
        builder.append("");
      } else {
        builder.append(new String(requestEntity));
      }
      requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
    } catch (IOException ex) {
      log.debug("----Exception occurred while reading entity stream :{}", ex.getMessage());
    }
    return builder.toString();
  }
}