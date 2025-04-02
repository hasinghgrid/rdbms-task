package com.cachesystem;

import com.cachesystem.cache.Cache;
import com.cachesystem.cache.CacheFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("Enter cache size: ");
        int size = scanner.nextInt();

        System.out.println("Enter cache type (LRU/LFU/TTL): ");
        String type = scanner.next();

        System.out.println("Enter TTL (if applicable, else 0): ");
        long ttl = scanner.nextLong();

        Cache<String, String> cache = CacheFactory.createCache(size, type, ttl);

        while (true) {
            System.out.println("Enter command (put/get/exit): ");
            String command = scanner.next();

            if (command.equals("exit")) break;
            if (command.equals("put")) {
                System.out.println("Enter key: ");
                String key = scanner.next();
                System.out.println("Enter value: ");
                String value = scanner.next();
                cache.put(key, value);
            } else if (command.equals("get")) {
                System.out.println("Enter key: ");
                String key = scanner.next();
                System.out.println("Value: " + cache.get(key));
            }
        }
        scanner.close();
    }
}
