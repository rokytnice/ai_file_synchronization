package org.rochlitz.ai;


import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OpenAIClientTest {

    private OpenAIClient openAIClient;
    private OkHttpClient mockHttpClient;
    private Call mockCall;
    private Response mockResponse;

    @BeforeEach
    public void setUp() {
        openAIClient = new OpenAIClient();

        // Mock OkHttpClient
        mockHttpClient = mock(OkHttpClient.class);
        mockCall = mock(Call.class);
        mockResponse = mock(Response.class);
    }

    @Test
    public void testRequestAiImplementation_SuccessfulResponse() throws IOException {
        // Given
        String requestBody = "schreibe eine neue junit 5 test klasse, die linien zeichnet";

        // Mock the response body
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        when(mockResponseBody.string()).thenReturn("Mocked response body with test class code");

        // Set up the successful response behavior
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        // Capture the request sent
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        // When
        openAIClient.requestAiImplementation(requestBody);

        // Then
        verify(mockHttpClient).newCall(requestCaptor.capture());
        Request capturedRequest = requestCaptor.getValue();

        // Verify that the request contains the correct API URL and body
        assertEquals("https://api.openai.com/v1/chat/completions", capturedRequest.url().toString());
        assertEquals("POST", capturedRequest.method());

        // Verify response handling logic
        verify(mockResponseBody).string();
        assertTrue(mockResponse.isSuccessful());
    }

    @Test
    public void testRequestAiImplementation_FailedResponse() throws IOException {
        // Given
        String requestBody = "Erzeuge mir eine java junit test klasse.";

        // Set up the failed response behavior
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockResponse.code()).thenReturn(400);
        when(mockResponse.message()).thenReturn("Bad Request");
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            openAIClient.requestAiImplementation(requestBody);
        });

        // Assert the exception message
        assertTrue(exception.getMessage().contains("400"));
    }
}
