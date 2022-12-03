
//Scheduler class

package JoshA.Schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Date; 
import java.text.SimpleDateFormat; 
import org.springframework.scheduling.annotation.Scheduled; 
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate; 
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import JoshA.DataBox.*;

@Component
@EnableScheduling
public class Scheduler{
    //Annotated @EnableScheduling as to tell spring to enable schedule configuration for this bean
    
    public Integer Index;
    
    public Scheduler (){
        Index = 2;
    }
	
	@Scheduled(cron = "30-58,59 * * * * SUN,MON,TUE,WED,FRI")
    public void Show(){
        //Shedule runs from ONLY 30sec to 58sec and 59secs, at every minute, at every hour, ONLY runs everyday of month if it dont contradict with the specified day of the week, at evry month,  and ONLY at sunday,monday,tuesday,wednesday,friday and saturday as day of the week.
        //NOTE ALSO that unlike how wwek starts from Sunday and ends at Saturday which is numerically 0 - 7. Cron assumes this another way round which is from Monday to Sunday which is figuratively 0 - 7
        
        
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        
        System.out.println("\n\n.Shedule runs from ONLY 30sec to 58sec and 59secs, at every minute, at every hour, ONLY runs everyday of month if it dont contradict with the specified day of the week, at evry month,  and ONLY at sunday,monday,tuesday,wednesday,friday and saturday as day of the week::"+smf.format(new Date()));
        
    }
    
    @Scheduled(cron = "*/3 * * * * 5")
    public void ShowZ(){
        //Shedule runs at every 3sec interval, at every minute, at every hour, ONLY at everyday of month if it dont contradict with the specified day of the week, at every month and then at ONLY 5th day of week(Friday)
        //NOTE ALSO that unlike how wwek starts from Sunday and ends at Saturday which os figuratively 0 - 7. Cron assumes this another way round which is from Monday to Sunday whixh is numerically 0 - 7.
        
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        
        System.out.println("\n\nShedule runs at every 3sec interval, at every minute, every hour, ONLY at everyday of month if it dont contradict with the specified day of the week, at every month and then at ONLY 5th day of week(Friday)::"+smf.format(new Date()));
        
    }
    
    @Scheduled(cron = "3,6,9,12,15,18,21,24,27 * * * * *")
    public void AddFruit(){
        //Shedule runs at every (3,6,9,12,15,18,21,24 and 27)secs, at every minute, at every hour, at every day of month, at every month, at every day of week
        //In this method Im planning on sending post request to this app controller with an increasing value of an Integer value as request path
        Index++;
        //Increases each time time schedule triggers
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        
        System.out.println("\n\nShedule runs at every (3,6,9,12,15,18,21,24 and 27)secs, at every minute, at every hour, at every day of month, at every month, at every day of week::"+smf.format(new Date()));
        
        
        String requestUrl = "http://localhost:9090/JoshFruit/Fruit/Create/"+Index;
        
        Integer numberOfFruit = Index*5;
        
        Fruit fruitObj = new Fruit();
        
        Apple apple = new Apple();
        apple.setNumberOfApple(numberOfFruit);
        apple.setAmount(numberOfFruit*7);
        
        Lemon lemon = new Lemon();
        lemon.setNumberOfLemon(numberOfFruit);
        lemon.setAmount(numberOfFruit*5);
        
        Orange orange = new Orange();
        orange.setNumberOfOrange(numberOfFruit);
        orange.setAmount(numberOfFruit*3);
        
        fruitObj.setNumberOfFruit(numberOfFruit);
        fruitObj.setApple(apple);
        fruitObj.setLemon(lemon);
        fruitObj.setOrange(orange);
        
        HttpHeaders headers = new HttpHeaders(); 
        //
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        
        HttpEntity<Fruit> entity = new HttpEntity(fruitObj,headers);
        
        String response = new RestTemplate().exchange(requestUrl,HttpMethod.POST,entity,String.class).getBody();
        //Has sent post request and gotten response
        System.out.println("\nResponse from Sending a POST request is"+response);
    }
}

//Go to FruitController.java to see how I start and stop this scheduler task