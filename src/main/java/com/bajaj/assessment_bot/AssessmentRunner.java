package com.bajaj.assessment_bot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Component
public class AssessmentRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> 1. STARTING: Fetching Token...");

        // --- STEP A: GET TOKEN ---
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        // YOUR DETAILS
        String myName = "Aruniya Asokan";
        String myRegNo = "22BCE1367";
        String myEmail = "aruniya.asokan2022@vitstudent.ac.in";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", myName);
        requestBody.put("regNo", myRegNo);
        requestBody.put("email", myEmail);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 1. Send First Request
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            // 2. Extract Token and URL
            String token = root.path("accessToken").asText();
            String webhookUrl = root.path("webhook").asText();

            System.out.println(">>> TOKEN: " + token.substring(0, 10) + "...");
            System.out.println(">>> WEBHOOK: " + webhookUrl);

            // --- STEP B: SUBMIT ANSWER ---

            // This is the correct SQL for Question 1 (ODD)
            String mySqlQuery = "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) <> 1 ORDER BY p.AMOUNT DESC LIMIT 1";

            System.out.println(">>> SUBMITTING ANSWER...");

            // 3. Prepare Final JSON
            Map<String, String> finalBody = new HashMap<>();
            finalBody.put("finalQuery", mySqlQuery);

            // 4. Add Token to Headers
            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setContentType(MediaType.APPLICATION_JSON);
            authHeaders.set("Authorization", token);

            HttpEntity<Map<String, String>> finalEntity = new HttpEntity<>(finalBody, authHeaders);

            // 5. Send Final Request
            ResponseEntity<String> finalResponse = restTemplate.postForEntity(webhookUrl, finalEntity, String.class);

            // 6. Print Success Message
            System.out.println("==========================================");
            System.out.println(">>> FINAL RESULT: " + finalResponse.getBody());
            System.out.println("==========================================");

        } catch (Exception e) {
            System.out.println(">>> ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}