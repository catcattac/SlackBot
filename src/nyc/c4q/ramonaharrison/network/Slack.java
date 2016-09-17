package nyc.c4q.ramonaharrison.network;

import nyc.c4q.ramonaharrison.Bot;
import nyc.c4q.ramonaharrison.model.Attachment;
import nyc.c4q.ramonaharrison.model.Message;
import nyc.c4q.ramonaharrison.network.response.*;
import nyc.c4q.ramonaharrison.util.Token;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Ramona Harrison
 * on 8/26/16
 *
 * A class for interacting with Slack's Web API.
 *
 */

public class Slack {

    private static final String API_KEY = Token.findApiToken();
    private static final String BASE_URL = "https://slack.com/api/";
    private static final String ENDPOINT_TEST = "api.test";
    private static final String ENDPOINT_LIST_CHANNELS = "channels.list";
    private static final String ENDPOINT_LIST_MESSAGES = "channels.history";
    private static final String ENDPOINT_POST_MESSAGE = "chat.postMessage";
    private static final String ENDPOINT_DELETE_MESSAGE = "chat.delete";
    public static final String BOTS_CHANNEL_ID = "C2BLV9LV6";
    // static strings for Holiday API.
    public static final String HOLIDAY_BASE_URL = "https://holidayapi.com";
    public static final String HOLIDAY_ENDPOINT = "/v1/holidays";
    public static final String HOLIDAY_API_KEY = "51638001-7212-488d-bfa5-9d0e6e7a8ef3";
    //Strings For Holiday ++ Giphy!




    public boolean solved;


    public static String getMonth(){
        GregorianCalendar date = new GregorianCalendar();
        return "&month=" + Integer.toString((date.get(Calendar.MONTH)+1));
    }

    public static String getYear(){
        GregorianCalendar date = new GregorianCalendar();
        return "&year=" + Integer.toString((date.get(Calendar.YEAR)));
    }

    public static String getDay(){
        GregorianCalendar date = new GregorianCalendar();
        return "&day=" + Integer.toString((date.get(Calendar.DATE)));
    }


    /**
     * Static method to test the Slack API.
     *
     * @return the Response indicating ok/error or null if the connection failed.
     */
    public static Response testApi() {
        URL testUrl = HTTPS.stringToURL(BASE_URL + ENDPOINT_TEST  + "?token=" + API_KEY);

        JSONObject object = HTTPS.get(testUrl);

        return new Response(object);


    }
//
    public static void getHolidaysForToday(){
        ListMessagesResponse listMessagesResponse = Slack.listMessages(BOTS_CHANNEL_ID);

        if(listMessagesResponse.isOk()) {
            List<Message> messages = listMessagesResponse.getMessages();


            do{
               if(/*messages.get(0).getText().contains("messybot") && messages.get(0).getText().contains("holiday"*/ true){
                   URL holidayURL = HTTPS.stringToURL(HOLIDAY_BASE_URL + HOLIDAY_ENDPOINT + "?country=us&key=" + HOLIDAY_API_KEY + getYear() + getMonth() + getDay() + "&upcoming=true");
                   System.out.println(holidayURL);

                   JSONObject holidayJson = HTTPS.get(holidayURL);

                   if (holidayJson.containsKey("holidays")) {
                       JSONArray array = (JSONArray) holidayJson.get("holidays");

                       JSONObject nextHoliday = (JSONObject) array.get(0);

                       String holidayName = (String) nextHoliday.get("name");

                       String holidayDate = (String) nextHoliday.get("date");

                       sendMessage(holidayDate);
                       sendMessage(holidayName);

                   }
                }
            }while(false);
        }
    }


    public static void giphyTesting (){

    }




    /**
     * Static method to list all public channels on the Slack team.
     *
     * @return the ListChannelsResponse indicating ok/error or null if the connection failed.
     */
    public static ListChannelsResponse listChannels() {

        URL listChannelsUrl = HTTPS.stringToURL(BASE_URL + ENDPOINT_LIST_CHANNELS + "?token=" + API_KEY);

        return new ListChannelsResponse(HTTPS.get(listChannelsUrl));
    }

    /**
     * Static method to list the last 100 message on a given channel.
     *
     * @param  channelId the id of the channel from which to list messages.
     * @return the ListMessagesResponse indicating ok/error or null if the connection failed.
     */
    public static ListMessagesResponse listMessages(String channelId) {

        URL listMessagesUrl = HTTPS.stringToURL(BASE_URL + ENDPOINT_LIST_MESSAGES + "?token=" + API_KEY + "&channel=" + channelId);

        return new ListMessagesResponse(HTTPS.get(listMessagesUrl));
    }

    /**
     * Static method to send a message to the #bots channel.
     *
     * @param  messageText the message text.
     * @return the SendMessageResponse indicating ok/error or null if the connection failed.
     */
    public static SendMessageResponse sendMessage(String messageText) {

        try {
            messageText = URLEncoder.encode(messageText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        URL sendMessageUrl = HTTPS.stringToURL(BASE_URL + ENDPOINT_POST_MESSAGE + "?token=" + API_KEY + "&channel=" + BOTS_CHANNEL_ID + "&text=" + messageText);

        return new SendMessageResponse(HTTPS.get(sendMessageUrl));
    }
    /**
     * Static method to send a message with one or more attachments to the #bots channel.
     *
     * @param  messageText the message text.
     * @param  attachments a list of one of more attachments to be parsed to a JSON-encoded URL string parameter.
     * @return the SendMessageResponse indicating ok/error or null if the connection failed.
     */
    public static SendMessageResponse sendMessageWithAttachments(String messageText, List<Attachment> attachments) {

        // TODO (optional): implement this method! See https://api.slack.com/docs/message-attachments



        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Static method to delete an existing message from the #bots channel.
     *
     * @param  messageTs the message timestamp.
     * @return the DeleteMessageResponse indicating ok/error or null if the connection failed.
     */
    public static DeleteMessageResponse deleteMessage(String messageTs) {
        URL deleteMessageUrl = HTTPS.stringToURL(BASE_URL + ENDPOINT_DELETE_MESSAGE + "?token=" + API_KEY + "&channel=" + BOTS_CHANNEL_ID + "&ts=" + messageTs);

        return new DeleteMessageResponse(HTTPS.get(deleteMessageUrl));
    }




}
