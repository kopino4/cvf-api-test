package com.mycompany.cvf.api.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        String response = "";
        String url = "https://api.cvf.cz/v2/hraci";
        String username = "CVUT_app";
        String password = "je9oSYiGcpHHf8+0UrobT9l72a/cxoyy";

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes()
        );

        System.out.println("Authentication payload: " + basicAuthPayload);

        try {

            // Connect to the web server endpoint
            URL serverUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

            // Set HTTP method as GET
            urlConnection.setRequestMethod("GET");

            // Include the HTTP Basic Authentication payload
            urlConnection.addRequestProperty("Authorization", basicAuthPayload);

            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
            BufferedReader httpResponseReader = null;
            httpResponseReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String lineRead;

            while((lineRead = httpResponseReader.readLine()) != null) {
                response += lineRead;
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
            response = ex.toString();
        }


        final Label caption = new Label("API Response:");
        final Label label = new Label(response);
        layout.addComponents(caption, label);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
