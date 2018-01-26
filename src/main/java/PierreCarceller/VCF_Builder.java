package PierreCarceller;

import htsjdk.variant.variantcontext.*;

import java.util.*;

class VCF_Builder {

   private Allele refAllele;
   private String refValue;

   VCF_Builder(){

   }

   ArrayList makeAlleles(ArrayList<Comparable> currentRow, ArrayList<String> samplesNames){
       ArrayList<ArrayList> allAlleles = new ArrayList<ArrayList>();
       for(int i=0;i<samplesNames.size();i++) {
           String temp = currentRow.get(i).toString();
           ArrayList<Allele > alleles = new ArrayList<Allele>();
           Allele newAllele;
           if(this.refAllele==null){
               if (temp.substring(0,1).equals("A") || temp.substring(0,1).equals("T") || temp.substring(0,1).equals("G") || temp.substring(0,1).equals("C")){
                   this.refAllele = Allele.create(temp.substring(0,1),true);
                   this.refValue=temp.substring(0,1);
                   alleles.add(this.refAllele);
                   temp = temp.substring(1);
               }else {
                   newAllele = Allele.create(Allele.NO_CALL,false);
                   alleles.add(newAllele);
                   temp = temp.substring(1);
               }
           }
           while (!temp.equals("")){
               if (temp.substring(0,1).equals("A") || temp.substring(0,1).equals("T") || temp.substring(0,1).equals("G") || temp.substring(0,1).equals("C")){
                   if (this.refValue.equals(temp.substring(0,1))){
                       newAllele = Allele.create(temp.substring(0,1),true);
                   }else {
                       newAllele = Allele.create(temp.substring(0,1),false);
                   }
               }else {
                   newAllele = Allele.create(Allele.NO_CALL,false);
               }
               alleles.add(newAllele);
               temp = temp.substring(1);
           }
           allAlleles.add(alleles);
       }
       this.refAllele=null;
       return allAlleles;
   }

   ArrayList makeGenotypes(ArrayList<ArrayList> allAlleles, ArrayList<String> samplesNames){
       ArrayList<Genotype> genotypes = new ArrayList<Genotype>();
       for (int i=0; i<samplesNames.size();i++){
           //ArrayList<Allele> temp = allAlleles.get(i);
           Collections.sort(allAlleles.get(i));
           Genotype genotype = GenotypeBuilder.create(samplesNames.get(i),allAlleles.get(i));
           genotypes.add(genotype);
       }
       return genotypes;
   }

   VariantContext makeVarianContexte(ArrayList<Genotype> genotypes, MarkerPosition markerPosition, ArrayList<Allele> alleleArrayList){
       if (alleleArrayList.size() > 0) {
           VariantContextBuilder variantContextBuilder = new VariantContextBuilder();
           variantContextBuilder.genotypes(genotypes);
           variantContextBuilder.start(markerPosition.getMarkerPosition()); //position
           variantContextBuilder.stop(markerPosition.getMarkerPosition());
           variantContextBuilder.chr(markerPosition.getMarkerChromosome());//chromsome
           variantContextBuilder.alleles(alleleArrayList);
           variantContextBuilder.id(markerPosition.getMarkerName());
           VariantContext variantContext = variantContextBuilder.make();
           return variantContext;
       }else{
           return null;
       }

   }

   ArrayList<Allele> getAllele(ArrayList<ArrayList> arrayListArrayList){
       ArrayList<Allele> result = new ArrayList<Allele>();
       ArrayList<String> alleleValue = new ArrayList<String>();
       for (ArrayList<Allele> array: arrayListArrayList){
           for (Allele allele: array){
               if (!allele.isNoCall()){
                   if (!alleleValue.contains(allele.getBaseString()) && (allele.getBaseString().equals("A")||allele.getBaseString().equals("T")||allele.getBaseString().equals("C")||allele.getBaseString().equals("G"))){
                       alleleValue.add(allele.getBaseString());
                       result.add(allele);
                   }
               }
               if (result.size()>=4){
                   return result;
               }
           }
       }
       return result;
   }
}
