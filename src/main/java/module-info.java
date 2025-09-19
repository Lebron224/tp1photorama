module ca.qc.bdeb.sim.tp1photorama {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.qc.bdeb.sim.tp1photorama to javafx.fxml;
    exports ca.qc.bdeb.sim.tp1photorama;
}