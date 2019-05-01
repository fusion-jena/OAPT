package fusion.oapt.generalTest;

import java.util.Calendar;

//import fusion.oapt.algorithm.matcher.Matching;
import fusion.oapt.general.cc.Configuration;
import fusion.oapt.general.output.Alignment;

public class MatchingTest {

	public static void main(String args[])
    {
        System.out.println(Calendar.getInstance().getTime().toString() + "\n");

        Configuration config = new Configuration();
        config.init();
        
        //String onto = "202";
        String fp1 = "D:/Naouel_GFBio/ontology/envo.owl";//"file:./sample/benchmarks/101/onto.rdf";
        String fp2 = "D:/Naouel_GFBio/ontology/pato.owl";//"file:./sample/benchmarks/" + onto + "/onto.rdf";
        String fp3 = "D:/test_ont/test/cmt.owl";//"./sample/benchmarks/" + onto + "/falcon.rdf";
        String fp4 = "D:/test_ont/test/ekaw.owl";//"./sample/benchmarks/" + onto + "/refalign.rdf"; 
        
        /* String fp1 = "file:./sample/anatomy/mouse_anatomy_2010.owl";
        String fp2 = "file:./sample/anatomy/nci_anatomy_2010.owl";
        String fp3 = "./sample/anatomy/falcon.rdf";
        String fp4 = "./sample/anatomy/partial_reference_2010.rdf"; */
        
//        Controller controller = new Controller(fp1, fp2);
        //Matching controller = new Matching (fp1,fp2);
       // Alignment alignment = controller.run();
      //  controller.writeToFile(alignment);
     //    controller.writeToFile(alignment, fp3);
        // if(Parameters.reference != null) {
    //        controller.evaluate(alignment, fp4);
        // }

      //  System.out.println("\n" + Calendar.getInstance().getTime().toString()+"\t"+alignment.size());
    } 

}
