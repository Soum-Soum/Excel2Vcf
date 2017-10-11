package PierreCarceller;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;

public class MarkerPosition implements Comparable {
    private String markerName;
    private String markerChromosome;
    private Integer markerPosition;

    public MarkerPosition(String markerName, String markerChromosome, Integer markerPosition) {
        this.markerName = markerName;
        this.markerChromosome = markerChromosome;
        this.markerPosition = markerPosition;
    }

    public void printMarkerPosition(){
        System.out.println("Marker Name : " + this.markerName);
        System.out.println("Marker Chromosome : "+ this.markerChromosome);
        System.out.println("Marker Position : " + this.markerPosition);
    }

    public int compareTo(Object o) {
        MarkerPosition temp = (MarkerPosition) o;
        int valueOfCompare = this.markerChromosome.compareTo(temp.markerChromosome);
        return valueOfCompare!=0 ? valueOfCompare : this.markerPosition.compareTo(temp.markerPosition);
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getMarkerChromosome() {
        return markerChromosome;
    }

    public void setMarkerChromosome(String markerChromosome) {
        this.markerChromosome = markerChromosome;
    }

    public Integer getMarkerPosition() {
        return markerPosition;
    }

    public void setMarkerPosition(Integer markerPosition) {
        this.markerPosition = markerPosition;
    }
}
