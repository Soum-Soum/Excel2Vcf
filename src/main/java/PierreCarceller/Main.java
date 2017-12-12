package PierreCarceller;

import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.vcf.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

// /home/u017-h433/Bureau/marker_snp-2.xls

// /home/u017-h433/Bureau/T_cacao_diversity.xls


public class Main extends Observable{

    Observer obs;
    final static String DATA_SHEET_NAME_1 = "marker";
    final static String DATA_SHEET_NAME_2 = "data_matrix";

    public Main(Observer obs){
        this.obs=obs;
    }

    void launchConvertion(String pathFileMarker, String pathFileMatrix, String finalPath, View obs) throws IOException {
        System.out.println(pathFileMarker);
        System.out.println(pathFileMatrix);
        this.obs.update(this, "true");
        InformationCatcher informationCatcherMarkersFile = new InformationCatcher(pathFileMarker, obs);
        TreeSet<MarkerPosition> treeOfMarkersPositions = informationCatcherMarkersFile.getMarkersPosition(DATA_SHEET_NAME_1);
        InformationCatcher informationCatcherMatrixFile = new InformationCatcher(pathFileMatrix,obs);
        ArrayList<String> sampelsNames = informationCatcherMatrixFile.getSamplesNames(DATA_SHEET_NAME_2);
        HashMap<String, Integer> stringIntegerHashMap = informationCatcherMatrixFile.getHmap(DATA_SHEET_NAME_2);
        VCF_Builder vcfBuilder = new VCF_Builder();
        HashSet<VCFHeaderLine> hashSet = new HashSet();
        hashSet.add(new VCFFormatHeaderLine("GT", 1, VCFHeaderLineType.String, "Genotype"));
        VCFHeader header = new VCFHeader(hashSet, informationCatcherMatrixFile.getSamplesNames(DATA_SHEET_NAME_2));;
        File file;
        if(!finalPath.equals("")){
            file = new File(finalPath);
        }else {
            file = new File(pathFileMatrix.substring(0,pathFileMatrix.length()-4)+".vcf");
        }
        VariantContextWriterBuilder builder = new VariantContextWriterBuilder();
        builder.setOutputFile(file);
        builder.setOutputFileType(VariantContextWriterBuilder.OutputType.VCF);
        builder.setReferenceDictionary(new SAMSequenceDictionary());
        VariantContextWriter writer = builder.build();
        writer.writeHeader(header);
        for (MarkerPosition markerPosition :treeOfMarkersPositions){
            markerPosition.printMarkerPosition();
            ArrayList<Comparable> currentRow = informationCatcherMatrixFile.getRowWithHmap(stringIntegerHashMap,markerPosition.getMarkerName(),"data_matrix");
            ArrayList<ArrayList> alleleArrayList = vcfBuilder.makeAlleles(currentRow,sampelsNames);
            ArrayList<Genotype> genotypeArrayList = vcfBuilder.makeGenotypes(alleleArrayList, sampelsNames);
            ArrayList<Allele> alleles = vcfBuilder.getAllele(alleleArrayList);
            VariantContext variantContext = vcfBuilder.makeVarianContexte(genotypeArrayList,markerPosition, alleles );
            writer.add(variantContext);
        }
        System.out.println("Done");
        this.obs.update(this, "false");
    }
}
