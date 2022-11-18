package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendVideo;
import org.jsoup.Jsoup;

public class movieBot {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("");
//211433463
        bot.setUpdatesListener(updates -> {
            updates.forEach(upd -> {
                try {
                    System.out.println(upd);
                    long chatId = upd.message().chat().id();
                    String incomeMessage = upd.message().text();
                    //logic
                    String movieName = incomeMessage;
                    String jsonString = Jsoup.connect("https://itunes.apple.com/search?media=movie&term=" + movieName)
                            .ignoreContentType(true)
                            .execute()
                            .body();
                    ObjectMapper objectMapper = new ObjectMapper();
                    var jsonNode = objectMapper.readTree(jsonString);
                    String result = jsonNode.get("results").get(0).get("previewUrl").asText();
                    //send response
                    SendVideo request = new SendVideo(chatId, result);
                    bot.execute(request);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}


