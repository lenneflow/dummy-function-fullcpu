package de.lenneflow.dummyfunctionfullcpu.controller;


import de.lenneflow.dummyfunctionfullcpu.dto.FunctionPayload;
import de.lenneflow.dummyfunctionfullcpu.enums.RunStatus;
import de.lenneflow.dummyfunctionfullcpu.util.Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dummy/fullcpu")
@EnableAsync
@RequiredArgsConstructor
public class FunctionController {

    private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);

    final RestTemplate restTemplate;

    @GetMapping("ping")
    public String ping(){
        return "Dummy function fullcpu is working fine!";
    }

    @PostMapping("start")
    @Async
    public void fullcpu(@RequestBody FunctionPayload functionPayload){
        String callBackUrl = functionPayload.getCallBackUrl();
        try {
            String keyMin = "durationInSeconds";
            int duration = (int) functionPayload.getInputData().get(keyMin);
            logger.info("Start cpu intensive calculation for  {} seconds", duration);

            LocalDateTime startTime = LocalDateTime.now();
            for(int i=0; i<8; i++){
                new Thread(() -> Util.calculatePrimeNumbersBetween(1000000000)).start();
            }

            while (LocalDateTime.now().isBefore(startTime.plusSeconds(duration))) {
             Thread.sleep(5000);
            }

            Util.stopCalculation();

            logger.info("Calculation finished");
            functionPayload.setRunStatus(RunStatus.COMPLETED);
            Map<String, Object> output = new HashMap<>();
            output.put("durationInSeconds" , duration);
            functionPayload.setOutputData(output);
            logger.info("call the callback url {}", callBackUrl);
            restTemplate.postForObject(callBackUrl, functionPayload, Void.class);
            logger.info("Payload sent successfully");
        }catch (Exception e){
            handleException(callBackUrl, e);
            Thread.currentThread().interrupt();
        }
    }

    private void handleException(String callBackUrl, Exception e) {
        FunctionPayload errorPayload = new FunctionPayload();
        logger.error(e.getMessage());
        errorPayload.setRunStatus(RunStatus.FAILED);
        errorPayload.setFailureReason(e.getLocalizedMessage());
        restTemplate.postForObject(callBackUrl, errorPayload, Void.class);
    }
}
