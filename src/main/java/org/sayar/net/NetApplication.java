package org.sayar.net;

import org.sayar.net.Service.activityType.ActivityTypeService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetApplication implements CommandLineRunner {

    @Autowired
    private ActivityTypeService activityTypeService;
    public static String token;


    public static void main(String[] args) {
        System.out.println("version: " + SpringVersion.getVersion());
        int [] cars = {1,2,3};
        for (int i=0; i<cars.length;++i) {
            Print.print("cars",i);
        }
        SpringApplication.run(NetApplication.class, args);
    }

    //5.2.12
    @Override
    public void run(String... args) throws Exception {

    }

//	@Autowired//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(NetApplication.class);
//	}
}
