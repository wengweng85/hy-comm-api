package com.insigma.mvc.component.task;

import java.util.Date;

import org.springframework.stereotype.Component;


@Component("taskJob") 
public class TaskJob { 
	
    public void redisUpdate() { 
        System.out.println(new Date().toLocaleString()); 
        try {
        	
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
    } 
}
