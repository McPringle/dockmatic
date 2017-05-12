/*
 * Hello Service
 * Copyright (C) 2017 Marcus Fihlon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ch.fihlon.dockmatic.timeservice.boundary;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloResource {

    private Client client;
    private WebTarget timeservice;

    @PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
        timeservice = client.target("http://timeservice:8080").path("/api/time");
    }

    @PreDestroy
    private void clean() {
        client.close();
    }

    @GET
    @Path("{name}")
    public String sayHello(@PathParam("name") final String name) throws UnknownHostException {
        final String time = timeservice.request(MediaType.TEXT_PLAIN).get(String.class);
        final String hostname = InetAddress.getLocalHost().getHostName();
        return "Hello " + name + "\n"
                + time + "\n"
                + hostname;
    }

}
