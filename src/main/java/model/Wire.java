package model;

import controller.AllCurvesMethods;
import controller.Methods;
import javafx.scene.Group;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import mains.Configg;

public class Wire {
    private Gate firstgate;
    private Gate secondgate;
    private double length;
    private Line line ;
    private Group allOFCurves = new Group();
    Configg cons = Configg.getInstance();




    public Wire(){
        Configg cons = Configg.getInstance();
        //just make first curve that use for older curve style

        CubicCurve firstCurve = new CubicCurve();

        firstCurve.setStrokeWidth(cons.getLine_width());
        firstCurve.setStroke(cons.getLine_color());
        firstCurve.setFill(null);

        allOFCurves.getChildren().add(firstCurve);





    }
    public Wire(Gate firstgate, Gate secondgate) {
        Configg cons = Configg.getInstance();
        this();
        //first is outer. maybe...
        this.firstgate = firstgate;
        this.secondgate = secondgate;

        CubicCurve firstCurve =(CubicCurve) allOFCurves.getChildren().getFirst();
        AllCurvesMethods.locateACurve(firstCurve,firstgate.getX(),firstgate.getY(),secondgate.getX(),secondgate.getY());
        length= Methods.calculate_wire_length(this);
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
//        CubicCurve firstCurve = new CubicCurve();
//        AllCurvesMethods.locateACurve(firstCurve,firstgate.getX(),firstgate.getY(),secondgate.getX(),secondgate.getY());
//        firstCurve.setStrokeWidth(cons.getLine_width());
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


}
