package net.okocraft.lunachat.japanize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

final class GoogleIME {

    private static final Gson GSON = new GsonBuilder().create();
    // see https://www.google.co.jp/ime/cgiapi.html
    private static final String GOOGLE_IME_URL = "https://www.google.com/transliterate?langpair=ja-Hira%7Cja&text=";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private GoogleIME() {
    }

    static @NotNull String convert(String original) throws Exception {
        if (original == null || original.isEmpty()) {
            return "";
        }

        var url = GOOGLE_IME_URL + URLEncoder.encode(original, StandardCharsets.UTF_8);
        var request = HttpRequest.newBuilder().uri(new URL(url).toURI()).GET().build();
        var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            var result = new StringBuilder();

            for (JsonElement element : GSON.fromJson(response.body(), JsonArray.class)) {
                result.append(element.getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString());
            }

            return result.toString();
        } else {
            return original;
        }
    }
}
