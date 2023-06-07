package com.ossovita.clients.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RetrieveMessageErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        try {
            switch (response.status()) {

                case 404 -> {
                    throw new IdNotFoundException(convertFeignResponseToExceptionMessage(response).message());
                }

                case 417 -> {
                    throw new UnexpectedRequestException(convertFeignResponseToExceptionMessage(response).message());
                }

                default -> {
                    return errorDecoder.decode(methodKey, response);
                }
            }
        } catch (IOException e) {
            return new IOException(e);
        }
    }

    public ExceptionMessage convertFeignResponseToExceptionMessage(Response response) throws IOException {

        try (InputStream body = response.body().asInputStream()) {//this line provides us to close input stream automatically in case of exception
            return new ExceptionMessage(
                    (String) response.headers().get("date").toArray()[0],//date
                    response.status(),//status
                    HttpStatus.resolve(response.status()).getReasonPhrase(),//error name
                    /*
                    * Replaceable with just IOUtils.toString(body, StandardCharsets.UTF_8))
                    * Because we may want to see multiple exception messages in one message object in the future
                    * */
                    parseMessageFromJson(IOUtils.toString(body, StandardCharsets.UTF_8)),//message
                    response.request().url());//url

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static String parseMessageFromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);

            return jsonNode.get("message").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
