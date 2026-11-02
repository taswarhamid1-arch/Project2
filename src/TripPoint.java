import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents one GPS point from a trip and holds the list of all trip points.
 * 
 * @author Taswar Hamid
 */
public class TripPoint {

	private double lat;
	private double lon;
	private int time;
	private static ArrayList<TripPoint> trip = new ArrayList<>();
	
	/**
	 * Creates a trip point.
	 *
	 * @param time the time in minutes since the trip started
	 * @param lat the latitude in degrees
	 * @param lon the longitude in degrees
	 */
	public TripPoint(int time, double lat, double lon) {
		this.time = time;
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * Returns the time of this point.
	 *
	 * @return the time in minutes
	 */
	public int getTime() {
        return time;
    }

	/**
	 * Returns the latitude of this point.
	 *
	 * @return the latitude in degrees
	 */
    public double getLat() {
        return lat;
    }
    
	/**
	 * Returns the longitude of this point.
	 *
	 * @return the longitude in degrees
	 */
    public double getLon() {
        return lon;
    }
    
    /**
     * Returns a copy of the list of trip points.
     * 
     * @return a new list holding every trip point
     */
    public static ArrayList<TripPoint> getTrip() {
    	ArrayList<TripPoint> copy = new ArrayList<TripPoint>();
    	 for (int i = 0; i < trip.size(); i++) {
    		 copy.add(trip.get(i));
    	 }
    	 return copy;
    }
    
    /**
     * reads the CSV file ad fills the trip list with one point per line.
     * Prints a message if the file cannot be found.
     * 
     * @param filename the name of the CSV file to read
     */
    public static void readFile(String filename) {
        trip.clear();

        try {
            Scanner input = new Scanner(new File(filename));
            input.nextLine();

            while (input.hasNextLine()) {
                String line = input.nextLine();

                if (line.length() > 0) {
                    int comma1 = line.indexOf(",");
                    int comma2 = line.indexOf(",", comma1 + 1);

                    int time = Integer.parseInt(line.substring(0, comma1));
                    double lat = Double.parseDouble(line.substring(comma1 + 1, comma2));
                    double lon = Double.parseDouble(line.substring(comma2 + 1));

                    trip.add(new TripPoint(time, lat, lon));
                }
            }

            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }
    
    /**
     * Returns the total time of the trip.
     * 
     * @return the total time in hours
     */
    public static double totalTime() {
    	int last = trip.size()-1;
    	int minutes = trip.get(last).getTime();
    	
    	return minutes / 60.0;
    }
    
    /**
     * Calculates the Haversine distance between two points.
     * 
     * @param a the first point
     * @param b the second point
     * @return the distance in km
     */
    public static double haversineDistance(TripPoint a, TripPoint b) {
    	double radius = 6371.0;
    	
    	double lat1 = Math.toRadians(a.getLat());
    	double lat2 = Math.toRadians(b.getLat());
    	double lon1 = Math.toRadians(a.getLon());
    	double lon2 = Math.toRadians(b.getLon());
    	
    	double dLat = lat2 - lat1;
    	double dLon = lon2 - lon1;
    	
    	double part1 = Math.sin(dLat / 2) * Math.sin(dLat / 2);
        double part2 = Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double h = part1 + part2;

        return 2 * radius * Math.asin(Math.sqrt(h));
    }
    
    /**
     * Returns the total distance of the trip.
     * 
     * @return the total distance in kilometers
     */
    public static double totalDistance() {
    	return totalDistance(0,0);
    }
    
    /**
     * Adds up the distance between each point and the next one, using recursion.
     * 
     * @param index the position of the current point
     * @param total the distance added up till now
     * @return the total distance in kilometers
     */
    private static double totalDistance(int index, double total) {
    	if (index >= trip.size() - 1) {
    		return total;
    	}
    	
    	double distance = haversineDistance(trip.get(index), trip.get(index + 1));
    	
    	return totalDistance(index +1, total + distance);
    }
    
    /**
     * Returns the average speed between two points. The order of the points do not matter
     * 
     * @param a the first point
     * @param b second point
     * @return the average speed in kilometers per hour
     */
    public static double avgSpeed(TripPoint a, TripPoint b) {
        double distance = haversineDistance(a, b);

        int minutes = a.getTime() - b.getTime();
        if (minutes < 0) {
            minutes = -minutes;
        }

        double hours = minutes / 60.0;

        return distance / hours;
    }
    	    	
    	
 }