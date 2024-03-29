
package fusion.oapt.general.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import fusion.oapt.algorithm.partitioner.SeeCOnt.CClustering;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Cluster;
import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.model.Node;

public class SaveOutput
{
 private RandomAccessFile raf = null;
 private String filepath = null;
 private ArrayList<String> writeList = null;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public SaveOutput(String fp, int index)
 {
     filepath = fp;
     try {
         File file = new File(fp);
         if (file.exists()) {
             file.delete();
         }
         raf = new RandomAccessFile(filepath, "rw");
         writeList = new ArrayList<String>();
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     } 
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public String write(String onto1, String uri1)
 {
     writeNS();
     writeStart("yes", "0", "??", onto1,  uri1);
     for (int i=0; i<Coordinator.KNumCH; i++){
       	Cluster c = Coordinator.clusters.get(i);
       	Iterator<Node> x= c.listElements();
       	while (x.hasNext()){           		
        		Node element= x.next();
        		writeElement(element);
        		}
     	}
         
     writeEnd();
     String s = writeToFile();
     try {
         raf.close();
     } catch (IOException e) {
         e.printStackTrace();
     }
     return s;
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public void writeNS()
 {
     String temp = "<?xml version='1.0' encoding='utf-8'?>\n" 
             + "<rdf:RDF xmlns='http://knowledgeweb.semanticweb.org/heterogeneity/alignment' \n" 
             + "xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' \n" 
             + "xmlns:xsd='http://www.w3.org/2001/XMLSchema#'>\n";
     writeList.add(temp);
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public void writeStart(String xml, String level, String type, 
 		String onto1, String uri1)
 {
     String temp = "<Alignment>\n" 
             + "  <xml>" + xml + "</xml>\n" 
             + "  <level>" + level + "</level>\n"
             + "  <type>" + type + "</type>\n" 
             + "  <onto1>" + onto1 + "</onto1>\n" 
             + "  <uri1>" + uri1 + "</uri1>"; 
     writeList.add(temp);
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public void writeEnd()
 {
     String temp = "  </Alignment>\n</rdf:RDF>";
     writeList.add(temp);
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public void writeElement(Node res1)
 {
     String temp = "      <Cell>\n" 
             + "        <entity rdf:resource=\"" + res1 + "\"/>\n"  
             + "      </Cell>\n" ;
     writeList.add(temp);
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public String writeToFile()
 {
 	StringBuffer s = new StringBuffer();
     try {
         for (int i = 0, n = writeList.size(); i < n; i++) {
             raf.seek(raf.length());
             String t = writeList.get(i) + "\r\n";
             s.append(t);
             raf.writeBytes(t);
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
     return s.toString();
 }

 
}
