package put.sk.publish;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Connection service to enable sending and receive message
 */
public class ConnectionService {
    /**
     * Server IP
     */
    private static String IP_ADDRESS;
    /**
     * Server PORT
     */
    private static int IP_PORT;
    /**
     * Username
     */
    private static String USERNAME;
    /**
     * Last action status
     */
    private boolean actionSuccess;

    /**
     * Connection service constructor
     * @param IP Server IP
     * @param port Server port
     * @param userName UserName
     */
    public ConnectionService(String IP, int port, String userName) {
        IP_ADDRESS = IP;
        IP_PORT = port;
        USERNAME = userName;
    }

    /**
     * Login to server API
     * @return Login action status
     */
    public boolean login() {
        // Prepare request parameters
        ArrayList<String> requestData = new ArrayList();
        requestData.add("LOGIN_C");
        requestData.add(USERNAME);

        // Create API Parser
        APIParser apiParser = new APIParser();
        String request = apiParser.buildRequest(requestData);

        // Connection
        Connection connection = new Connection(IP_ADDRESS, IP_PORT);
        try {
            // Transfer data to API, receive and parse response
            String response = connection.transfer(request);
            ArrayList<String> data = apiParser.parseResponse(response);

            // Check response from server
            if(data.get(0).equals("OK")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();

            // Return invalid login
            return false;
        }
    }

    /**
     * Fetch all topics from API
     * @param skip Number of topics to skip
     */
    public ArrayList<Topic> fetchAllTopics(int skip) {
        // Prepare request parameters
        ArrayList<String> requestData = new ArrayList();
        requestData.add("GET_QUEUES");
        requestData.add(USERNAME);
        requestData.add(Integer.toString(skip));

        // Create API Parser
        APIParser apiParser = new APIParser();
        String request = apiParser.buildRequest(requestData);

        // Connection
        Connection connection = new Connection(IP_ADDRESS, IP_PORT);
        try {
            // Transfer data to API, receive and parse response
            String response = connection.transfer(request);
            ArrayList<String> data = apiParser.parseResponse(response);

            // Prepare result
            ArrayList<Topic> result = new ArrayList<>(data.size());

            // Create new topic, set title
            for (String title : data) {
                Topic topic = new Topic(title);
                result.add(topic);
            }

            // Return result
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return empty list
        return new ArrayList<>();
    }

    /**
     * Load articles from API - only for selected topic
     * @param selectedTopic Selected topic to check
     * @param skip Articles to skip in load
     * @return ArrayList with articles
     */
    public ArrayList<Article> fetchTopicArticles(Topic selectedTopic, int skip) {
        // Prepare request parameters
        ArrayList<String> requestData = new ArrayList();
        requestData.add("GET_ALL_ARTICLES");
        requestData.add(USERNAME);
        requestData.add(selectedTopic.getName());
        requestData.add(Integer.toString(skip));

        // Create API Parser
        APIParser apiParser = new APIParser();
        String request = apiParser.buildRequest(requestData);

        // Connection
        Connection connection = new Connection(IP_ADDRESS, IP_PORT);
        try {
            // Transfer data to API, receive and parse response
            String response = connection.transfer(request);
            ArrayList<String> data = apiParser.parseResponse(response);

            System.out.println(data);

            if(data.get(0).equals("ERROR")) {
                this.actionSuccess = false;
            } else {
                // Prepare result
                int resultSize = data.size();
                ArrayList<Article> result = new ArrayList<>(resultSize);

                // Prepare articles list
                for(int i = 0; i < resultSize; i += 2) {
                    Article article = new Article(data.get(i), data.get(i+1), selectedTopic.getName());
                    result.add(article);
                }

                // Append result to topic
                selectedTopic.setArticles(result);
                this.actionSuccess = true;

                // Return result
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return empty list
        return new ArrayList<>();
    }

    /**
     * Getter - check action success?
     * @return Boolean action status
     */
    public boolean isActionSuccess() {
        return actionSuccess;
    }
}
