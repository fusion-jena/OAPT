
package fusion.oapt.algorithm.partitioner.SeeCOnt;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.ibm.icu.text.DecimalFormat;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.model.Node;

public class EvaluationSeeCOnt 
{
	public static String NumConClusString = null;
	ModelBuild MB;
	private  LinkedHashMap<Integer, Cluster> clusters;
	
	public EvaluationSeeCOnt(){}
	public EvaluationSeeCOnt(ModelBuild MB,  LinkedHashMap<Integer, Cluster> clusters)
	{
		this.MB=MB;
		this.clusters=clusters;// new LinkedHashMap<Integer, Cluster>();
	}
	
public void  Evaluation_SeeCont(){ 

	// ****************** call evaluation process
	
	ModuleEvaluation EP = new ModuleEvaluation(MB,clusters);
    EP.Eval_SeeCont();
	int size=clusters.size();
	// ********************Evaluation of Size****************************
    OPATgui.Result1_Details.setText(null);
	int sumSiz =0;
	for (int i = 0; i< size; i++  ){
		//OPATgui.Result1_Details.append("Size of Cluster "+ i+ " :\t "+ String.valueOf(CClustering.sizeCluster[i])+'\n');
		OPATgui.Result1_Details.append("Size of Cluster "+ i+ " :\t "+ String.valueOf(clusters.get(i).getSize())+'\n');
		sumSiz = sumSiz + clusters.get(i).getSize();
	}
	OPATgui.AvgTextBox1.setText(String.valueOf(new DecimalFormat("##.###").format(sumSiz / size)));
		
	///////////////////////////////////////////////////////////////////////////////////////
	// ********************Evaluation of Coupling ***************************
	OPATgui.Result2_Details.setText(null);
	double sumHeMo =0;
	for (int i = 0; i< size; i++  ){
		OPATgui.Result2_Details.append("Coupling of Cluster " + i + " :\t " + String.valueOf(new DecimalFormat("##.###").format(ModuleEvaluation.HeMo[i])+'\n'));
		sumHeMo = sumHeMo + ModuleEvaluation.HeMo [i];
	}
	OPATgui.AvgTextBox2.setText(String.valueOf(new DecimalFormat("##.###").format(sumHeMo/size)));
	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//********************Evaluation of Cohesion ***************************
	OPATgui.Result3_Details.setText(null);
	
	double sumHoMo =0;
	for (int i = 0; i< size; i++  ){
		OPATgui.Result3_Details.append("Cohesion of Cluster " + i + " :\t " + String.valueOf(new DecimalFormat("##.###").format(ModuleEvaluation.HoMo[i])+'\n'));
		sumHoMo = sumHoMo + ModuleEvaluation.HoMo[i];
	}

	OPATgui.AvgTextBox3.setText(String.valueOf(new DecimalFormat("##.###").format(sumHoMo/size)));
	
	
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private  int findSahreinfo(Node inode, Node jnode){
	int numinfo =0;
	
	if (inode != null && jnode != null)	{
		if (inode != jnode){
			ArrayList<Node> isurround = MB.Connexion(inode);
			ArrayList<Node> jsurround =MB.Connexion(jnode);
			   for(int i=0;i<isurround.size();i++)
			   {
				   Node ind =isurround.get(i);
				   if(jsurround.contains(ind)) numinfo++;
			   }
		}
	}   
	return numinfo;
}

}