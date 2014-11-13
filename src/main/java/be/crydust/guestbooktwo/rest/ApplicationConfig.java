/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.crydust.guestbooktwo.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author kristof
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        
        resources.add(be.crydust.guestbooktwo.rest.ChildFacadeREST.class);
        resources.add(be.crydust.guestbooktwo.rest.HelloWorld.class);
        resources.add(be.crydust.guestbooktwo.rest.ParentFacadeREST.class);
        resources.add(be.crydust.guestbooktwo.rest.PostFacadeREST.class);
//        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(be.crydust.guestbooktwo.rest.ChildFacadeREST.class);
        resources.add(be.crydust.guestbooktwo.rest.HelloWorld.class);
        resources.add(be.crydust.guestbooktwo.rest.ParentFacadeREST.class);
        resources.add(be.crydust.guestbooktwo.rest.PostFacadeREST.class);
        resources.add(com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper.class);
        resources.add(com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JsonMappingExceptionMapper.class);
        resources.add(com.fasterxml.jackson.jaxrs.json.JsonParseExceptionMapper.class);
    }
    
}
