package nyc.c4q.ramonaharrison;

import java.util.*;
import nyc.c4q.ramonaharrison.model.Message;
import nyc.c4q.ramonaharrison.network.Slack;
import sun.misc.resources.Messages;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Bot aBot = new Bot();
        while (true) {
            Slack.getHolidaysForToday();
            if (Slack.giphySearch() != null) {
                aBot.sendMessageToBotsChannel("You have entered the guessing game command! Type 'hint' for another gif related to the next holiday, or 'give up' for the answer. Good luck!");
                aBot.sendMessageToBotsChannel(Slack.giphySearch());
            }
            Slack.guessingGame();
        }
    }
}
