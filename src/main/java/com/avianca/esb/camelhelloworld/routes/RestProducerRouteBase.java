/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.avianca.esb.camelhelloworld.routes;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.avianca.esb.camelhelloworld.model.Book;
import com.avianca.esb.camelhelloworld.model.Order;

import com.avianca.esb.camelhelloworld.properties.RestProducerBase;
import com.avianca.esb.camelhelloworld.configurador.ConfigurationRoute;

@Component
public class RestProducerRouteBase extends ConfigurationRoute {

    @Autowired
    private RestProducerBase restConfig;

    @Override
    public void configure() throws Exception {
        super.configure();
        // use http4 with rest dsl producer
        restConfiguration().producerComponent("http4")
                // to call rest service on localhost:8080 (the REST service from GeoRestController)
                .host(restConfig.getHost()).port(restConfig.getPort());


        from("direct:restProducerRouteBase")
                .to("bean:orderService?method=getOrder(${header.id})")
                // set a random city to use
                //.setHeader("city", RestProducerRoute::randomCity)
                // use the rest producer to call the rest service
                //.convertBodyTo(Order.class)
                .marshal().json(JsonLibrary.Jackson, Order.class)
                //.to("rest:get:country/{city}")
                //.unmarshal().json(JsonLibrary.Jackson, Order.class)
                // print the response
                .log("${body}");
    }
}
