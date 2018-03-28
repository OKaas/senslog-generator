package cz.senslog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import cz.hsrs.maplog.rest.dto.receive.PositionReceive;

import java.sql.Timestamp;
import java.util.Random;

/**
 * Created by OK on 6/9/2017.
 */
public class Application {
    public static void main(String[] args) {

        final String URL = args[0];
        final String USERNAME = args[1];
        final String PASSWORD = args[2];
        final long PERIOD = Long.parseLong(args[3]);

        System.out.println("Pinging: "+URL);
        System.out.println("Period: "+PERIOD);

        final Long unitId = new Long(1);

        final Random rnd = new Random();
        final ObjectMapper mapper = new ObjectMapper();

        int iteration = -1;
        long timeBuffer = 0;

        while(true){
            try {
                Client client = Client.create();
                client.addFilter(new HTTPBasicAuthFilter(USERNAME, PASSWORD));

                WebResource webResource = client.resource(URL);

                PositionReceive positionReceive = new PositionReceive();
                positionReceive.setUnitId(unitId);
                positionReceive.setAltitude(rnd.nextDouble());
                positionReceive.setDop(rnd.nextLong());
                positionReceive.setSpeed(rnd.nextDouble());
                positionReceive.setTimeStamp( new Timestamp(System.currentTimeMillis()) );

                final String postObject = mapper.writeValueAsString(positionReceive);

                System.out.println("=================\nPOST: "+postObject);

                final long start = System.nanoTime();

                ClientResponse response = webResource
                        .header("content-type", "application/json")
                        .post(ClientResponse.class, postObject);

                final long elapsed = System.nanoTime() - start;

                timeBuffer += elapsed;

                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response.getStatus());
                }

                String output = response.getEntity(String.class);

                System.out.println(
                        "Response : "+output+
                        "\n Current resp. time: \t"+(elapsed/1000000)+" \t[ms]" +
                        "\n Avg. response time: \t"+(timeBuffer/(++iteration) /  1000000)+" \t[ms]\n=================");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


