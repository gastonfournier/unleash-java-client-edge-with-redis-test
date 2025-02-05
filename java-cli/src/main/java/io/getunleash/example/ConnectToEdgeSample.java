package io.getunleash.example;

import java.util.stream.Collectors;

import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.UnleashContext;
import io.getunleash.event.UnleashSubscriber;
import io.getunleash.repository.FeatureToggleResponse;
import io.getunleash.util.UnleashConfig;

public class ConnectToEdgeSample {

    public static void main(String[] args) throws InterruptedException {
        UnleashConfig config = UnleashConfig.builder()
                .appName("client-example.java")
                .customHttpHeader(
                        "Authorization",
                        getOrElse("UNLEASH_API_TOKEN",
                                "*:development.unleash-insecure-client-api-token"))
                .unleashAPI("http://localhost:3063/api")
                .instanceId("java-example")
                .synchronousFetchOnInitialisation(true)
                .sendMetricsInterval(1)
                .fetchTogglesInterval(1) // Fetch every second
                .synchronousFetchOnInitialisation(true)
                .subscriber(
                        new UnleashSubscriber() {
                            @Override
                            public void togglesFetched(
                                    FeatureToggleResponse toggleResponse) {
                                System.out.println(toggleResponse);
                                if (toggleResponse.getHttpStatusCode() != 304) {
                                    System.out.println("Number of toggles from edge: " +
                                            toggleResponse
                                                    .getToggleCollection()
                                                    .getFeatures()
                                                    .size());
                                    System.out.println(toggleResponse.getToggleCollection().getFeatures().stream().map(f -> f.getName()
                                    + " -> " + f.isEnabled()).collect(Collectors.joining(", ")));
                                }
                            }
                        })
                .build();
        Unleash unleash = new DefaultUnleash(config);
        
        int countTrue = 0;
        int iter = 0;
        String flagName = System.getenv("FLAG_NAME");
        System.out.println("Flag name: " + flagName);
        while (iter++ < 10000) {
            UnleashContext context = UnleashContext.builder()
                    .addProperty("userId", ""+Math.random())
                    .build();
            if (unleash.isEnabled(flagName, context)) {
                countTrue++;
            }
        }
        System.out.println("Waiting to send metrics");
        Thread.sleep(1500);
        System.out.println(flagName +" was true: " + countTrue + " of " + (iter-1));
    }

    public static String getOrElse(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
