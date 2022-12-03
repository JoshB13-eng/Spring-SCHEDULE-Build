
//
package JoshA.Control;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.stereotype.Controller;

import JoshA.DataBox.*;
import JoshA.Schedule.Scheduler;

@Controller
@RequestMapping("/JoshFruit")
public class FruitController {
	
	private static final String SCHEDULED_TASKS = "scheduledTasks";
    //scheduled task bean name

	@Autowired
	private ScheduledAnnotationBeanPostProcessor postProcessor;
    
	@Autowired
	private Scheduler schedulerBean;
    
    public Map<Integer,Fruit> fruit;
    
    public FruitController(){
        this.fruit = new HashMap();
        Fruit fruitObj = new Fruit();
        
        Apple apple = new Apple();
        apple.setNumberOfApple(5);
        apple.setAmount(500);
        
        Lemon lemon = new Lemon();
        lemon.setNumberOfLemon(5);
        lemon.setAmount(250);
        
        Orange orange = new Orange();
        orange.setNumberOfOrange(5);
        orange.setAmount(150);
        
        fruitObj.setNumberOfFruit(5);
        fruitObj.setApple(apple);
        fruitObj.setLemon(lemon);
        fruitObj.setOrange(orange);
        
        this.fruit.put(1,fruitObj);
        
        Fruit fruitObj2 = new Fruit();
        
        Apple apple2 = new Apple();
        apple2.setNumberOfApple(10);
        apple2.setAmount(1000);
        
        Lemon lemon2 = new Lemon();
        lemon2.setNumberOfLemon(10);
        lemon2.setAmount(600);
        
        Orange orange2 = new Orange();
        orange2.setNumberOfOrange(10);
        orange2.setAmount(300);
        
        fruitObj2.setNumberOfFruit(10);
        fruitObj2.setApple(apple2);
        fruitObj2.setLemon(lemon2);
        fruitObj2.setOrange(orange2);
        
        this.fruit.put(2,fruitObj2);
        //
    }
    
	@GetMapping(value = "/stop")
	public ResponseEntity<Object> stopSchedule() {
		//To stop our schedule task 
		postProcessor.postProcessBeforeDestruction(schedulerBean, SCHEDULED_TASKS);
		
		return new ResponseEntity(this.fruit,HttpStatus.OK);
	}

	@GetMapping(value = "/start")
	public ResponseEntity<Object> startSchedule() {
	    //To start our schedule task 
		postProcessor.postProcessAfterInitialization(schedulerBean, SCHEDULED_TASKS);
		 
		return new ResponseEntity("OK",HttpStatus.OK);
	}

	@GetMapping(value = "/set")
	public ResponseEntity<Object> listSchedules() throws JsonProcessingException {
	    //Get set of scheduled task. 
		Set<ScheduledTask> setTasks = postProcessor.getScheduledTasks();
		
		if (!setTasks.isEmpty()) {
		    //If the set of Scheduled task returned are not empty return a String of the set of the scheduled task
			return new ResponseEntity(setTasks.toString(),HttpStatus.OK);
		} 
		//Else return the below string
		//There will be no scheduled task likely if .postProcessBeforeDestruction(..) of ScheduledAnnotationBeanPostProcessor have been invoked
		return new ResponseEntity("Currently no scheduler tasks are running",HttpStatus.OK);
	}
	
	@PostMapping(path = "/Fruit/Create/{FruitIndex}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createFruit(@RequestBody Fruit fruitp, @PathVariable("FruitIndex")Integer FruitIndex){
        //Create a fruit bean element in map
        this.fruit.put(FruitIndex,fruitp);
        
        return new ResponseEntity<>("\nFruit is created successfully\n", HttpStatus.CREATED);
        //
    }
    
    
    @RequestMapping(path = "/Exit")
    public ResponseEntity<Object> Exit(){
        //Exit app
        return new ResponseEntity<>("\nShutting down server...\n", HttpStatus.OK);
    }
}

//TO STOP SCHEDULED
//Shell: curl http://localhost:9090/JoshFruit/stop

//TO START SCHEDULED
//Shell: curl http://localhost:9090/JoshFruit/start

//TO GET SET OF SCHEDULED TASK RUNNING
//Shell: curl http://localhost:9090/JoshFruit/set

//TO EXIT APP
//Shell:curl http://localhost:9090/JoshFruit/Exit
