package org.example;

import org.example.Enums.RateLimitType;
import org.example.Enums.UserTier;
import org.example.Model.RateLimiterConfig;
import org.example.Model.User;
import org.example.Service.RateLimiterService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Map<UserTier, RateLimiterConfig> configs = new HashMap<>();

        configs.put(
                UserTier.FREE,
                new RateLimiterConfig(
                        RateLimitType.TOKEN_BUCKET,
                        1,
                        5
                )
        );

        configs.put(
                UserTier.PREMIUM,
                new RateLimiterConfig(
                        RateLimitType.FIXED_BUCKET,
                        5,
                        5
                )
        );

        RateLimiterService rateLimiterService = new RateLimiterService(configs);

        User freeUser = new User("Alice", UserTier.FREE);
        User premiumUser = new User("Bob", UserTier.PREMIUM);

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n========== RATE LIMITER ==========");
            System.out.println("1. Send Request (FREE User)");
            System.out.println("2. Send Request (PREMIUM User)");
            System.out.println("3. Send 5 FREE Requests");
            System.out.println("4. Send 15 PREMIUM Requests");
            System.out.println("5. Sleep 10 Seconds");
            System.out.println("6. Exit");
            System.out.print("Choose Option : ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {

                    boolean allowed = rateLimiterService.allowRequest(freeUser);

                    if (allowed)
                        System.out.println("✅ FREE Request Allowed");
                    else
                        System.out.println("❌ FREE User Rate Limited");
                }

                case 2 -> {

                    boolean allowed = rateLimiterService.allowRequest(premiumUser);

                    if (allowed)
                        System.out.println("✅ PREMIUM Request Allowed");
                    else
                        System.out.println("❌ PREMIUM User Rate Limited");
                }

                case 3 -> {

                    System.out.println("\nSending 5 FREE Requests...\n");

                    for (int i = 1; i <= 5; i++) {

                        boolean allowed = rateLimiterService.allowRequest(freeUser);

                        System.out.println(
                                "Request " + i + " : "
                                        + (allowed ? "Allowed" : "Blocked"));
                    }
                }

                case 4 -> {

                    System.out.println("\nSending 15 PREMIUM Requests...\n");

                    for (int i = 1; i <= 15; i++) {

                        boolean allowed = rateLimiterService.allowRequest(premiumUser);

                        System.out.println(
                                "Request " + i + " : "
                                        + (allowed ? "Allowed" : "Blocked"));
                    }
                }

                case 5 -> {

                    System.out.println("Sleeping for 10 seconds...");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                case 6 -> {

                    System.out.println("Server Stopped.");
                    return;
                }

                default -> System.out.println("Invalid Choice");
            }
        }
    }
}