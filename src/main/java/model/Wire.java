package model;

import controller.AllCurvesMethods;
import javafx.scene.Group;
import javafx.scene.shape.CubicCurve;
import mains.Configg;

import java.util.ArrayList;
import java.util.Collection;

public class Wire {
    private Gate firstgate;
    private Gate secondgate;
    private double length;
    private String state; //can be 1.satisfied 2.have_bug
    private Group allOFCurves = new Group();
    private ArrayList<CurveHandler> CurveHandlers = new ArrayList<>();
    public boolean _state_temp;
    Configg cons = Configg.getInstance();




    public Wire(){
    }
    public Wire(Gate firstgate, Gate secondgate) {

        //first is outer. maybe...
        this.firstgate = firstgate;
        this.secondgate = secondgate;

    }
    public void getClone_justCurvesAndLengthAndHandlers(Wire clone){
        clone.length = length;
        clone.allOFCurves=AllCurvesMethods.allCurve_justData_Clone(this);
        AllCurvesMethods.wire_setLikeSample(clone, (CubicCurve) this.getAllOfCurve_Group().getChildren().getFirst());
        clone.getCurveHandlers().clear();
        clone.getCurveHandlers().addAll((Collection<? extends CurveHandler>) this.getCurveHandlers().clone());
    }


    public ArrayList<CurveHandler> getCurveHandlers() {
        return CurveHandlers;
    }

    public Gate getFirstgate() {
        return firstgate;
    }

    public void setFirstgate(Gate firstgate) {
        this.firstgate = firstgate;
        if(secondgate!= null){
            just_isSafeTo_DrawCurve();
        }
    }

    public Gate getSecondgate() {
        return secondgate;
    }

    //most wire create here (have gates and curve)
    public void setSecondgate(Gate secondgate) {
        this.secondgate = secondgate;

        if(firstgate!= null){
            just_isSafeTo_DrawCurve();
        }
    }

    private void just_isSafeTo_DrawCurve() {
        System.out.println("just_isSafeTo_DrawCurve");
        CubicCurve firstCurve = new CubicCurve();
        AllCurvesMethods.locateACurve(firstCurve,firstgate.getX(),firstgate.getY(),secondgate.getX(),secondgate.getY());
        firstCurve.setStrokeWidth(cons.getLine_width());
        firstCurve.setStroke(cons.getLine_color());
        firstCurve.setFill(null);
        allOFCurves.getChildren().add(firstCurve);

        update_length_AfterNewAllOfCurves();
    }

    private void update_length_AfterNewAllOfCurves() {
        length=AllCurvesMethods.calculateWireLength(this);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }


//    public ArrayList<CubicCurve> getCubicCurvesModels() {
//        return cubicCurves;
//    }

    public Group getAllOfCurve_Group(){
//        updateGroup();
        return allOFCurves;
    }

//    private void updateGroup() {
//        //maybe affect on performance
//        boolean changed = false;
//        for(CubicCurve c1 : cubicCurves){
//            boolean isNew=true;
//            for (Node c2:allOFCurves.getChildren()) {
//                if (Objects.equals(c1,c2)) {
//                    isNew=false;
//                }
//            }
//            if(isNew){
//                allOFCurves.getChildren().add(c1);
//                changed=true;
//            }
//        }
//        if(changed){
//            CubicCurve sampleCurve=(CubicCurve) allOFCurves.getChildren().getFirst();
//            AllCurvesMethods.wire_setLikeSample(this,sampleCurve);
//        }
//    }

    public Wire cloneWire() {
        return new Wire(this.firstgate, this.secondgate);
    }


    public void newCurveHandlerAdded(CurveHandler curveHandler) {
        CubicCurve cubicCurve = new CubicCurve();
        allOFCurves.getChildren().add(cubicCurve);

    }
    private int curveHandler_index(CurveHandler curveHandler){
        //just count that curve handler what time added
        for(int i=0;i<CurveHandlers.size();i++){
            if(CurveHandlers.get(i).equals(curveHandler)){
                return i;
            }
        }
        System.out.println("Error cubic curve handler not found");
        return -1;
    }
}
