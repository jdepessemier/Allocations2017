package miro.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import miro.shared.Allocation;
import miro.shared.Time;

import org.apache.commons.fileupload.FileItemStream;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class AllocationsFileParser {
    static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private GreetingServiceImpl greetingService = new GreetingServiceImpl();
    private Time time = new Time();

    public void parse(FileItemStream file) throws IOException {

        InputStream stream = file.openStream();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream, "ISO-8859-1");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            bufferedReader.readLine();
            String line;

            while ((line = bufferedReader.readLine()).charAt(0) != ';') {
                decodeLine(line);
            }
        } finally {
            stream.close();
        }
    }

    private void decodeLine(String line) {
    	
        List<String> itemList = getItemsOfLine(line);
        
        String personFullName = itemList.get(0);
        String year = itemList.get(1);
        String month = itemList.get(2);
        String missionType = itemList.get(3).toUpperCase();
        String missionName = itemList.get(4);
        String activityName = itemList.get(5);

        int index = Integer.parseInt(month);

        if (time == null) {
            time = new Time(Integer.parseInt(month), Integer.parseInt(year));
            checkTime(year, month);
        }

        double value = Double.valueOf(itemList.get(6).replace(",", "."));
        
        if (activityName.equalsIgnoreCase("Total")){
        	activityName = "Projets";
        }

        if (activityName.equalsIgnoreCase("Absence")){
        	activityName = "Congés & Absences";
        }
        
        if (activityName.equalsIgnoreCase("OTHERS")){
        	activityName = "Activités Hors Projets";
        }
 
        
        if ((!personFullName.equalsIgnoreCase("Auquiere Eric")) &&
        	(!personFullName.equalsIgnoreCase("Engelen Albert")) &&
        	(!personFullName.equalsIgnoreCase("De Pessemier Johan"))) {        	
        	putAllocationData(personFullName,missionName,activityName,index, value);
        } 
    }
  
    private List<String> getItemsOfLine(String line) {
    	
        List<String> itemList = new ArrayList<String>();

        for (int i = 0; i < 6; i++) {
            int pointIndex = line.indexOf(";");
            String item = line.substring(0, pointIndex);
            itemList.add(item);
            line = line.substring(pointIndex + 1);
        }
        
        itemList.add(line);
        return itemList;
    }
    
    private void checkTime(String yearTxt, String monthTxt) {
        String month = greetingService.getMonthOfDate();
        String year = greetingService.getYearOfDate();

        boolean valid = Integer.parseInt(monthTxt) < Integer.parseInt(month)
                && Integer.parseInt(yearTxt) == Integer.parseInt(year);

        if (!valid) throw new RuntimeException("Periode du fichier invalide !!!");
    }
    
    private void putAllocationData(String personfullname, 
    							   String missionname, 
    							   String activityname, 
    							   int idx,
    							   Double prestation) {
    	
    	Double value = round(prestation,2);
    	
    	ObjectifyService.register(Allocation.class);
        Objectify obj = ObjectifyService.begin();
        Query<Allocation> query = obj.query(Allocation.class);
        
        Allocation allocation = query
                .filter("personFullName =", personfullname)
                .filter("missionName =", missionname)
                .filter("activityName =", activityname).get();

        if (allocation == null) allocation = new Allocation(personfullname, missionname, activityname);
        allocation.setAllocation(idx, value);
  
        obj.put(allocation);
    }
    
    public double round(double what, int howmuch) {
    	return (double)( (int)(what * Math.pow(10,howmuch) + .5) ) / Math.pow(10,howmuch);
    }

}
