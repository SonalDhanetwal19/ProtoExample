package com.phoenix.ProtoExample;

import com.chargingtime.protobuf.proto.ChargingTimeProto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@RestController
@RequestMapping("/chargingTime/v1")
public class ChargingTimeController {

    @GetMapping(value="/generateProto", produces = "application/x-protobuf")
    public ResponseEntity<byte[]> getChargingTimeData() {

        ChargingTimeProto.Durations durations1 = ChargingTimeProto.Durations.newBuilder()
                .setToSoc("80%")
                .setDuration("50")
                .setDurationWhenCold("55")
                .build();
        ChargingTimeProto.Durations durations2 = ChargingTimeProto.Durations.newBuilder()
                .setToSoc("70%")
                .setDuration("40")
                .setDurationWhenCold("45")
                .build();

        ChargingTimeProto.Mode2AC mode2AC1 = ChargingTimeProto.Mode2AC.newBuilder()
                .setModeValue("1.8kW")
                .setUnit("h")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();
        ChargingTimeProto.Mode2AC mode2AC2 = ChargingTimeProto.Mode2AC.newBuilder()
                .setModeValue("2.3kW")
                .setUnit("h")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();
        ChargingTimeProto.Mode2AC mode2AC3 = ChargingTimeProto.Mode2AC.newBuilder()
                .setModeValue("3.5kW")
                .setUnit("h")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();
        ChargingTimeProto.Mode3AC mode3AC1 = ChargingTimeProto.Mode3AC.newBuilder()
                .setModeValue("7kW")
                .setUnit("h")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();
        ChargingTimeProto.Mode3AC mode3AC2 = ChargingTimeProto.Mode3AC.newBuilder()
                .setModeValue("11kW")
                .setUnit("h")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();

        ChargingTimeProto.Mode4AC mode4AC1 = ChargingTimeProto.Mode4AC.newBuilder()
                .setModeValue("50kW")
                .setUnit("min")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();
        ChargingTimeProto.Mode4AC mode4AC2 = ChargingTimeProto.Mode4AC.newBuilder()
                .setModeValue("100kW")
                .setUnit("min")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();
        ChargingTimeProto.Mode4AC mode4AC3 = ChargingTimeProto.Mode4AC.newBuilder()
                .setModeValue("150kW")
                .setUnit("min")
                .setFromSoc("10%")
                .addDuration(durations1)
                .addDuration(durations2)
                .build();

        ChargingTimeProto.Temperature temprature = ChargingTimeProto.Temperature.newBuilder()
                .setTempratureValue("-10")
                .addMode2AC(mode2AC1)
                .addMode2AC(mode2AC2)
                .addMode2AC(mode2AC3)
                .addMode3AC(mode3AC1)
                .addMode3AC(mode3AC2)
                .addMode4AC(mode4AC1)
                .addMode4AC(mode4AC2)
                .addMode4AC(mode4AC3)
                .build();
        ChargingTimeProto.ChargingCurveData chargingCurveData = ChargingTimeProto.ChargingCurveData.newBuilder()
                .addTemprature(temprature)
                .build();

        ChargingTimeProto.ChargingTime chargingTime = ChargingTimeProto.ChargingTime.newBuilder()
                .setBatteryCapacity("71.4 KWh")
                .setMaximumInputChargingPowerAC("11 KW")
                .setMaximumInputChargingPowerDC("11 KW")
                .setChargingCurveVersionNumber("V1")
                .setMaxCumulatedPowerOfFastCharging("50 KW")
                .setElectricRange("11 KW")
                .addChargingCurveData(chargingCurveData)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/x-protobuf")
                .body(chargingTime.toByteArray());
    }

    @GetMapping(value="/generateProtobufFile")
    public String generateProtobugFile() {
        String endppointUrl = "localhost:8080/chargingTime/v1/generateProto";
        String outputFileProto = "output.pb";
        String outputFileBin = "output.bin";

        try {
            URL url = new URL("http://localhost:8080/chargingTime/v1/generateProto");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept", "application/x-protobuf");
            int responseCode = httpURLConnection.getResponseCode();
//            HttpClient httpClient = HttpClient.newHttpClient();
//            HttpRequest httpRequest = HttpRequest.newBuilder()
//                    .uri(URI.create(endppointUrl))
//                    .header("Accept","application/x-protobuf")
//                    .build();
//            HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());

            if (responseCode == HttpURLConnection.HTTP_OK) {
                ChargingTimeProto.ChargingCurveData.Builder chargingDataBuilder = ChargingTimeProto.ChargingCurveData.newBuilder();
                chargingDataBuilder.mergeFrom(httpURLConnection.getInputStream());
                ChargingTimeProto.ChargingCurveData chargingCurveData = chargingDataBuilder.build();
                try (FileOutputStream outputStream = new FileOutputStream(outputFileBin)) {
                    chargingCurveData.writeTo(outputStream);
                }
                try (FileOutputStream outputStream = new FileOutputStream(outputFileProto)) {
                    chargingCurveData.writeTo(outputStream);
                }
            } else {
                System.err.println("HTTP request failed with response code: " + responseCode);
                // Attempt to read the error stream, if available.
                try (Scanner scanner = new Scanner(httpURLConnection.getErrorStream(), StandardCharsets.UTF_8.name())) {
                    scanner.useDelimiter("\\A");
                    if (scanner.hasNext()) {
                        System.err.println("Error details: " + scanner.next());
                    }
                } catch (NullPointerException e) {
                    System.err.println("Error stream was null");
                }
                httpURLConnection.disconnect();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Success";
    }

}
