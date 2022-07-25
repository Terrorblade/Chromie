package org.hour.of.twilight.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class SoapHelper {

    private final static String soapRequestTemplate = "<soapenv:Envelope " +
            "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:ns1=\"urn:TC\" " +
            "xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
            "soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n" +
            "   <soapenv:Header/>\r\n" +
            "   <soapenv:Body>\r\n" +
            "      <ns1:executeCommand>\r\n" +
            "         <command>%s</command>\r\n" +
            "      </ns1:executeCommand>\r\n" +
            "   </soapenv:Body>\r\n" +
            "</soapenv:Envelope>";


    static {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (ConfigHelper.botUsername, ConfigHelper.botPassword.toCharArray());
            }
        });
    }

    public static String sendSoapRequest(final String command, final boolean liveRealm) {
        final String xml = String.format(soapRequestTemplate, command);
        try {
            return callSoapService(xml, liveRealm ? ConfigHelper.liveRealmSoap : ConfigHelper.devRealmSoap);
        } catch (Exception e) {
            return e.toString();
        }
    }

    private static String callSoapService(final String soapRequest, final String url) throws Exception {
        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        con.setDoOutput(true);
        final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(soapRequest);
        wr.flush();
        wr.close();

        final BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
