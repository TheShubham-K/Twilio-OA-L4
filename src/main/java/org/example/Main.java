package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;

public class Main {

    public static void main(String[] args) {

        try {
            String urlString = "https://jsonmock.hackerrank.com/api/transactions/search";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse the response as JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray dataArray = jsonResponse.getJSONArray("data");

            System.out.println("JSON response (pretty-printed):");
            System.out.println(dataArray.toString(1));
            // Map to store net amounts for each user
            Map<String, Double> userBalances = new HashMap<>();

            // Process the data
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject data = dataArray.getJSONObject(i);
                String userName = data.getString("userName");
                String txnType = data.getString("txnType");

                String amountStr = data
                        .getString("amount")
                        .replace("$", "")
                        .replace(",", "");
                double amount = Double.parseDouble(amountStr);

                // Update user's balance based on transaction type
                if (txnType.equals("credit")) {
                    userBalances.put(userName, userBalances.getOrDefault(userName, 0.0) + amount);
                } else if (txnType.equals("debit")) {
                    userBalances.put(userName, userBalances.getOrDefault(userName, 0.0) - amount);
                }
            }

            // Find the user with the maximum net balance
            String maxUserName = null;
            double maxBalance = Double.MIN_VALUE;

            for (Map.Entry<String, Double> entry : userBalances.entrySet()) {
                if (entry.getValue() > maxBalance) {
                    maxUserName = entry.getKey();
                    maxBalance = entry.getValue();
                }
            }

            // Output the userName with the maximum net balance
            System.out.println("User with maximum net balance: " + maxUserName);
            System.out.println("Maximum balance: " + maxBalance);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
