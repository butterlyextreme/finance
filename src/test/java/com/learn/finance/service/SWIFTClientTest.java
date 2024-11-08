package com.learn.finance.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learn.finance.config.DownStreamConfiguration;
import com.learn.finance.config.DownStreamProperties;
import com.learn.finance.model.consumer.BankDetails;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableConfigurationProperties({DownStreamProperties.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SWIFTClientImpl.class})
@ContextConfiguration(
    classes = {DownStreamConfiguration.class, JacksonAutoConfiguration.class,
        WebClientAutoConfiguration.class})
public class SWIFTClientTest {

  public static final String SWIFT_CODE = "CRBAALTRXXX";

  @Autowired
  SWIFTClient SWIFTClientAPI;

  @Autowired
  private ObjectMapper objectMapper;

  private static WireMockServer wireMockServer;

  @BeforeAll
  static void startWireMock() {
    wireMockServer = new WireMockServer(
        options().port(7005).withRootDirectory("src/test/resources/wiremock"));
    wireMockServer.start();
  }

  @AfterAll
  static void stopWireMock() {
    wireMockServer.stop();
  }


  @Test
  public void executeGetBankDetailsBySWIFTCode()  {

   List<BankDetails> bankDetailsList = deserialize(readMockJson("bank_details.json"), new TypeReference<List<BankDetails>>(){});


    StepVerifier.create(SWIFTClientAPI.getBankDetails(SWIFT_CODE))
            .assertNext(response ->
                    assertEquals(bankDetailsList,
                            response))
            .verifyComplete();

  }

  @SneakyThrows
  static String readMockJson(final String name) {
    return new String(Files.readAllBytes(Paths.get(
        SWIFTClientTest.class.getResource("/wiremock/__files/" + name).toURI())));
  }

  public  <T> T deserialize(final String content, final TypeReference<T> valueType) {
    try {
      return objectMapper.readValue(content, valueType);
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  <T> T deserialize(final String content, final Class<T> valueType) {
    try {
      return objectMapper.readValue(content, valueType);
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }
}
