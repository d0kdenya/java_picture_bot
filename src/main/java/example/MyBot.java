package example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MyBot extends TelegramLongPollingBot {
    String BOT_NAME;
    String BOT_TOKEN;
    String URL = "https://api.nasa.gov/planetary/apod" +
            "?api_key=McZzCjsoqb68odMdMhlWYV002H1sxlBdVyLYchPc";

    public MyBot(String BOT_NAME, String BOT_TOKEN) throws TelegramApiException {
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String[] action = update.getMessage().getText().split(" ");

            switch (action[0]) {
                case "/start":
                case "/image": {
                    String url = Utils.getUrl(URL);
                    sendMessage(url, chatId);
                    break;
                }
                case "/date": {
                    String url = Utils.getUrl(URL + "&date=" + action[1]);
                    sendMessage(url, chatId);
                    break;
                }
                case "/help": {
                    sendMessage("Я бот НАСА. По запросу /image или /start я пришлю картинку дня", chatId);
                    break;
                }
                default: {

                    break;
                }
            }
        }
    }

    public void sendMessage(String msg, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Не удалось отправить сообщение!");
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
