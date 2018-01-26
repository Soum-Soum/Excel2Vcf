package PierreCarceller;

public class MarkerPosition implements Comparable {
    private String markerName;
    private String markerChromosome;
    private Integer markerPosition;

    MarkerPosition(String markerName, String markerChromosome, Integer markerPosition) {
        this.markerName = markerName;
        this.markerChromosome = markerChromosome;
        this.markerPosition = markerPosition;
    }
//+ this.markerName + "\nMarker Chromosome : "+ this.markerChromosome + "\nMarker Position : " + this.markerPosition
    void printMarkerPosition(){
        System.out.println("Marker Name : " + this.markerName);
        System.out.println("Marker Chromosome : "+ this.markerChromosome);
        System.out.println("Marker Position : " + this.markerPosition);
    }

    public int compareTo(Object o) {
        MarkerPosition temp = (MarkerPosition) o;
        int valueOfCompare = this.markerChromosome.toLowerCase().compareTo(temp.markerChromosome.toLowerCase());
        return valueOfCompare!=0 ? valueOfCompare : this.markerPosition.compareTo(temp.markerPosition);
    }

    String getMarkerName() {
        return markerName;
    }

    String getMarkerChromosome() {
        return markerChromosome;
    }

    Integer getMarkerPosition() {
        return markerPosition;
    }

}
